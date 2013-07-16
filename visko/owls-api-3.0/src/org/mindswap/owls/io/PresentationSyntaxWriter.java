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
 * Created on Jun 1, 2004
 */
package org.mindswap.owls.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mindswap.exceptions.InvalidURIException;
import org.mindswap.exceptions.NotImplementedException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLEntity;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AsProcess;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ControlConstructVisitor;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ParameterValue;
import org.mindswap.owls.process.variable.ParameterValueVisitor;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ResultVar;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.owls.process.variable.ValueForm;
import org.mindswap.owls.process.variable.ValueFunction;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.QNameProvider;

/**
 * This is a experimental version of OWL-S presentation syntax writer.
 *
 * @author unascribed
 * @version $Rev: 2323 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public class PresentationSyntaxWriter implements ProcessWriter {
	private static final String INDENT = "   ";
	private String indent = "";
	private QNameProvider qnames;
	PrintWriter out;
	private Writer writer;
	private SwrlExpressionWriter swrlExprWriter; // lazy initialization
	private SparqlExpressionWriter sparqlExprWriter; // lazy initialization
	private String defaultNS;

	public PresentationSyntaxWriter()
	{
		qnames = new QNameProvider();
	}

	public String getDefaultNS() {
		return defaultNS;
	}

	public void setDefaultNS( final String defaultNS )
	{
		if (defaultNS.endsWith("#")) this.defaultNS = defaultNS;
		else this.defaultNS = defaultNS + "#";
	}

	/* @see org.mindswap.owls.io.ProcessWriter#getSparqlExpressionWriter() */
	public ExpressionWriter<String> getSparqlExpressionWriter()
	{
		if (sparqlExprWriter == null)
		{
			sparqlExprWriter = new SparqlExpressionWriter();
		}
		return sparqlExprWriter;
	}

	/* @see org.mindswap.owls.io.ProcessWriter#getSwrlExpressionWriter() */
	public SwrlExpressionWriter getSwrlExpressionWriter()
	{
		if (swrlExprWriter == null)
		{
			swrlExprWriter = new SwrlExpressionWriter();
		}
		return swrlExprWriter;
	}

	String qname( final OWLEntity entity ) {
		if( entity.isAnon() ) {
			return "<< Anonymous " + (entity instanceof OWLClass? "Class" : "Individual") + " >>";
		}
		return qnames.shortForm( entity.getURI() );
	}

	String qname( final URI uri ) {
		try {
			return qnames.shortForm( uri );
		}
		catch( final Exception e ) {
			return "<< Invalid URI >>";
		}
	}

	private void indent( final boolean left ) {
		if( left ) indent += INDENT;
		else indent = indent.substring( INDENT.length() );
	}

	private void printIndented() {
		out.print( indent );
	}

	private void printIndented( final String str ) {
		printIndented();
		out.print( str );
	}

	private void printlnIndented( final String str ) {
		printIndented();
		out.println( str );
	}

	private String repeat( final char c, final int n ) {
		final char[] chars = new char[n];
		Arrays.fill( chars, c );
		return String.copyValueOf( chars );
	}

	void block(final boolean start)
	{
		if (start)
		{
			printlnIndented("{");
			indent(true);
		}
		else
		{
			indent(false);
			printIndented("}");
		}
	}

	public void setWriter(final Writer writer) {
		this.writer = writer;
	}

	public void setWriter(final OutputStream stream) {
		setWriter( new OutputStreamWriter( stream ) );
	}


	public void write(final Process process, final OutputStream stream) {
		setWriter( stream );
		write( process );
	}

	public void write(final Process process, final Writer outWriter) {
		setWriter( outWriter );
		write( process );
	}

	public void write(final Process process) {
		write(Collections.singleton( process ) );
	}

	public void write(final Collection<Process> processes) {
		if(defaultNS != null) qnames.setMapping( "", defaultNS );

		final StringWriter buffer = new StringWriter();
		out = new PrintWriter( buffer );
		swrlExprWriter.setWriter( out );
		swrlExprWriter.setIndent( "" );
		swrlExprWriter.setQNames( qnames );

		indent( true );
		for (final Process process : processes)
		{
			writeProcess(process);
		}
		indent( false );

		out.flush();

		out = new PrintWriter( writer );
		writeHeader();
		out.println( buffer.toString() );
		writeFooter();
		out.flush();
	}

	private void writeHeader() {
		out.println("with_namespaces");

		final Set<String> prefixSet = new HashSet<String>(qnames.getPrefixSet());
		final String defNS = qnames.getURI( "" );
		if( defNS != null ) {
			out.println("  (uri\"" + defNS + "\",");
			prefixSet.remove( "" );
		}

		for(final Iterator<String> i = prefixSet.iterator(); i.hasNext(); ) {
			final String prefix = i.next();
			final String uri = qnames.getURI( prefix );

			out.print("   " + prefix + ":  \"" + uri + "\"");
			if (i.hasNext()) out.println(",");
			else out.println(")");
		}
		out.println("{");
		out.println();
	}

	private void writeFooter() {
		out.println("}");
	}

	private boolean emptyProcess( final Process process ) {
		return process.getInputs().isEmpty() &&
		((process instanceof CompositeProcess)? ((CompositeProcess) process).getLocals().isEmpty() : true) &&
		process.getOutputs().isEmpty() &&
		process.getConditions().isEmpty() &&
		process.getResults().isEmpty();
	}

	private void writeProcess(final Process process) {
		if(process instanceof AtomicProcess) writeProcess((AtomicProcess) process);
		else if(process instanceof CompositeProcess) writeProcess((CompositeProcess) process);
		else throw new NotImplementedException("Only writing Atomic and Composite process implemented yet");

		out.println();
	}

	private void writeProcess(final CompositeProcess process) {
		printIndented("define composite process ");
		out.print(process.getLocalName() + "(");

		if (!emptyProcess(process)) {
			out.println();
			writeIO(process);
			writePreconditions(process);
			writeResults(process);
		}
		out.println(")");

		final ControlConstruct cc = process.getComposedOf();
		if (cc != null) writeConstruct( cc, true );

		out.println();
	}

	private void writeIO( final Process process ) {
		indent( true );

		final OWLIndividualList<Input> inputs = process.getInputs();
		if (inputs.size()>0)
		{
			printIndented("inputs: (" );
			for(int i = 0; i < inputs.size(); i++) {
				final Input input = inputs.get(i);
				final OWLType type = input.getParamType();
				out.print(qname(input));
				out.print(" - " + qname(type.getURI()));
				if (i<inputs.size()-1)
				{
					out.println();
					printIndented("         ");
				}
				else
					out.print(")");
			}
		}

		if (process instanceof CompositeProcess)
		{
			final OWLIndividualList<Local> locals = ((CompositeProcess) process).getLocals();
			if (locals.size() > 0)
			{
				out.println(",");
				printIndented("locals: (" );
				for (int i = 0; i < locals.size(); i++) {
					final Local local = locals.get(i);
					final OWLType type = local.getParamType();
					out.print(qname(local));
					out.print(" - " + qname(type.getURI()));
					if (i<locals.size()-1)
					{
						out.println();
						printIndented("         ");
					}
					else
						out.print(")");
				}
			}
		}

		final OWLIndividualList<Output> outputs = process.getOutputs();
		if (outputs.size()>0)
		{
			out.println(",");
			printIndented("outputs: (" );
			for(int i = 0; i < outputs.size(); i++) {
				final Output output = outputs.get(i);
				final OWLType type = output.getParamType();
				out.print(qname(output));
				out.print(" - " + qname(type.getURI()));
				if (i<outputs.size()-1)
				{
					out.println();
					printIndented("          ");
				}
				else
					out.print(")");
			}
		}

		indent( false );
	}

	void writePerform(final Perform perform)
	{
		final Process process = perform.getProcess();
		final String str = qname(perform) + " :: perform " + qname(process) + "(";
		printIndented( str );

		final OWLIndividualList<InputBinding> bindings = perform.getBindings();
		writeBindings( bindings, str.length() );

		out.print(")");
	}

	private void writeBindings( final OWLIndividualList<? extends Binding<?>> bindings, final int extraIndent ) {
		final String pad = repeat( ' ', extraIndent );
		for (int i = 0; i < bindings.size(); i++)
		{
			final Binding<?> binding = bindings.get(i);
			final ProcessVar param = binding.getProcessVar();
			final ParameterValue paramValue = binding.getValue();
			final StringBuilder mappedValue = new StringBuilder();
			paramValue.accept(new ParameterValueVisitor() {

				public void visit(final ValueOf valueOf)
				{
					final Perform otherPerform = valueOf.getPerform();
					final Parameter otherParam = valueOf.getParameter();
					if (!otherPerform.equals(OWLS.Process.ThisPerform))
						mappedValue.append(qname(otherPerform)).append(".");
					mappedValue.append(qname(otherParam));
				}

				public void visit(final ValueFunction<?> valueFunction)
				{
					mappedValue.append(valueFunction.getFunction());
				}

				public void visit(final ValueForm vf)
				{
					mappedValue.append("<< ValueForm no yet supported >>");
				}

				public void visit(final ValueConstant valueData)
				{
					if (valueData.isDataValue())
					{
						final OWLDataValue value = valueData.getData();
						final URI datatypeURI = value.getDatatypeURI();
						final boolean quote = datatypeURI == null || (
							!datatypeURI.equals(XSD.xsdBoolean) &&
							!datatypeURI.equals(XSD.integer) &&
							!datatypeURI.equals(XSD.xsdInt) &&
							!datatypeURI.equals(XSD.decimal) &&
							!datatypeURI.equals(XSD.xsdDouble));

						if (quote)
						{
							mappedValue.append("\"").append(value).append("\"");
							if (datatypeURI != null) mappedValue.append("^^").append(qname(datatypeURI));
						}
						else mappedValue.append(value);
					}
					else mappedValue.append(qname(valueData.getIndividual()));
				}
			});

			out.print(qname(param) + " <= " + mappedValue);
			if (i < bindings.size() - 1)
			{
				out.print(", ");
				if (bindings.size() > 1)
				{
					out.println();
					printIndented();
					out.print(pad);
				}
			}
		}
	}

//	private void writeConstruct(ControlConstruct cc) {
//	writeConstruct(cc, false);
//	}

	private void writeConstruct(final ControlConstruct cc, final boolean block)
	{
		cc.accept(new ControlConstructVisitor() {

			public void visit(final SplitJoin sj) { writeComponents(sj.getConstructs(), "||>"); }
			public void visit(final Split st) {	writeComponents(st.getConstructs(), "||<"); }

			public void visit(final org.mindswap.owls.process.Set set)
			{
				// TODO implement
				out.println("<< Not yet implemented " + cc.getConstructName() + " >>" );
			}

			public void visit(final Sequence sq) {	writeComponents(sq.getConstructs(), ";");	}

			public void visit(final RepeatWhile rw)
			{
				// TODO implement
				out.println("<< Not yet implemented " + cc.getConstructName() + " >>" );
			}

			public void visit(final RepeatUntil ru)
			{
				// TODO implement
				out.println("<< Not yet implemented " + cc.getConstructName() + " >>" );
			}

			public void visit(final Produce pr)
			{
				if (block) block(true);
				writeProduce(pr);
				if (block)
				{
					out.println();
					block(false);
				}
			}

			public void visit(final Perform pf)
			{
				if (block) block(true);
				writePerform(pf);
				if (block)
				{
					out.println();
					block(false);
				}
			}

			public void visit(final IfThenElse ite)
			{
				if (block) block(true);
				writeIfThenElse(ite);
				if (block)
				{
					out.println();
					block(false);
				}
			}

			public void visit(final ForEach fe)
			{
				// TODO implement
				out.println("<< Not yet implemented " + cc.getConstructName() + " >>" );
			}

			public void visit(final Choice ch) { writeComponents(ch.getConstructs(), ";?"); }

			public void visit(final AsProcess ap)
			{
				// TODO implement
				out.println("<< Not yet implemented " + cc.getConstructName() + " >>" );
			}

			public void visit(final AnyOrder ao) {	writeComponents(ao.getConstructs(), "||;"); }
		});
	}

	void writeComponents (final OWLIndividualList<? extends ControlConstruct> components, final String delim)
	{
		block(true);
		for (final Iterator<? extends ControlConstruct> i = components.iterator(); i.hasNext();)
		{
			final ControlConstruct cc = i.next();
			writeConstruct(cc, false);

			if (i.hasNext()) out.println(delim);
			else out.println();
		}
		block(false);
	}

	void writeIfThenElse(final IfThenElse ifThenElse)
	{
		final Condition<?> condition = ifThenElse.getCondition();
		final ControlConstruct thenP = ifThenElse.getThen();
		final ControlConstruct elseP = ifThenElse.getElse();

		printIndented("if( ");
		condition.writeTo(this, indent + "    ");
		out.println( " )" );

		printlnIndented( "then" );
		writeConstruct( thenP, true );

		if( elseP != null ) {
			out.println();
			printlnIndented( "else" );
			writeConstruct( elseP, true );
		}
	}

	void writeProduce(final Produce produce)
	{
		final OWLIndividualList<OutputBinding> bindings = produce.getOutputBindings();
		if( !bindings.isEmpty() ) {
			final String str = "produce(";
			printIndented( str );
			writeBindings( bindings, str.length() );

			out.print( ")" );
		}
	}

	private void writeProcess(final AtomicProcess process) {
		printIndented("define atomic process " + process.getLocalName() + "(");

		if( !emptyProcess( process ) ) {
			out.println();
			writeIO(process);
			writePreconditions(process);
			writeResults(process);
		}
		out.println(")");
	}

	private void writePreconditions(final Process process)
	{
		indent(true);

		for (final Condition<?> condition : process.getConditions())
		{
			out.println(",");
			printIndented("precondition: (");
			condition.writeTo(this, indent + "               ");
			out.print(")");
		}

		indent(false);
	}

	private void writeResults(final Process process)
	{
		indent(true);

		for (final Result result : process.getResults())
		{
			out.println(",");
			printIndented("result: (");

			final OWLIndividualList<ResultVar> resultVars = result.getResultVars();
			if (!resultVars.isEmpty())
			{
				out.print("forall (");
				for (int i = 0; i < resultVars.size(); i++)
				{
					final ResultVar resultVar = resultVars.get(i);
					final OWLType type = resultVar.getParamType();
					out.print(qname(resultVar));
					out.print(" - " + qname(type.getURI()));
					if (i < resultVars.size() - 1)
					{
						out.println();
						printIndented("                 ");
					}
					else
						out.println(")");
				}
				printIndented("         ");
			}


			for (final Condition<?> condition : result.getConditions())
			{
				if (!condition.equals(OWLS.Expression.AlwaysTrue))
				{
					condition.writeTo(this, "               ");
					out.println();
					printlnIndented("         |->");
					printIndented(  "         ");
				}
			}

			final OWLIndividualList<OutputBinding> bindings = result.getBindings();
			if (!bindings.isEmpty())
			{
				out.print( "output(" );

				final int pad = "result: (".length() + "output(".length();
				writeBindings( bindings, pad );
				out.print( ")" );
			}

			final Expression<?> effect = result.getEffect();
			if (effect != null)
			{
				if (!bindings.isEmpty())
				{
					out.println(" & ");
					printIndented("         ");
				}

				effect.writeTo(this, "               ");
			}

			out.print(")");
		}

		indent(false);
	}

	public Writer getWriter() {
		return writer;
	}

	public void write(final Collection<Process> processes, final Writer outWriter) {
		setWriter( outWriter);

		write( processes );
	}

	public void write(final Collection<Process> processes, final OutputStream outStream) {
		setWriter( outStream );

		write( processes );
	}

	public QNameProvider getQNames() {
		return qnames;
	}

	public void setQNames(final QNameProvider qnames) {
		this.qnames = qnames;
	}


	public static void main(final String[] args) throws InvalidURIException, IOException, URISyntaxException
	{
		if (args.length == 0)
		{
			System.out.println("No URL given!");
			System.out.println("Usage: " + PresentationSyntaxWriter.class.getName() + " <URL>");
			return;
		}

		final URI uri = new URI(args[0]);

		final PresentationSyntaxWriter writer = new PresentationSyntaxWriter();
		writer.setDefaultNS(uri.toString());

		final OWLKnowledgeBase kb = OWLFactory.createKB();
		kb.setStrictConversion(false);
		kb.read(uri);

		final List<Process> processes = new ArrayList<Process>();
		processes.addAll(kb.getProcesses(Process.ATOMIC, true));
		processes.addAll(kb.getProcesses(Process.COMPOSITE, true));

		if (processes.isEmpty()) System.err.println("No processes found in " + uri);
		else writer.write(processes, System.out);
	}
}
