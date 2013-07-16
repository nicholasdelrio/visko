// The MIT License
//
// Copyright (c) 2004 Evren Sirin
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to
// deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
// sell copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/*
 * Created on Dec 29, 2003
 */
package impl.owls.process.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.exceptions.UnsatisfiedPreconditionException;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AsProcess;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.execution.BaseExecutionContext;
import org.mindswap.owls.process.execution.ExecutionSupport;
import org.mindswap.owls.process.execution.ExecutionValidator;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.execution.ProcessExecutionMonitor;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.LocBinding;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author unascribed
 * @version $Rev: 2339 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class ProcessExecutionEngineImpl implements ProcessExecutionEngine,
	ExecutionSupport<BaseExecutionContext>
{
	private static final Logger logger = LoggerFactory.getLogger(ProcessExecutionEngineImpl.class);

	protected boolean caching;
	protected final AtomicBoolean working;
	protected ExecutionValidator executionValidator;
	protected final List<ProcessExecutionMonitor> monitors;
	protected OWLKnowledgeBase env;
	protected final Map<Perform, ValueMap<ProcessVar, OWLValue>> performResults;
	protected Process process;

	public ProcessExecutionEngineImpl()
	{
		executionValidator = new StandardExecutionValidator();

		// modification may occur (add, remove monitor) while iterating for the purpose of sending notifications
		monitors = new CopyOnWriteArrayList<ProcessExecutionMonitor>();

		// TODO synchronize access for Split/Split-Join --> is a thread safe version sufficient?
		performResults = new HashMap<Perform, ValueMap<ProcessVar, OWLValue>>();

		working = new AtomicBoolean(false);

		// caching is used by default: repeated execution of control constructs reuses instances
		caching = true;
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#getExecutionValidator() */
	public ExecutionValidator getExecutionValidator()
	{
		return executionValidator;
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#setExecutionValidator(org.mindswap.owls.process.execution.ExecutionValidator) */
	public ExecutionValidator setExecutionValidator(final ExecutionValidator ev)
	{
		if (working.get()) throw new IllegalStateException(
			"Setting the execution validator is forbidden while an execution is on the way.");

		final ExecutionValidator previous = executionValidator;
		executionValidator = ev;
		return previous;
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#setCaching(boolean) */
	public boolean setCaching(final boolean caching)
	{
		if (working.get()) throw new IllegalStateException(
			"Setting caching policy is forbidden while an execution is on the way.");

		final boolean previous = caching;
		this.caching = caching;
		return previous;
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#addMonitor(org.mindswap.owls.process.execution.ProcessExecutionMonitor) */
	public boolean addMonitor(final ProcessExecutionMonitor monitor)
	{
		return monitors.add(monitor);
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#removeMonitor(org.mindswap.owls.process.execution.ProcessExecutionMonitor) */
	public boolean removeMonitor(final ProcessExecutionMonitor monitor)
	{
		return monitors.remove(monitor);
	}

	protected ExecutionException executionFailed(final String msg)
	{
		return executionFailed(new ExecutionException(msg, process));
	}

	protected ExecutionException executionFailed(final Exception e)
	{
		return executionFailed(new ExecutionException(e, process));
	}

	protected ExecutionException executionFailed(final ExecutionException e)
	{
		for (final ProcessExecutionMonitor monitor : monitors)
		{
			monitor.executionFailed(e);
		}
		return e;
	}

	protected boolean passFilter(final ProcessExecutionMonitor monitor, final Process p)
	{
		int processType = Process.ANY;
		if (p instanceof AtomicProcess) processType = Process.ATOMIC;
		else if (p instanceof CompositeProcess) processType = Process.COMPOSITE;
		else processType = Process.SIMPLE;

		return (processType & monitor.getMonitorFilter()) != 0;
	}

	protected void executionStarted(final Process p, final ValueMap<Input, OWLValue> inputs)
	{
		for (final ProcessExecutionMonitor monitor : monitors)
		{
			if (passFilter(monitor, p)) monitor.executionStarted(p, inputs);
		}
	}

	protected void executionFinished(final Process p, final ValueMap<Input, OWLValue> inputs,
		final ValueMap<Output, OWLValue> outputs)
	{
		for (final ProcessExecutionMonitor monitor : monitors)
		{
			if (passFilter(monitor, p)) monitor.executionFinished(p, inputs, outputs);
		}
	}

	protected void executionStarted()
	{
		for (final ProcessExecutionMonitor monitor : monitors)
		{
			monitor.executionStarted();
		}
	}

	protected void executionFinished()
	{
		for (final ProcessExecutionMonitor monitor : monitors)
		{
			monitor.executionFinished();
		}
	}

	protected void init(final Process p, final OWLKnowledgeBase kb)
	{
		this.env = (kb != null)? kb : p.getKB();
		this.process = p;
		performResults.clear();
		working.set(true);
	}

	protected void cleanup()
	{
		env = null;
		process = null;
		working.set(false);
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#execute(org.mindswap.owls.process.Process, org.mindswap.owl.OWLKnowledgeBase) */
	public ValueMap<Output, OWLValue> execute(final Process p, final OWLKnowledgeBase kb) throws ExecutionException
	{
		return execute(p, new ValueMap<Input, OWLValue>(), kb);
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#execute(org.mindswap.owls.process.Process, org.mindswap.query.ValueMap) */
	public ValueMap<Output, OWLValue> execute(final Process p, final ValueMap<Input, OWLValue> inputs,
		final OWLKnowledgeBase kb) throws ExecutionException
	{
		init(p, kb);
		try
		{
			executionStarted(p, inputs);

			final BaseExecutionContext context = new BaseExecutionContext(caching);
			context.addInputs(inputs);
			p.prepare(context).execute(context, this);
			final ValueMap<Output, OWLValue> results = context.getOutputs();

			executionFinished(p, inputs, results);
			return results;
		}
		catch (final ExecutionException e)
		{
			throw executionFailed(e); // notify listeners
		}
		finally
		{
			cleanup();
		}
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionEngine#execute(org.mindswap.owls.process.Perform, org.mindswap.owl.OWLKnowledgeBase) */
	public ValueMap<Output, OWLValue> execute(final Perform p, final OWLKnowledgeBase kb) throws ExecutionException
	{
		init(p.getProcess(), kb);
		final BaseExecutionContext context = new BaseExecutionContext(caching);

		final ValueMap<Output, OWLValue> results;
		try
		{
			p.execute(context, this);
			results = context.getOutputs();
			executionFinished(process, context.getInputs(), results);
		}
		catch (final ExecutionException e)
		{
			throw executionFailed(e); // notify listeners
		}
		finally
		{
			cleanup();
		}
		return results;
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeAtomicProcess(org.mindswap.owls.process.AtomicProcess, java.lang.Object) */
	public void executeAtomicProcess(final AtomicProcess p, final BaseExecutionContext context)
		throws ExecutionException
	{
		executionValidator.checkPreconditions(p, context.getValues(), env);

		final AtomicGrounding<?> grounding = p.getGrounding();
		if (grounding == null) throw new ExecutionException("No grounding for " + p);

		logger.debug("Executing atomic process {} by invoking {}", process, grounding);

		final ValueMap<Output, OWLValue> outputs = grounding.invoke(context.getInputs(), env);
		context.addOutputs(outputs);

		executionValidator.checkResults(p, context.getValues(), env);
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeCompositeProcess(org.mindswap.owls.process.CompositeProcess, java.lang.Object) */
	public void executeCompositeProcess(final CompositeProcess cp, final BaseExecutionContext context)
		throws ExecutionException
	{
		initializeLocals(cp, context);

		executionValidator.checkPreconditions(cp, context.getValues(), env);

		// After precondition evaluation because it may end with PreconditionException,
		// and it may bind Existential variables (i.e. assign a value).
		final ValueMap<ProcessVar, OWLValue> prevThisPerform = performResults.get(OWLS.Process.ThisPerform);
		performResults.put(OWLS.Process.ThisPerform, context.getValues());

		try
		{
			cp.getComposedOf().execute(context, this);

			final OWLIndividualList<Result> results = cp.getResults();
			// TODO more than one result may exist --> consolidate with result checking in ExecutionValidator
			if (results.size() > 0)
			{
				ProcessExecutionUtil.processValues(performResults, context.getValues(), results.get(0).getBindings());
			}

			executionValidator.checkResults(cp, context.getValues(), env);
		}
		finally
		{
			// Restore previous this perform results in case of execution exception. Otherwise,
			// the associated value map would get lost but subsequent executions may depend on it.
			performResults.put(OWLS.Process.ThisPerform, prevThisPerform);
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeSimpleProcess(org.mindswap.owls.process.SimpleProcess, java.lang.Object) */
	public void executeSimpleProcess(final SimpleProcess sp, final BaseExecutionContext context)
		throws ExecutionException
	{
		final AtomicProcess ap = sp.getAtomicProcess();
		final CompositeProcess cp = sp.getCompositeProcess();

		if (ap != null && cp == null) ap.execute(context, this);
		else if (ap == null && cp != null) cp.execute(context, this);
		else throw new ExecutionException("SimpleProcess must be either realized by AtomicProcess xor " +
			"expand to CompositeProcess, but not both or neither of them.");
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executePerform(org.mindswap.owls.process.Perform, java.lang.Object) */
	public void executePerform(final Perform perform, final BaseExecutionContext context)
		throws ExecutionException
	{
		final Process performProcess = perform.getProcess();
		if (performProcess == null) throw executionFailed("Invalid: Perform " + perform + " without process!");

		final ValueMap<ProcessVar, OWLValue> values = new ValueMap<ProcessVar, OWLValue>();

		final List<InputBinding> bindings = perform.getBindings();
		ProcessExecutionUtil.processValues(performResults, values, bindings);

		final BaseExecutionContext newContext = new BaseExecutionContext(values, caching);

		executionStarted(performProcess, newContext.getInputs());
		try
		{
			performProcess.execute(newContext, this);
			executionFinished(performProcess, newContext.getInputs(), newContext.getOutputs());
		}
		catch (final ExecutionException e)
		{
			executionFailed(e);
			throw e;
		}
		finally
		{
			performResults.put(perform, newContext.getValues());
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeProduce(org.mindswap.owls.process.Produce, java.lang.Object) */
	public void executeProduce(final Produce produce, final BaseExecutionContext context)
		throws ExecutionException
	{
		final ValueMap<ProcessVar, OWLValue> values = performResults.get(OWLS.Process.ThisPerform);

		// first, produce all outputs
		OWLIndividualList<? extends Binding<?>> bindings = produce.getOutputBindings();
		ProcessExecutionUtil.processValues(performResults, values, bindings);

		// second, do the same for all links
		bindings = produce.getLinkBindings();
		ProcessExecutionUtil.processValues(performResults, values, bindings);
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeSequence(org.mindswap.owls.process.Sequence, java.lang.Object) */
	public void executeSequence(final Sequence seq, final BaseExecutionContext context)
		throws ExecutionException
	{
		final OWLIndividualList<ControlConstruct> ccList = seq.getConstructs();

		for (final ControlConstruct cc : ccList)
		{
			cc.execute(context, this);
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeSet(org.mindswap.owls.process.Set, java.lang.Object) */
	public void executeSet(final org.mindswap.owls.process.Set set, final BaseExecutionContext context)
		throws ExecutionException
	{
		final OWLIndividualList<LocBinding> assignments = set.getBindings();
		ProcessExecutionUtil.processValues(performResults, context.getValues(), assignments);
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeAnyOrder(org.mindswap.owls.process.AnyOrder, java.lang.Object) */
	public void executeAnyOrder(final AnyOrder ao, final BaseExecutionContext context) throws ExecutionException
	{
		final OWLIndividualList<ControlConstruct> queue = ao.getConstructs();

		// AnyOrder says it doesn't matter in which order sub elements are executed
		// (but not concurrently), so let's begin in the sequential order. If some
		// constructs precondition is not satisfied and we are not reaching stagnation
		// reschedule execution of this construct, so that this yields another order.
		int i = 0;
		while (queue.size() > 0)
		{
			final ControlConstruct cc = queue.remove(0);
			i++;
			try
			{
				cc.execute(context, this);
				i = 0;
			}
			catch (final UnsatisfiedPreconditionException e)
			{
				queue.add(cc);
				if (i >= queue.size())
				{
					logger.debug("No execution progress for AnyOrder because of unsatisfied preconditions. Exeception will be propagated upwards.");
					throw e;
				}
				logger.debug("Reschedule execution of control construct {} in AnyOrder because of unsatisfied precondition.", cc);
			}
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeAsProcess(org.mindswap.owls.process.AsProcess, java.lang.Object) */
	public void executeAsProcess(final AsProcess ap, final BaseExecutionContext context) throws ExecutionException
	{
		// TODO implement execution of AsProcess control construct
		throw new UnsupportedOperationException("Execution of AsProcess constructs is not yet supported.");
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeChoice(org.mindswap.owls.process.Choice, java.lang.Object) */
	public void executeChoice(final Choice choice, final BaseExecutionContext context) throws ExecutionException
	{
		final OWLIndividualList<ControlConstruct> ccList = choice.getConstructs();

		// Choose a random order for execution
		final List<Integer> indexes = ProcessExecutionUtil.createRandomIntegers(ccList.size());

		// try to execute the construct selected as a first choice. If its precondition is
		// not satisfied and there exist more constructs then chose another one and try to
		// execute this, unless there are no more alternatives
		boolean notDone = true;
		while (indexes.size() > 0 && notDone)
		{
			final int index = indexes.remove(0);
			final ControlConstruct cc = ccList.get(index);

			try
			{
				cc.execute(context, this);
				notDone = false;
			}
			catch (final UnsatisfiedPreconditionException e)
			{
				if (indexes.isEmpty())
				{
					logger.debug("Unsatisifed precondition in Choice and no alternative left. Exception will be propagated upwards.");
					throw e;
				}
				logger.debug("Discard choice construct {} because its precondition(s) are not satisfied. Trying to execute next choice now.", cc);
			}
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeIfThenElse(org.mindswap.owls.process.IfThenElse, java.lang.Object) */
	public void executeIfThenElse(final IfThenElse ifThenElse, final BaseExecutionContext context)
		throws ExecutionException
	{
		final Condition<?> ifCondition = ifThenElse.getCondition();
		final ControlConstruct thenCC = ifThenElse.getThen();
		final ControlConstruct elseCC = ifThenElse.getElse();

		if (isTrue(ifCondition)) thenCC.execute(context, this);
		else if (elseCC != null) elseCC.execute(context, this);
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeRepeatWhile(org.mindswap.owls.process.RepeatWhile, java.lang.Object) */
	public void executeRepeatWhile(final RepeatWhile cc, final BaseExecutionContext context)
		throws ExecutionException
	{
		final Condition<?> whileCondition = cc.getCondition();
		final ControlConstruct loopBody = cc.getComponent();

		while (isTrue(whileCondition))
		{
			loopBody.execute(context, this);
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeRepeatUntil(org.mindswap.owls.process.RepeatUntil, java.lang.Object) */
	public void executeRepeatUntil(final RepeatUntil cc, final BaseExecutionContext context)
		throws ExecutionException
	{
		final Condition<?> repeatCondition = cc.getCondition();
		final ControlConstruct loopBody = cc.getComponent();

		do
		{
			loopBody.execute(context, this);
		}
		while (isTrue(repeatCondition));
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeForEach(org.mindswap.owls.process.ForEach, java.lang.Object) */
	public void executeForEach(final ForEach fe, final BaseExecutionContext context) throws ExecutionException
	{
		final ValueMap<ProcessVar, OWLValue> thisPerformValues = performResults.get(OWLS.Process.ThisPerform);

		final ControlConstruct loopBody = fe.getComponent();
		final ProcessVar loopVar = fe.getLoopVar();
		final ValueOf valueOf = fe.getListValue();

		final Perform otherPerform = valueOf.getPerform();
		final Parameter otherParam = valueOf.getParameter();

		final ValueMap<ProcessVar, OWLValue> performResult = performResults.get(otherPerform);
		if (performResult == null) throw executionFailed("Process variable value mappings of perform " +
			otherPerform + " not found in execution state! This is likely caused by an invalid data flow " +
			"specification inside " + fe);

		final OWLIndividual ind = performResult.getIndividualValue(otherParam);
		final OWLList<OWLValue> list = ind.castToList(RDF.ListVocabulary);

		for (final OWLValue value : list)
		{
			thisPerformValues.setValue(loopVar, value);
			loopBody.execute(context, this);
		}
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeSplit(org.mindswap.owls.process.Split, java.lang.Object) */
	public void executeSplit(final Split split, final BaseExecutionContext context)
		throws ExecutionException
	{
		executeParallel(split.getConstructs(), context, false);
	}

	/* @see org.mindswap.owls.process.execution.ExecutionSupport#executeSplitJoin(org.mindswap.owls.process.SplitJoin, java.lang.Object) */
	public void executeSplitJoin(final SplitJoin splitJoint, final BaseExecutionContext context)
		throws ExecutionException
	{
		executeParallel(splitJoint.getConstructs(), context, true);
	}

	/**
	 * Get Loc variables of composite process that specify an initial value and
	 * put them to the execution context, thus, initialize them.
	 */
	protected void initializeLocals(final CompositeProcess cp, final BaseExecutionContext context)
	{
		final ValueMap<Local, OWLValue> locals = new ValueMap<Local, OWLValue>();
		for (final Local local : cp.getLocals())
		{
			if (local instanceof Loc)
			{
				final OWLValue initialValue = ((Loc) local).getInitialValue();
				if (initialValue != null) locals.setValue(local, initialValue);
			}
		}
		context.addLocals(locals);
	}

	protected boolean isTrue(final Condition<?> condition)
	{
		final ValueMap<ProcessVar, OWLValue> binding = performResults.get(OWLS.Process.ThisPerform);
		return condition.isTrue(env, binding);
	}

	protected boolean isTrue(final Condition<?> condition, final ValueMap<Parameter, OWLValue> binding)
	{
		return condition.isTrue(env, binding);
	}

	private final class ProcessExecutionThread extends Thread
	{
		final BaseExecutionContext context;
		final ControlConstruct cc;
		Exception exception;

		ProcessExecutionThread(final ControlConstruct cc, final BaseExecutionContext context, final String name)
		{
			super(name);
			this.cc = cc;
			this.context = context;
		}

		/* @see java.lang.Thread#run() */
		@Override public void run()
		{
			try
			{
				cc.execute(context, ProcessExecutionEngineImpl.this);
			}
			catch (final Exception e) // ExecutionException, RuntimeException
			{
				exception = e;
			}
		}
	}

	protected void executeParallel(final OWLIndividualList<ControlConstruct> constructs,
		final BaseExecutionContext context, final boolean join) throws ExecutionException
	{
		final List<ProcessExecutionThread> threads = new ArrayList<ProcessExecutionThread>();

		for (final ControlConstruct construct : constructs)
		{
			String name = construct.getLocalName();
			name = (name == null)? construct.getConstructName() : name;
			name = name + " execution thread";
			final ProcessExecutionThread t = new ProcessExecutionThread(construct, context, name);
			threads.add(t);
			logger.debug("Starting {} ...", t.getName());
			t.start();
		}

		if (join)
		{
			final Set<Exception> exceptions = new HashSet<Exception>();
			Exception ee;
			for (final ProcessExecutionThread t : threads)
			{
				try
				{
					logger.debug("Waiting for {} to finish...", t.getName());
					t.join();
					ee = t.exception;
					logger.debug("{} finished {}", t.getName(),
						(ee == null)? "successfully" : "with exception " + ee.toString());
					if (ee != null) exceptions.add(ee);
				}
				catch (final InterruptedException e)
				{
					exceptions.add(e);
					throw executionFailed(e);
				}
			}
			if (exceptions.size() > 0) throw new ExecutionException(exceptions);
		}
	}
}
