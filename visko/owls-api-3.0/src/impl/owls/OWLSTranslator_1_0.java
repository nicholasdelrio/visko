//The MIT License
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
 * Created on Dec 27, 2003
 */
package impl.owls;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLProperty;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.RDFS;
import org.mindswap.owls.OWLSVersionTranslator;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.grounding.AtomicGrounding;
import org.mindswap.owls.grounding.Grounding;
import org.mindswap.owls.grounding.UPnPAtomicGrounding;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.Conditional;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Iterate;
import org.mindswap.owls.process.MultiConditional;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.FLAServiceOnt;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.vocabulary.OWLS_1_0;
import org.mindswap.owls.vocabulary.OWLS_1_1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class OWLSTranslator_1_0 implements OWLSVersionTranslator {
	private static final Logger logger = LoggerFactory.getLogger(OWLSTranslator_1_0.class);

	private OWLOntology inputOnt;
	private OWLOntology outputOnt;
	/*******************************************
	 ** added by guang huang @2005-4-12 **
	 ********************************************/
	private final List<URI> profileInputURIs;
	/*******************************************
	 ** end by guang huang **
	 ********************************************/

	/**
	 * Used to store already translated individuals in order to prevent
	 * re-translation if they are references multiple times (optimization).
	 * Key: original individual, Value: translated individual.
	 */
	private Map<OWLIndividual, OWLIndividual> translation;

	/**
	 *
	 */
	public OWLSTranslator_1_0()
	{
		profileInputURIs = new ArrayList<URI>();
	}

	/* @see org.mindswap.owls.OWLSVersionTranslator#getVersion() */
	public String getVersion()
	{
		return OWLS_1_0.version;
	}

	/* @see org.mindswap.owls.OWLSVersionTranslator#canTranslate(org.mindswap.owl.OWLOntology) */
	public boolean canTranslate(final OWLOntology ontology)
	{
		return false; // TODO bring back to life <-- this class is currently not maintained
//		return ontology.getInstances(OWLS_1_0.Service.Service, false).size() > 0;
	}

	/* @see org.mindswap.owls.OWLSVersionTranslator#translate(org.mindswap.owl.OWLOntology, org.mindswap.owl.OWLOntology) */
	public OWLOntology translate(final OWLOntology inpOnt, final OWLOntology outOnt)
	{
		this.inputOnt = inpOnt;
		this.outputOnt = outOnt;
		translation = new HashMap<OWLIndividual, OWLIndividual>();

		translateServices();
		translateProfiles();
		translateProcesses();
		translateGroundings();
		translateAtomicGroundings();

		outputOnt.addImports(inputOnt.getImports(false)); // imports are added as is, see interface JavaDoc

		outputOnt.setTranslationSource(inputOnt);
		/*******************************************
		 ** added by guang huang @2005-4-8 **
		 *corrrect source version bug
		 ********************************************/
		if (canTranslate(inputOnt))
			outputOnt.addType(inputOnt.getInstances(OWLS_1_0.Service.Service, false).get(0), OWLS_1_0.Service.Service);
		/*******************************************
		 ** end by guang huang **
		 ********************************************/

		return outputOnt;
	}

	private void translateServices()
	{
		final OWLIndividualList<?> list = inputOnt.getInstances(OWLS_1_0.Service.Service, false);
		for (OWLIndividual serviceInfo : list)
		{
			createService(serviceInfo);
		}
	}

	private void translateProfiles()
	{
		final OWLIndividualList<?> list = inputOnt.getInstances(OWLS_1_0.Profile.Profile, false);
		for (OWLIndividual profileInfo : list)
		{
			createProfile(profileInfo);
		}
	}

	private void translateProcesses()
	{
		// we are not using reasoning so loop through all possible process instances
		final OWLClass[] processClass = {OWLS_1_0.Process.AtomicProcess,
			OWLS_1_0.Process.CompositeProcess, OWLS_1_0.Process.SimpleProcess};
		for (final OWLClass element : processClass)
		{
			final OWLIndividualList<?> list = inputOnt.getInstances(element, false);
			for (OWLIndividual processInfo : list)
			{
				createProcess(processInfo);
			}
		}
	}

	private void translateGroundings()
	{
		final OWLClass[] groundingClass = {OWLS_1_0.Grounding.WsdlGrounding, FLAServiceOnt.UPnPGrounding};
		for (final OWLClass element : groundingClass)
		{
			final OWLIndividualList<?> list = inputOnt.getInstances(element, false);
			for (OWLIndividual groundingInfo : list)
			{
				createGrounding(groundingInfo);
			}
		}
	}

	private void translateAtomicGroundings()
	{
		final OWLClass[] groundingClass = {OWLS_1_0.Grounding.WsdlAtomicProcessGrounding,
			FLAServiceOnt.UPnPAtomicProcessGrounding};
		for (final OWLClass element : groundingClass)
		{
			final OWLIndividualList<?> list = inputOnt.getInstances(element, false);
			for (OWLIndividual groundingInfo : list)
			{
				createAPGrounding(groundingInfo);
			}
		}
	}

	private Perform getCachedPerform(final Process process, final OWLIndividual ind)
	{
		final List<ControlConstruct> components = ((CompositeProcess)process).getComposedOf().getConstructs();
		for (ControlConstruct cc : components)
		{
			if (cc instanceof Perform && ((Perform) cc).getProcess().equals(ind)) return (Perform) cc;
		}

		return null;
	}

	private OWLIndividual translate(final OWLIndividual ind, final OWLClass owlClass)
	{
		OWLIndividual translated = translation.get(ind);
		if (translated == null)
		{
			if (ind.isAnon()) translated = outputOnt.createInstance(owlClass, null);
			else translated = outputOnt.createInstance(owlClass, ind.getURI());
			if (owlClass != null) translated.addType(owlClass);
			translation.put(ind, translated);
		}

		return translated;
	}

	private <T extends OWLIndividual> T translate(final OWLIndividual untranslated, final Class<T> javaClass,
		final OWLClass owlClass)
	{
		T translated = translation.get(untranslated).castTo(javaClass);
		if (translated == null)
		{
			OWLIndividual newInd;
			if (untranslated.isAnon()) newInd = outputOnt.createInstance(owlClass, null);
			else newInd = outputOnt.createInstance(owlClass, untranslated.getURI());
			translated = newInd.castTo(javaClass);
			translation.put(untranslated, translated);
		}

		return translated;
	}

	private Service createService(final OWLIndividual service10)
	{
		try
		{
			final OWLIndividual translated = translation.get(service10);
			if (translated != null) return translated.castTo(Service.class);

			final Service service11 = translate(service10, Service.class, OWLS_1_1.Service.Service);

			final OWLIndividual profile10 = inputOnt.getProperty(service10, OWLS_1_0.Service.presents);
			final OWLIndividual process10 = inputOnt.getProperty(service10, OWLS_1_0.Service.describedBy);
			final OWLIndividual grounding10 = inputOnt.getProperty(service10, OWLS_1_0.Service.supports);

			final Process process11 = createProcessModel(process10);
			final Profile profile11 = createProfile(profile10);
			final Grounding grounding11 = createGrounding(grounding10);

			service11.setProcess(process11);
			service11.addProfile(profile11);
			service11.addGrounding(grounding11);
			translateAll(service10);

			return service11;
		}
		catch (final RuntimeException e)
		{
			logger.error("Invalid service description");
			return null;
		}
	}

	private Process createProcessModel(final OWLIndividual processModelInfo)
	{
		final OWLIndividual processInfo = inputOnt.getProperty(processModelInfo, OWLS_1_0.Process.hasProcess);
		return createProcess(processInfo);
	}

	private Process createProcess(final OWLIndividual processInfo)
	{
		try
		{
			final OWLIndividual translated = translation.get(processInfo);
			if (translated != null) return translated.castTo(Process.class);

			Process process = null;
			if (inputOnt.isType(processInfo, OWLS_1_0.Process.AtomicProcess))
				process = createAtomicProcess(processInfo);
			else if (inputOnt.isType(processInfo, OWLS_1_0.Process.CompositeProcess))
				process = createCompositeProcess(processInfo);
			else if (inputOnt.isType(processInfo, OWLS_1_0.Process.SimpleProcess))
				process = createSimpleProcess(processInfo);
			else
			{
				logger.error("Unknown process type {}", processInfo);
				return null;
			}

			copyPropertyValues(processInfo, OWLS_1_0.Process.name, process, OWLS_1_1.Process.name);
			createProcessParams(process, true, processInfo);
			createProcessParams(process, false, processInfo);
			createCondition(process, inputOnt.getProperties(processInfo, OWLS_1_0.Profile.hasPrecondition));
			createDataFlow(process, processInfo);
			return process;
		}
		catch (final RuntimeException e)
		{
			logger.error("Invalid process description");
			return null;
		}
	}

	private AtomicProcess createAtomicProcess(final OWLIndividual processInfo)
	{
		final AtomicProcess process =	translate(processInfo, AtomicProcess.class, OWLS_1_1.Process.AtomicProcess);
		return process;
	}

	private SimpleProcess createSimpleProcess(final OWLIndividual processInfo) {
		final SimpleProcess process =	translate(processInfo, SimpleProcess.class, OWLS_1_1.Process.SimpleProcess);
		// TODO translate SimpleProcess properties
		return process;
	}

	private CompositeProcess createCompositeProcess(final OWLIndividual processInfo) {
		final CompositeProcess process = translate(processInfo, CompositeProcess.class, OWLS_1_1.Process.CompositeProcess);
		final OWLIndividual composedInfo = inputOnt.getProperty(processInfo, OWLS_1_0.Process.composedOf);

		if (composedInfo == null)
			logger.error("Cannot find the components for composite process (process: {})", processInfo);
		else
		{
			final ControlConstruct controlConstruct = createControlConstruct(composedInfo);
			process.setComposedOf(controlConstruct);
		}

		return process;
	}

	private boolean isProcess(final OWLIndividual processInfo) {
		return inputOnt.isType(processInfo, OWLS_1_0.Process.AtomicProcess) ||
		inputOnt.isType(processInfo, OWLS_1_0.Process.CompositeProcess) ||
		inputOnt.isType(processInfo, OWLS_1_0.Process.SimpleProcess);
	}

	private ControlConstruct createControlConstruct(final OWLIndividual ccInfo) {
		final OWLClass ccType = ccInfo.getType();

		ControlConstruct cc = null;
		if (isProcess(ccInfo)) cc = createPerform(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.Sequence)) cc = createSequence(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.Split)) cc = createSplit(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.SplitJoin)) cc = createSplitJoin(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.Unordered)) cc = createAnyOrder(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.IfThenElse)) cc = createIfThenElse(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.Choice)) cc = createChoice(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.Iterate)) cc = createIterate(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.RepeatUntil)) cc = createRepeatUntil(ccInfo);
		else if (inputOnt.isType(ccInfo, OWLS_1_0.Process.RepeatWhile)) cc = createRepeatWhile(ccInfo);
		else logger.error("Don't know how to translate the control construct {}", ccType);

		return cc;
	}

	private Perform createPerform(final OWLIndividual processInfo) {
		final Perform perform = outputOnt.createPerform(null);
		final Process process = createProcess(processInfo);
		perform.setProcess(process);
		return perform;
	}

	private Sequence createSequence(final OWLIndividual sequenceInfo) {
		final Sequence sequence = translate(sequenceInfo, Sequence.class, OWLS_1_1.Process.Sequence);
		createComponents(sequence, sequenceInfo);
		return sequence;
	}

	private IfThenElse createIfThenElse(final OWLIndividual ifThenElseInfo) {
		final IfThenElse ifThenElse = translate(ifThenElseInfo, IfThenElse.class, OWLS_1_1.Process.IfThenElse);

		if (inputOnt.contains(ifThenElseInfo, OWLS_1_0.Process.ifCondition, null))
			createCondition(ifThenElse, inputOnt.getProperties(ifThenElseInfo, OWLS_1_0.Process.ifCondition));
		else logger.error(
			"If-Then-Else does not have a process:ifCondition associated with it (ifThenElse: {})", ifThenElseInfo);

		if (inputOnt.contains(ifThenElseInfo, OWLS_1_0.Process.thenP, null)) {
			final OWLIndividual thenComponentInfo = inputOnt.getProperty(ifThenElseInfo, OWLS_1_0.Process.thenP);
			final ControlConstruct thenComponent = createControlConstruct(thenComponentInfo);
			if (thenComponent != null) ifThenElse.setThen(thenComponent);
			else logger.error(
				"If-Then-Else has an invalid process:then associated with it (ifThenElse: {})", ifThenElseInfo);
		}
		else logger.error(
			"If-Then-Else does not have a process:then associated with it (ifThenElse: {}", ifThenElseInfo);

		if (inputOnt.contains(ifThenElseInfo, OWLS_1_0.Process.elseP, null)) {
			final OWLIndividual elseComponentInfo = inputOnt.getProperty(ifThenElseInfo, OWLS_1_0.Process.elseP);
			final ControlConstruct elseComponent = createControlConstruct(elseComponentInfo);
			if (elseComponent != null) ifThenElse.setThen(elseComponent);
			else logger.error(
				"If-Then-Else has an invalid process:else associated with it (ifThenElse: {}", ifThenElseInfo);
		}

		return ifThenElse;
	}

	private Split createSplit(final OWLIndividual splitInfo) {
		final Split split = translate(splitInfo, Split.class, OWLS_1_1.Process.Split);
		createComponents(split, splitInfo);
		return split;
	}

	private SplitJoin createSplitJoin(final OWLIndividual splitInfo) {
		final SplitJoin split = translate(splitInfo, SplitJoin.class, OWLS_1_1.Process.SplitJoin);
		createComponents(split, splitInfo);
		return split;
	}

	private AnyOrder createAnyOrder(final OWLIndividual anyOrderInfo) {
		final AnyOrder unordered = translate(anyOrderInfo, AnyOrder.class, OWLS_1_1.Process.AnyOrder);
		createComponents(unordered, anyOrderInfo);
		return unordered;
	}

	private Iterate createIterate(final OWLIndividual iterateInfo) {
		final Iterate iterate = translate(iterateInfo, Iterate.class, OWLS_1_1.Process.Iterate);

//		createComponents(iterate, iterateInfo);
//		if(iterate.getComponents().size() > 1)
//		error("Iterate should have only one component " + iterateInfo);

		return iterate;
	}

	private RepeatUntil createRepeatUntil(final OWLIndividual repeatInfo) {
		final RepeatUntil repeat = translate(repeatInfo, RepeatUntil.class, OWLS_1_1.Process.RepeatUntil);

		if(inputOnt.contains(repeatInfo, OWLS_1_0.Process.untilCondition, null))
			createCondition(repeat, inputOnt.getProperties(repeatInfo, OWLS_1_0.Process.untilCondition));
		else logger.error(
			"RepeatUntil does not have a process:untilCondition associated with it (RepeatUntil: {})",	repeat);

		if (inputOnt.contains(repeatInfo, OWLS_1_0.Process.components, null))
			logger.error("RepeatUntil cannot have a process:components property. Use process:untilProcess instead!");

		if (inputOnt.contains(repeatInfo, OWLS_1_0.Process.untilProcess, null)) {
			final OWLIndividual untilComponentInfo = inputOnt.getProperty(repeatInfo, OWLS_1_0.Process.untilProcess);
			final ControlConstruct untilComponent = createControlConstruct(untilComponentInfo);
			if (untilComponent != null) repeat.setComponent(untilComponent);
			else logger.error(
				"RepeatUntil has an invalid process:untilProcess associated with it (RepeatUntil: {})", repeatInfo);
		}
		else logger.error(
			"RepeatUntil does not have a process:untilProcess associated with it (RepeatUntil: {})",	repeat);

		return repeat;
	}

	private RepeatWhile createRepeatWhile(final OWLIndividual repeatInfo) {
		final RepeatWhile repeat = translate(repeatInfo, RepeatWhile.class, OWLS_1_1.Process.RepeatWhile);

		if(inputOnt.contains(repeatInfo, OWLS_1_0.Process.whileCondition, null))
			createCondition(repeat, inputOnt.getProperties(repeatInfo, OWLS_1_0.Process.whileCondition));
		else logger.error(
			"RepeatUntil does not have a process:ifCondition associated with it (RepeatWhile: {})", repeat);

		if (inputOnt.contains(repeatInfo, OWLS_1_0.Process.components, null))
			logger.error("RepeatWhile cannot have a process:components property. Use process:untilProcess instead!");

		if (inputOnt.contains(repeatInfo, OWLS_1_0.Process.whileProcess, null)) {
			final OWLIndividual whileComponentInfo = inputOnt.getProperty(repeatInfo, OWLS_1_0.Process.whileProcess);
			final ControlConstruct whileComponent = createControlConstruct(whileComponentInfo);
			if (whileComponent != null) repeat.setComponent(whileComponent);
			else logger.error(
				"RepeatWhile has an invalid process:whileProcess associated with it (RepeatWhile: {})", repeatInfo);
		}
		else logger.error(
			"RepeatWhile does not have a process:whileProcess associated with it (RepeatWhile: {})", repeat);

		return repeat;
	}

	private Choice createChoice(final OWLIndividual choiceInfo) {
		final Choice choice = translate(choiceInfo, Choice.class, OWLS_1_1.Process.Choice);
		createComponents(choice, choiceInfo);
		return choice;
	}

	private void createComponents(final ControlConstruct cc, final OWLIndividual ccInfo) {
		final OWLIndividual componentsInfo = inputOnt.getProperty(ccInfo, OWLS_1_0.Process.components);
		if (componentsInfo == null)
		{
			logger.error("{} construct does not have any components associated with it (Construct: {})",
				cc.getConstructName(), ccInfo);
			return;
		}

		final OWLList<OWLIndividual> list = componentsInfo.castToList(OWLS.ObjectList.List);
		for (OWLIndividual componentInfo : list)
		{
			final ControlConstruct component = createControlConstruct(componentInfo);

			if (component == null) logger.warn("There is an invalid component description for {}", cc);
			else if(cc instanceof Sequence) ((Sequence)cc).addComponent(component);
			else if(cc instanceof AnyOrder) ((AnyOrder)cc).addComponent(component);
			else if(cc instanceof Choice) ((Choice)cc).addComponent(component);
			else if(cc instanceof Split) ((Split)cc).addComponent(component);
			else if(cc instanceof SplitJoin) ((SplitJoin)cc).addComponent(component);
			else throw new RuntimeException("Invalid control construct!");
		}
	}

	private void createDataFlow(final Process process, final OWLIndividual processComponentInfo) {
		try {
			final OWLIndividualList<?> list = inputOnt.getProperties(processComponentInfo, OWLS_1_0.Process.sameValues);
			for (OWLIndividual ind : list)
			{
				final OWLList<OWLIndividual> sameValuesList = ind.castToList(OWLS.ObjectList.List);
				final OWLIndividual value1 = sameValuesList.get(0);
				final OWLIndividual value2 = sameValuesList.get(1);

				final OWLIndividual p1 = inputOnt.getProperty(value1, OWLS_1_0.Process.theParameter);
				final OWLIndividual p2 = inputOnt.getProperty(value2, OWLS_1_0.Process.theParameter);

				final Parameter param1 = (Parameter) translation.get(p1);
				final Parameter param2 = (Parameter) translation.get(p2);

				final OWLIndividual process1 = inputOnt.getProperty(value1, OWLS_1_0.Process.atProcess);
				final OWLIndividual process2 = inputOnt.getProperty(value2, OWLS_1_0.Process.atProcess);

				if (process1.equals(process) && process2.equals(process)) {
					// FIXME handle this case using ThisPerform
					logger.error("Same process used twice in the ValueOf!");
				}
				else if (process1.equals(process)) {
					final Perform perform = getCachedPerform(process, process2);
					if (param1 instanceof Input) {
						// add binding to the perform
						perform.addBinding((Input) param1, OWLS_1_1.Process.TheParentPerform, (Input) param2);
					}
					else {
						// add result to the process
						Result result = process.getResult();
						if (result == null) {
							result = outputOnt.createResult(null);
							process.addResult(result);
						}

						result.addBinding((Output) param1, perform, (Output) param2);
					}
				}
				else if (process2.equals(process)) {
					final Perform perform = getCachedPerform(process, process1);
					if (param2 instanceof Input) {
						// add binding to the perform
						perform.addBinding((Input) param2, OWLS_1_1.Process.TheParentPerform, (Input) param1);
					}
					else {
						// add result to the process
						Result result = process.getResult();
						if (result == null) {
							result = outputOnt.createResult(null);
							process.addResult(result);
						}

						result.addBinding((Output) param2, perform, (Output) param1);
					}
				}
				else {
					final Perform perform1 = getCachedPerform(process, process1);
					final Perform perform2 = getCachedPerform(process, process2);
					if(param1 instanceof Input) {
						// add binding to the perform1
						perform1.addBinding((Input) param1, perform2, (Output) param2);
					}
					else {
						// add binding to the perform1
						perform2.addBinding((Input) param2, perform1, (Output) param1);
					}
				}
			}
		} catch(final Exception e) {
			logger.error("Invalid data flow specification. Details: ", e.toString());
		}
	}

	private Parameter createParam(final OWLIndividual paramInfo) {
		final OWLIndividual translated = translation.get(paramInfo);
		if (translated != null) return translated.castTo(Parameter.class);

		Parameter param = null;
		if (inputOnt.isType(paramInfo, OWLS_1_0.Process.Input))
			param = translate(paramInfo, Input.class, OWLS_1_1.Process.Input);
		else if (inputOnt.isType(paramInfo, OWLS_1_0.Process.Output))
			param = translate(paramInfo, Output.class, OWLS_1_1.Process.Output);
		else {
			logger.error("Unknown parameter type {}", paramInfo);
			return null;
		}

		if( inputOnt.contains(paramInfo, OWLS_1_0.Process.parameterType, null)) {
			final URI typeURI = inputOnt.getProperty(paramInfo, OWLS_1_0.Process.parameterType).getURI();
			if (typeURI != null) {
				OWLType type = inputOnt.getType(typeURI);
				if (type == null) type = outputOnt.createClass(typeURI);

				param.setParamType(type);
			}
			else {
				// FIXME default values
			}
		}
		else logger.error("Cannot find the type for the process parameter (parameter: {})", param);
		/*******************************************
		 ** added by guang huang @2005-4-8 **
		 *Keep <fla:useRandomInput>true</fla:useRandomInput> in
		 *process:Input or process:Onput
		 ********************************************/
		keepFLAProperties(paramInfo);
		if (inputOnt.isType(paramInfo, OWLS_1_0.Process.Input)) {
			try {
				if ((!profileInputURIs.contains(param.getURI())) && (paramInfo.getProperties().size() <= 1))
					param.addProperty(FLAServiceOnt.useRandomInput, "true");
			} catch (final Exception e) {
				// TODO: handle exception
			}
		}

		/*******************************************
		 ** end by guang huang **
		 ********************************************/
		return param;
	}

	private void createProcessParams(final Process process, final boolean isInput, final OWLIndividual processInfo) {
		final OWLObjectProperty prop = isInput ? OWLS_1_0.Process.hasInput : OWLS_1_0.Process.hasOutput;
		final OWLIndividualList<?> list = inputOnt.getProperties(processInfo, prop);
		for (OWLIndividual p : list)
		{
			Parameter param = null;
			if (isInput) {
				param = createParam(p);
				process.addInput((Input) param);
			}
			else {
				param = createParam(p);
				process.addOutput((Output) param);
			}

			copyPropertyValues(p, RDFS.label, param, RDFS.label);

			logger.debug("Process {} {} {} Type {}", new Object[]{
				process.getURI(), isInput? "Input" : "Output", param.getURI(), param.getParamType()});
		}
	}

	private Profile createProfile(final OWLIndividual profileInfo) {
		try {
			final OWLIndividual translated = translation.get(profileInfo);
			if (translated != null) return translated.castTo(Profile.class);

			final Profile profile = translate(profileInfo, Profile.class, OWLS_1_1.Profile.Profile);

			copyPropertyValues(profileInfo, OWLS_1_0.Profile.serviceName, profile, OWLS_1_1.Profile.serviceName);
			copyPropertyValues(profileInfo, RDFS.label, profile, RDFS.label);
			copyPropertyValues(profileInfo, OWLS_1_0.Profile.textDescription, profile,
				OWLS_1_1.Profile.textDescription);

			createProfileParams(profile, true, profileInfo);
			createProfileParams(profile, false, profileInfo);

			createCondition(profile, inputOnt.getProperties(profileInfo, OWLS_1_0.Profile.hasPrecondition));

			createServiceParameters(profile, profileInfo);
			translateAll(profileInfo);
			/*******************************************
			 ** added by guang huang @2005-4-8 **
			 *Keep FLAServiceParameter
			 ********************************************/
			keepFLAProperties(profileInfo);
			/*******************************************
			 ** end by guang huang **
			 ********************************************/
			return profile;
		} catch (final RuntimeException e) {
			logger.error("Invalid profile description");
			return null;
		}
	}

	private void createServiceParameters(final Profile profile, final OWLIndividual profileInfo) {
		final Set<OWLProperty> set = inputOnt.getKB().getSubProperties(OWLS_1_0.Profile.serviceParameter);
		for(final Iterator<OWLProperty> it = set.iterator(); it.hasNext();) {
			final OWLObjectProperty prop = (OWLObjectProperty) it.next();

			final OWLIndividualList<?> list = profileInfo.getProperties(prop);
			for (OWLIndividual serviceParamInfo : list)
			{
				final OWLIndividual serviceParam = translate(serviceParamInfo, serviceParamInfo.getType());

				OWLIndividual serviceParamValue = serviceParamInfo.getProperty(OWLS_1_0.Profile.sParameter);
				serviceParamValue = translateAll(serviceParamValue);

				profile.addProperty(prop, serviceParam);
				serviceParam.addProperty(OWLS_1_1.Profile.sParameter, serviceParamValue);
				copyPropertyValues(serviceParamInfo, OWLS_1_0.Profile.serviceParameterName, serviceParam,
					OWLS_1_1.Profile.serviceParameterName);
			}
		}
	}

	private OWLIndividual translateAll(final OWLIndividual untranslated)
	{
		OWLIndividual translated = translation.get(untranslated);
		if (translated == null)
		{
			if (isProcess(untranslated)) return createProcess(untranslated);

			translated = translate(untranslated, untranslated.getType());
			final Map<OWLProperty, List<OWLValue>> map = inputOnt.getKB().getProperties(untranslated);
			for (OWLProperty prop : map.keySet())
			{
				copyPropertyValues(untranslated, RDFS.label, translated, RDFS.label);
				if (prop instanceof OWLDataProperty)
					copyPropertyValues(untranslated, (OWLDataProperty) prop, translated, (OWLDataProperty) prop);
				else {
					final OWLObjectProperty objProp = (OWLObjectProperty) prop;
					final OWLIndividualList<?> list = inputOnt.getProperties(untranslated, objProp);
					for (OWLIndividual value : list)
					{
						value = translate(value, value.getType());
						translated.addProperty(objProp, value);
					}
				}
			}
		}
		return translated;
	}

	private void createProfileParams(final Profile profile, final boolean isInput, final OWLIndividual profileInfo) {
		final OWLObjectProperty prop = isInput ? OWLS_1_0.Profile.hasInput : OWLS_1_0.Profile.hasOutput;

		final OWLIndividualList<?> list = inputOnt.getProperties(profileInfo, prop);
		for (OWLIndividual p : list)
		{
			final Parameter refersTo = (Parameter) translation.get(p);

			if (refersTo == null) logger.error(
				"The parameter defined in profile does not exist in the process model (parameter: {}, in profile: {})",
				p, profile.getURI());
			else
			{
				if (isInput)
				{
					profile.addInput((Input) refersTo);
					if (refersTo.getURI() != null) profileInputURIs.add(refersTo.getURI());
				}
				else profile.addOutput((Output) refersTo);

				logger.debug("Profile {} {} {} refers to {}", new Object[]{
					profile.getURI(), isInput? "Input" : "Output", p.getURI(), refersTo});
			}
		}
	}


	private void createCondition(final Conditional conditional, final OWLIndividualList<?> list) {
		if (list.size() == 0) return;

		final OWLIndividual conditionInfo = list.get(0);
		final Condition condition = translate(conditionInfo, Condition.class,
			OWLS_1_1.Expression.Condition);
		conditional.setCondition(condition);

		if (list.size() > 1) logger.error("Multiple conditions defined for {}", conditional);
	}

	private void createCondition(final MultiConditional conditional, final OWLIndividualList<?> list)
	{
		for (OWLIndividual conditionInfo : list)
		{
			final Condition condition = translate(conditionInfo, Condition.class,
				OWLS_1_1.Expression.Condition);
			conditional.addCondition(condition);
		}
	}

	private Grounding createGrounding(final OWLIndividual groundingInfo) {
		final OWLIndividual translated = translation.get(groundingInfo);
		if (translated != null) return translated.castTo(Grounding.class);

		final Grounding grounding = translate(groundingInfo, Grounding.class, OWLS_1_1.Grounding.WsdlGrounding);

		OWLIndividualList<?> list = inputOnt.getProperties(groundingInfo, OWLS_1_0.Grounding.hasAtomicProcessGrounding);
		for (OWLIndividual apGroundingInfo : list)
		{
			final AtomicGrounding<String> apGrounding = createAPGrounding(apGroundingInfo);
			if (apGrounding != null) grounding.addGrounding(apGrounding);
			else logger.error("Invalid AtomicProcess grounding {}", apGroundingInfo);
		}

		if (grounding.getAtomicGroundings().size() == 0)
			logger.warn("The grounding of the service is empty (grounding {})", grounding.getURI());

		return grounding;
	}

	private AtomicGrounding createAPGrounding(final OWLIndividual groundingInfo) {
		try {
			final OWLIndividual translated = translation.get(groundingInfo);
			if (translated != null) return translated.castTo(AtomicGrounding.class);

			AtomicGrounding grounding = null;

			if (inputOnt.isType(groundingInfo, OWLS_1_0.Grounding.WsdlAtomicProcessGrounding))
				grounding = createWSDLGrounding(groundingInfo);
			else if (inputOnt.isType(groundingInfo, FLAServiceOnt.UPnPAtomicProcessGrounding))
				grounding = createUPnPGrounding(groundingInfo);

			return grounding;
		} catch (final RuntimeException e) {
			logger.error("Invalid profile description. Details: {}", e.toString());
			return null;
		}
	}

	private URI getGroundingURI(final OWLIndividual groundingInfo, final OWLDataProperty p) {
		final OWLDataValue value = inputOnt.getProperty(groundingInfo, p);
		if (value == null)
		{
			logger.error("{} does not have a grounding: {} property", groundingInfo, p.getURI().getFragment());
			return null;
		}

		try
		{
			return new URI(value.getLexicalValue().trim());
		}
		catch(final Exception e)
		{
			logger.error("The value of grounding: {} (property {}) in {} is not a valid URI literal",
				new Object[]{p.getURI().getFragment(), value.getLexicalValue(), groundingInfo});
			return null;
		}
	}

	private AtomicProcess getGroundingProcess(final OWLIndividual groundingInfo) {
		final OWLIndividual processInfo = inputOnt.getProperty(groundingInfo, OWLS_1_0.Grounding.owlsProcess);

		if (!inputOnt.contains(groundingInfo, OWLS_1_0.Grounding.owlsProcess, null))
		{
			logger.error("{} does not have a grounding:owlsProcess property", groundingInfo);
			return null;
		}

		final AtomicProcess process = (AtomicProcess) createProcess(processInfo);

		if (process == null)
		{
			logger.error("The process specified in the grounding cannot be found (grounding: {}, process: {})",
				groundingInfo, processInfo);
			return null;
		}

		return process;
	}

	private WSDLAtomicGrounding createWSDLGrounding(final OWLIndividual groundingInfo) {
		final AtomicProcess process = getGroundingProcess(groundingInfo);
		final URI wsdlLoc = getGroundingURI(groundingInfo, OWLS_1_0.Grounding.wsdlDocument);
		final OWLIndividual operationInfo = inputOnt.getProperty(groundingInfo, OWLS_1_0.Grounding.wsdlOperation);
		final URI opName = getGroundingURI(operationInfo, OWLS_1_0.Grounding.operation);
		final URI portType = getGroundingURI(operationInfo, OWLS_1_0.Grounding.portType);

		final WSDLAtomicGrounding g = translate(groundingInfo, WSDLAtomicGrounding.class,
			OWLS_1_1.Grounding.WsdlAtomicProcessGrounding);
		g.setWSDL(wsdlLoc);
		g.setOperation(opName);
		g.setPortType(portType);

		if (inputOnt.contains(groundingInfo, OWLS_1_0.Grounding.wsdlInputMessage, null))
			g.setInputMessage(getGroundingURI(groundingInfo, OWLS_1_0.Grounding.wsdlInputMessage));

		if (process != null)
		{
			g.setProcess(process);
			createMessageMapList(g, groundingInfo, true);
			createMessageMapList(g, groundingInfo, false);
			logger.debug("Process {}, WSDL file {}, operation {}", new Object[]{process.getURI(), wsdlLoc, opName});
		}
		else logger.error("No OWL-S process defined for AtomicProcessGrounding {}", groundingInfo);


		return g;
	}

	private UPnPAtomicGrounding createUPnPGrounding( final OWLIndividual groundingInfo) {
		final OWLIndividual processInfo = inputOnt.getProperty(groundingInfo, OWLS_1_0.Grounding.owlsProcess);
		final String upnpDevice = inputOnt.getProperty(groundingInfo, FLAServiceOnt.upnpDeviceURL).toString();
		final String upnpService = inputOnt.getProperty(groundingInfo, FLAServiceOnt.upnpServiceID).toString();
		final String upnpAction = inputOnt.getProperty(groundingInfo, FLAServiceOnt.upnpCommand).toString();

		final AtomicProcess process = (AtomicProcess) createProcess(processInfo);

		final UPnPAtomicGrounding g = translate(groundingInfo, UPnPAtomicGrounding.class, FLAServiceOnt.UPnPAtomicProcessGrounding);
		g.setProcess(process);
		g.setUPnPDescription(upnpDevice);
		g.setUPnPService(upnpService);
		g.setUPnPAction(upnpAction);

		createMessageMapList(g, groundingInfo, true);
		createMessageMapList(g, groundingInfo, false);

		logger.debug("Process {} Device {} Service {} Action {}",
			new Object[]{process.getURI(), upnpDevice, upnpService, upnpAction});

		return g;
	}

	private void createMessageMapList(final AtomicGrounding g, final OWLIndividual groundingInfo,
		final boolean isInput)
	{
		final Process process = g.getProcess();
		OWLObjectProperty messageParts = null;
		OWLObjectProperty olderMessageParts = null;
		OWLDataProperty messagePart = null;

		if (g instanceof UPnPAtomicGrounding)
		{
			messageParts = isInput? FLAServiceOnt.upnpInputMapping: FLAServiceOnt.upnpOutputMapping;
			messagePart = FLAServiceOnt.upnpParameter;
		}
		else if (g instanceof WSDLAtomicGrounding)
		{
			messageParts = isInput? OWLS_1_0.Grounding.wsdlInputs: OWLS_1_0.Grounding.wsdlOutputs;
			olderMessageParts = isInput? OWLS_1_0.Grounding.wsdlInputMessageParts:
				OWLS_1_0.Grounding.wsdlOutputMessageParts;
			messagePart = OWLS_1_0.Grounding.wsdlMessagePart;
		}

		OWLIndividual messageMaps = inputOnt.getProperty(groundingInfo, messageParts);
		// try older property name
		if (messageMaps == null && olderMessageParts != null) {
			messageMaps = inputOnt.getProperty(groundingInfo, olderMessageParts);
			if (messageMaps != null)
				logger.warn("Using deprecated property {} instead of {}", olderMessageParts, messageParts);
		}

		if (messageMaps == null) {
			final OWLIndividualList<?> params = isInput ? process.getInputs() : process.getOutputs();
			if (!params.isEmpty())
				logger.warn("No mapping defined for parameters (parameters: {}, process: {})", params, process);
			return;
		}
		/*******************************************
		 ** modified by guang huang @2005-4-14**
		 ********************************************/
		final OWLList<OWLIndividual> messageMapList = messageMaps.castToList(OWLS.ObjectList.List);
		/*******************************************
		 ** end by guang huang **
		 ********************************************/
		final Iterator<OWLIndividual> it = messageMapList.iterator();
		while(it.hasNext()) {
			final OWLIndividual messageMap = it.next();

			final URI owlsParameterInfo = inputOnt.getProperty(messageMap, OWLS_1_0.Grounding.owlsParameter).getURI();
			final Parameter owlsParameter = isInput?
				process.getInputs().getIndividual(owlsParameterInfo) :
					process.getOutputs().getIndividual(owlsParameterInfo);


			final String wsdlMessagePartInfo = inputOnt.getProperty(messageMap, messagePart).toString().trim();

			String transformation = null;
			if (inputOnt.contains(messageMap, OWLS_1_0.Grounding.xsltTransformation, null))
				transformation = inputOnt.getProperty(messageMap, OWLS_1_0.Grounding.xsltTransformation).toString();

			if (owlsParameter == null) logger.error(
				"Cannot find the target of message map for {} parameter (WSDL parameter: {}, in process: {}, mapped to: {})",
				new Object[]{isInput? "input" : "output", wsdlMessagePartInfo, process.getURI(), owlsParameterInfo});
			else g.addMessageMap(owlsParameter, wsdlMessagePartInfo, transformation);

			logger.debug("Process {}, Param {}, Grounding {}, Transform {}",
				new Object[]{process.getURI(), owlsParameterInfo, transformation});
		}
	}

	private void copyPropertyValues(final OWLIndividual src, final OWLDataProperty srcProp,
		final OWLIndividual target, final OWLDataProperty targetProp)
	{
		final List<OWLDataValue> list = inputOnt.getProperties(src, srcProp);
		for (OWLDataValue dataValue : list)
		{
			target.addProperty(targetProp, dataValue);
		}
	}

	private void copyPropertyValues(final OWLIndividual src, final URI srcProp, final OWLIndividual target,
		final URI targetProp)
	{
		final List<OWLDataValue> list = src.getPropertiesAsDataValue(srcProp);
		for (OWLDataValue dataValue : list)
		{
			target.addProperty(targetProp, dataValue);
		}
	}

	/*******************************************
	 ** added by guang huang @2005-4-8 **
	 ********************************************/
	private OWLIndividual keepFLAProperties(final OWLIndividual ind) {
		final OWLIndividual translated = translateAll(ind);
		if (translated != null)
		{
			final Map<OWLProperty, List<OWLValue>> map = ind.getProperties();
			for (OWLProperty prop : map.keySet())
			{
				if (prop instanceof OWLDataProperty)
				{
					if (FLAServiceOnt.flaDataProperties.contains(prop))
						copyPropertyValues(ind, (OWLDataProperty) prop, translated, (OWLDataProperty) prop);
				}
				else
				{
					final OWLObjectProperty objProp = (OWLObjectProperty) prop;
					if (FLAServiceOnt.strictType.getURI().equals(objProp.getURI()))
					{
						final OWLIndividualList<?> list = inputOnt.getProperties(ind, objProp);
						for (OWLIndividual serviceParamInfo : list)
						{
							translated.addProperty(FLAServiceOnt.strictType, serviceParamInfo.getURI());
						}
					}
					else if (FLAServiceOnt.flaObjectProperties.containsKey(objProp))
					{
						final OWLIndividualList<?> list = inputOnt.getProperties(ind, objProp);
						for (OWLIndividual serviceParamInfo : list)
						{
							final OWLIndividual serviceParam = translate(serviceParamInfo, serviceParamInfo.getType());

							OWLIndividual serviceParamValue = serviceParamInfo.getProperty(OWLS_1_0.Profile.sParameter);
							serviceParamValue = translate(serviceParamValue, FLAServiceOnt.flaObjectProperties.get(objProp));

							translated.addProperty(objProp, serviceParam);
							serviceParam.addProperty(OWLS_1_1.Profile.sParameter, serviceParamValue);
						}
					}
				}
			}
		}
		return translated;
	}
	/*******************************************
	 ** end by guang huang **
	 ********************************************/
}
