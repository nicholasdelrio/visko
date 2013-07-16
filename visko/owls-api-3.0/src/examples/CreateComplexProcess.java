// The MIT License
//
// Copyright (c) 2005 Evren Sirin
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
 * Created on Sep 9, 2005
 */
package examples;

import java.io.IOException;
import java.net.URI;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLDataProperty;
import org.mindswap.owl.OWLDataType;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.list.OWLList;
import org.mindswap.owl.vocabulary.XSD;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.grounding.WSDLAtomicGrounding;
import org.mindswap.owls.grounding.WSDLGrounding;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.execution.DefaultProcessMonitor;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.ValueConstant;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.query.ValueMap;
import org.mindswap.swrl.Atom;
import org.mindswap.swrl.SWRLDataObject;
import org.mindswap.swrl.SWRLFactory;
import org.mindswap.swrl.SWRLFactory.ISWRLFactory;
import org.mindswap.utils.URIUtils;

/**
 * This example shows how to create a complex process model that is shown in the file
 * <a href="http://www.mindswap.org/2004/owl-s/1.1/FindCheaperBook.owl">FindCheaperBook.owl
 * </a>. The process model contains a sequence that starts with a service that gets the
 * price for a given book title, then has a SplitJoin to get the book price from Amazon and
 * Barnes&Nobles concurrently, and as a last step has an If-Then-Else construct to compare
 * the prices and return the cheaper book price.
 *
 * @author unascribed
 * @version $Rev: 2276 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class CreateComplexProcess
{

	// knowledge base we are going to use
	OWLKnowledgeBase kb;

	// ontology where service description is saved
	OWLOntology ont;

	// SWRL factory to create conditions and expressions
	ISWRLFactory swrl;

	// processes we will use
	Process BookFinder;
	Process BNPrice;
	Process AmazonPrice;

	// commonly used classes, properties, datatypes
	OWLDataType xsdString;
	OWLDataType xsdFloat;
	OWLClass Price;
	OWLDataProperty amount;

	// the process we create to do the comparison
//	CompositeProcess ComparePricesProcess;

	// the inputs of the comparison process
	Input CP_AmazonPrice;
	Input CP_BNPrice;

	// the outputs of the comparison process
	Output CP_Bookstore;
	Output CP_OutputPrice;

	public CreateComplexProcess() {
	}

	private URI uri(final String localName) {
		return URIUtils.createURI(ExampleURIs.FIND_CHEAPER_BOOK_OWLS12, localName);
	}

	private Service createService()
	{
		final Service service = ont.createService(uri("TestService"));

		final CompositeProcess process = createProcess();
		final Profile profile = createProfile(process);
		final WSDLGrounding grounding = createGrounding(process);

		service.addProfile(profile);
		service.setProcess(process);
		service.addGrounding(grounding);

		return service;
	}

	private Profile createProfile( final Process process ) {
		final Profile profile = ont.createProfile(uri("TestProfile"));

		profile.setLabel("Cheaper Book Finder", null);
		profile.setTextDescription("Find the price of book in Amazon and Barnes & Nobles " +
			"and return the cheaper price");

		for(int i = 0; i < process.getInputs().size(); i++) {
			final Input input = process.getInputs().get( i );

			profile.addInput( input );
		}

		for(int i = 0; i < process.getOutputs().size(); i++) {
			final Output output = process.getOutputs().get( i );

			profile.addOutput( output );
		}

		return profile;
	}

	private WSDLGrounding createGrounding(final CompositeProcess process)
	{
		final WSDLGrounding grounding = ont.createWSDLGrounding(uri("TestGrounding"));

		final OWLIndividualList<Process> list = process.getComposedOf().getAllProcesses(false);
		for (final Process p : list)
		{
			if (p instanceof AtomicProcess)
			{
				grounding.addGrounding(((AtomicProcess) p).getGrounding().castTo(WSDLAtomicGrounding.class));
			}
		}

		return grounding;
	}

	private CompositeProcess createProcess() {
		// create the composite process
		final CompositeProcess process = ont.createCompositeProcess( uri( "TestProcess") );

		// Create an input that has parameterType xsd:string
		final Input inputBookName = process.createInput( uri( "BookName" ), xsdString );

		// Create an output that has parameterType xsd:string
		final Output outputBookstore = process.createOutput( uri( "Bookstore" ), xsdString );

		// Create an output that has parameterType concepts:Price
		final Output outputBookPrice = process.createOutput( uri( "BookPrice" ), Price );

		// process is composed of a sequence
		final Sequence sequence = ont.createSequence(null);
		process.setComposedOf(sequence);

		// first element of the sequence is a simple perform
		final Perform FindBookInfo = ont.createPerform( uri( "FindBookInfo" ) );
		FindBookInfo.setProcess( BookFinder );
		// the input of the FindBookInfo perform is coming from the enclosing perform
		FindBookInfo.addBinding( BookFinder.getInput(), OWLS.Process.ThisPerform, inputBookName );

		// add this perform as the first element of the sequence
		sequence.addComponent( FindBookInfo );

		// second element of the sequence is a Split-Join that has two branches, one perform in each
		final SplitJoin split = ont.createSplitJoin(null);

		// first perform AmazonPrice
		final Perform FindAmazonPrice = ont.createPerform( uri( "FindAmazonPrice" ) );
		FindAmazonPrice.setProcess( AmazonPrice );
		// the input of the perform is coming from FindBookInfo perform
		FindAmazonPrice.addBinding( AmazonPrice.getInput(), FindBookInfo, BookFinder.getOutput() );
		// add it to the split-join
		split.addComponent( FindAmazonPrice );

		// then similarly perform BNPrice
		final Perform FindBNPrice = ont.createPerform( uri( "FindBNPrice" ) );
		FindBNPrice.setProcess( BNPrice );
		// the input of the perform is coming from FindBookInfo perform
		FindBNPrice.addBinding( BNPrice.getInput(), FindBookInfo, BookFinder.getOutput() );
		// add it to the split-join
		split.addComponent( FindBNPrice );

		// finally add the Split-Join to the sequence
		sequence.addComponent( split );

		// last element of the sequence is a composite process that compares the prices and selects the cheaper
		final CompositeProcess ComparePricesProc = createComparePricesProcess();
		final Perform ComparePrices = ont.createPerform(uri("ComparePrices"));
		ComparePrices.setProcess(ComparePricesProc);
		// feed the input from book previous performs to the comparison process
		ComparePrices.addBinding( CP_AmazonPrice, FindAmazonPrice, AmazonPrice.getOutput() );
		ComparePrices.addBinding( CP_BNPrice, FindBNPrice, BNPrice.getOutput() );

		// add the comparison step as the last construct in the sequence
		sequence.addComponent( ComparePrices );

		final Result result = process.createResult(null);
		result.addBinding( outputBookstore, ComparePrices, CP_Bookstore );
		result.addBinding( outputBookPrice, ComparePrices, CP_OutputPrice );

		return process;
	}

	private CompositeProcess createComparePricesProcess() {
		// we need a new composite process for the last step to do the
		// comparison (we need local variables which can only be declared
		// in conjunction with a process)
		final CompositeProcess ComparePricesProc =
			ont.createCompositeProcess( uri( "ComparePricesProcess" ) );

		// the price from two bookstores as input
		CP_AmazonPrice = ComparePricesProc.createInput( uri( "CP_AmazonPrice"), Price );
		CP_BNPrice = ComparePricesProc.createInput( uri( "CP_BNPrice"), Price );

		// the actual value for each price as locals
		final Loc CP_AmazonPriceAmount = ComparePricesProc.createLoc(uri("CP_AmazonPriceAmount"), xsdFloat);
		final Loc CP_BNPriceAmount = ComparePricesProc.createLoc(uri("CP_BNPriceAmount"), xsdFloat);

		// the minimum price and the associated bookstore as outputs
		CP_OutputPrice = ComparePricesProc.createOutput( uri( "CP_OutputPrice"), Price );
		CP_Bookstore = ComparePricesProc.createOutput( uri( "CP_Bookstore"), xsdString );

		// the first precondition is just to bind the value of concepts:amount property
		// to the local variable for AmazonPrice
		final Condition.SWRL precondition1 = ont.createSWRLCondition(null);
		final Atom atom1 = swrl.createDataPropertyAtom( amount, CP_AmazonPrice, CP_AmazonPriceAmount );
		final OWLList<Atom> list1 = swrl.createList( atom1 );
		precondition1.setBody( list1 );
		ComparePricesProc.addCondition( precondition1 );

		// exactly same as the previous one but for BNPrice. note that we could have
		// equivalently create one precondition and add two atoms in it. the operational
		// semantics would be same because multiple preconditions are simply conjuncted
		final Condition.SWRL precondition2 = ont.createSWRLCondition(null);
		final Atom atom2 = swrl.createDataPropertyAtom( amount, CP_BNPrice, CP_BNPriceAmount );
		final OWLList<Atom> list2 = swrl.createList( atom2 );
		precondition2.setBody( list2 );
		ComparePricesProc.addCondition( precondition2 );

		// ComparePricesProcess is simply one If-Then-Else
		final IfThenElse ifThenElse = ont.createIfThenElse(null);
		ComparePricesProc.setComposedOf( ifThenElse );

		// now the condition for If-Then-Else to compare values
		final Condition.SWRL condition = ont.createSWRLCondition(null);
		final Atom atom = swrl.createLessThan(
			CP_BNPriceAmount.castTo(SWRLDataObject.class),
			CP_AmazonPriceAmount.castTo(SWRLDataObject.class));
		final OWLList<Atom> list = swrl.createList( atom );
		condition.setBody( list );

		// set the condition
		ifThenElse.setCondition( condition );

		// create two produce constructs to generate the results
		ifThenElse.setThen( createProduceSequence( "BN", CP_BNPrice ) );
		ifThenElse.setElse( createProduceSequence( "Amazon", CP_AmazonPrice ) );

		return ComparePricesProc;
	}

	private Sequence createProduceSequence( final String bookstore, final Input price ) {
		// the produce construct to produce the price
		final Produce producePrice = ont.createProduce( uri( "Produce" + bookstore + "Price" ) );
		// we directly pass the input value from the enclosing process as output to the enclosing process
		producePrice.addBinding( CP_OutputPrice, OWLS.Process.ThisPerform, price );

		// the produce construct to produce the name
		final Produce produceName = ont.createProduce( uri( "Produce" + bookstore + "Name" ) );
		final OutputBinding binding = ont.createOutputBinding(null);
		binding.setProcessVar(CP_Bookstore);
		// we need a constant string value to produce so use process:valueData
		final ValueConstant valueData = ont.createValueConstant(ont.createDataValue(bookstore), binding);
		binding.setValue(valueData);
		// add the binding for this output
		produceName.addBinding(binding);

		// now add both produce into this sequence
		final Sequence sequence = ont.createSequence(null);
		sequence.addComponent( producePrice );
		sequence.addComponent( produceName );

		return sequence;
	}

	public void run() throws ExecutionException, IOException
	{
		// create an OWL-S knowledge base
		kb = OWLFactory.createKB();

		// create an empty ontology in this KB
		ont = kb.createOntology(ExampleURIs.FIND_CHEAPER_BOOK_OWLS12);

		// create SWRLFactory we will use for creating conditions and expressions
		swrl = SWRLFactory.createFactory(ont);

		// load three OWL-S files and directly get the processes we want
		BookFinder = kb.readService(ExampleURIs.BOOK_FINDER_OWLS12).getProcess();
		BNPrice = kb.readService(ExampleURIs.BN_BOOK_PRICE_OWLS12).getProcess();
		AmazonPrice = kb.readService(ExampleURIs.AMAZON_BOOK_PRICE_OWLS12).getProcess();

		// also add the imports statement
		ont.addImport( BookFinder.getOntology() );
		ont.addImport( BNPrice.getOntology() );
		ont.addImport( AmazonPrice.getOntology() );

		// get common classes, properties and datatypes we will need
		xsdString = kb.getDataType( XSD.string );
		xsdFloat = kb.getDataType( XSD.xsdFloat );
		Price = kb.getClass(URIUtils.createURI(URI.create(ExampleURIs.ONT_MINDSWAP_CONCEPTS), "Price"));
		amount = kb.getDataProperty(URIUtils.createURI(URI.create(ExampleURIs.ONT_MINDSWAP_CONCEPTS), "amount"));

		// create the service
		final Service s = createService();

		// print the description of new service to standard output
		ont.write(System.out, ExampleURIs.FIND_CHEAPER_BOOK_OWLS12);
		System.out.println();

		// create an execution engine
		final ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();

		exec.addMonitor(new DefaultProcessMonitor());

		// get the process of the new service
		final Process process = s.getProcess();
		// initialize the input values to be empty
		final ValueMap<Input, OWLValue> inputs = new ValueMap<Input, OWLValue>();
		// get the parameter using the local name
		inputs.setValue(process.getInput(), kb.createDataValue("City of Glass"));

		// execute the service
		final ValueMap<Output, OWLValue> outputs = exec.execute(process, inputs, kb);

		// get the output param using the index
		final String bookstore = outputs.getStringValue(process.getOutput("Bookstore"));
		final OWLIndividual price = outputs.getIndividualValue(process.getOutput("BookPrice"));

		// display the result
		System.out.println("Cheaper price found in " + bookstore);
		System.out.println("Price is $" + price.getProperty(amount));
		System.out.println();
	}

	public static void main( final String[] args )
	{
		final CreateComplexProcess test = new CreateComplexProcess();
		try
		{
			test.run();
		}
		catch (final ExecutionException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
