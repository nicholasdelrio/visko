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
 * Created on Dec 10, 2004
 */
package impl.owls;

import impl.owl.CombinedOWLConverter;
import impl.owl.GenericOWLConverter;
import impl.owl.ListConverter;
import impl.owls.expression.VariableBindingImpl;
import impl.owls.process.AtomicProcessImpl;
import impl.owls.process.CompositeProcessImpl;
import impl.owls.process.ResultImpl;
import impl.owls.process.SimpleProcessImpl;
import impl.owls.process.binding.InputBindingImpl;
import impl.owls.process.binding.LinkBindingImpl;
import impl.owls.process.binding.LocBindingImpl;
import impl.owls.process.binding.OutputBindingImpl;
import impl.owls.process.binding.ValueOfImpl;
import impl.owls.process.constructs.AnyOrderImpl;
import impl.owls.process.constructs.AsProcessImpl;
import impl.owls.process.constructs.ChoiceImpl;
import impl.owls.process.constructs.ForEachImpl;
import impl.owls.process.constructs.IfThenElseImpl;
import impl.owls.process.constructs.PerformImpl;
import impl.owls.process.constructs.ProduceImpl;
import impl.owls.process.constructs.RepeatUntilImpl;
import impl.owls.process.constructs.RepeatWhileImpl;
import impl.owls.process.constructs.SequenceImpl;
import impl.owls.process.constructs.SetImpl;
import impl.owls.process.constructs.SplitImpl;
import impl.owls.process.constructs.SplitJoinImpl;
import impl.owls.process.variable.ExistentialImpl;
import impl.owls.process.variable.InputImpl;
import impl.owls.process.variable.LinkImpl;
import impl.owls.process.variable.LocImpl;
import impl.owls.process.variable.OutputImpl;
import impl.owls.process.variable.ParticipantImpl;
import impl.owls.process.variable.ResultVarImpl;
import impl.owls.profile.ActorImpl;
import impl.owls.profile.ProfileImpl;
import impl.owls.profile.ServiceCategoryImpl;
import impl.owls.profile.ServiceParameterImpl;
import impl.owls.service.ServiceImpl;
import impl.util.InternalFactory;

import java.util.ArrayList;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owl.OWLObjectConverterRegistry;
import org.mindswap.owl.OWLValue;
import org.mindswap.owl.vocabulary.RDF;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.expression.Expression;
import org.mindswap.owls.expression.VariableBinding;
import org.mindswap.owls.expression.Expression.QuotedExpression;
import org.mindswap.owls.process.AnyOrder;
import org.mindswap.owls.process.AsProcess;
import org.mindswap.owls.process.AtomicProcess;
import org.mindswap.owls.process.Choice;
import org.mindswap.owls.process.CompositeProcess;
import org.mindswap.owls.process.ControlConstruct;
import org.mindswap.owls.process.ForEach;
import org.mindswap.owls.process.IfThenElse;
import org.mindswap.owls.process.Iterate;
import org.mindswap.owls.process.Perform;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Produce;
import org.mindswap.owls.process.RepeatUntil;
import org.mindswap.owls.process.RepeatWhile;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.Sequence;
import org.mindswap.owls.process.Set;
import org.mindswap.owls.process.SimpleProcess;
import org.mindswap.owls.process.Split;
import org.mindswap.owls.process.SplitJoin;
import org.mindswap.owls.process.variable.Binding;
import org.mindswap.owls.process.variable.Existential;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.InputBinding;
import org.mindswap.owls.process.variable.Link;
import org.mindswap.owls.process.variable.LinkBinding;
import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.LocBinding;
import org.mindswap.owls.process.variable.Local;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.process.variable.OutputBinding;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.Participant;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.process.variable.ResultVar;
import org.mindswap.owls.process.variable.ValueOf;
import org.mindswap.owls.profile.Actor;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;

/**
 * @author unascribed
 * @version $Rev: 2298 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
class OWLSConverters
{
	static final void registerConverters(final OWLObjectConverterRegistry registry)
	{
		final OWLObjectConverter<Service> serviceConverter =
			new GenericOWLConverter<Service>(ServiceImpl.class, OWLS.Service.Service);
		final OWLObjectConverter<Profile> profileConverter =
			new GenericOWLConverter<Profile>(ProfileImpl.class, OWLS.Profile.Profile);
		final OWLObjectConverter<Actor> actorConverter =
			new GenericOWLConverter<Actor>(ActorImpl.class, OWLS.Actor.Actor);
		final OWLObjectConverter<ServiceParameter> serviceParamConverter =
			new GenericOWLConverter<ServiceParameter>(ServiceParameterImpl.class, OWLS.ServiceParameter.ServiceParameter);
		final OWLObjectConverter<ServiceCategory> serviceCategoryConverter =
			new GenericOWLConverter<ServiceCategory>(ServiceCategoryImpl.class, OWLS.ServiceCategory.ServiceCategory);

		final OWLObjectConverter<AtomicProcess> atomicProcessConverter =
			new GenericOWLConverter<AtomicProcess>(AtomicProcessImpl.class, OWLS.Process.AtomicProcess);
		final OWLObjectConverter<CompositeProcess> compositeProcessConverter =
			new GenericOWLConverter<CompositeProcess>(CompositeProcessImpl.class, OWLS.Process.CompositeProcess);
		final OWLObjectConverter<SimpleProcess> simpleProcessConverter =
			new GenericOWLConverter<SimpleProcess>(SimpleProcessImpl.class, OWLS.Process.SimpleProcess);

		final List<OWLObjectConverter<? extends Process>> prCs =
			new ArrayList<OWLObjectConverter<? extends Process>>(3);
		prCs.add(atomicProcessConverter);
		prCs.add(compositeProcessConverter);
		prCs.add(simpleProcessConverter);
		final OWLObjectConverter<Process> processConverter = new CombinedOWLConverter<Process>(Process.class, prCs);

		final OWLObjectConverter<Existential> existentialConverter =
			new GenericOWLConverter<Existential>(ExistentialImpl.class, OWLS.Process.Existential);
		final OWLObjectConverter<Input> inputConverter =
			new GenericOWLConverter<Input>(InputImpl.class, OWLS.Process.Input);
		final OWLObjectConverter<Output> outputConverter =
			new GenericOWLConverter<Output>(OutputImpl.class, OWLS.Process.Output);
		final OWLObjectConverter<Link> linkConverter =
			new GenericOWLConverter<Link>(LinkImpl.class, OWLS.Process.Link);
		final OWLObjectConverter<Loc> locConverter =
			new GenericOWLConverter<Loc>(LocImpl.class, OWLS.Process.Loc);
		final OWLObjectConverter<Participant> participantConverter =
			new GenericOWLConverter<Participant>(ParticipantImpl.class, OWLS.Process.Participant);
		final OWLObjectConverter<ResultVar> resultVarConverter =
			new GenericOWLConverter<ResultVar>(ResultVarImpl.class, OWLS.Process.ResultVar);


		final List<OWLObjectConverter<? extends Local>> localCs =
			new ArrayList<OWLObjectConverter<? extends Local>>(2);
		localCs.add(locConverter);
		localCs.add(linkConverter);
		final OWLObjectConverter<Local> localConverter =
			new CombinedOWLConverter<Local>(Local.class, localCs);

		final List<OWLObjectConverter<? extends Parameter>> paCs =
			new ArrayList<OWLObjectConverter<? extends Parameter>>(2);
		paCs.add(inputConverter);
		paCs.add(outputConverter);
		final OWLObjectConverter<Parameter> parameterConverter =
			new CombinedOWLConverter<Parameter>(Parameter.class, paCs);

		final List<OWLObjectConverter<? extends ProcessVar>> procVarCs =
			new ArrayList<OWLObjectConverter<? extends ProcessVar>>(5);
		procVarCs.add(parameterConverter);
		procVarCs.add(localConverter);
		procVarCs.add(resultVarConverter);
		procVarCs.add(existentialConverter);
		procVarCs.add(participantConverter);
		final OWLObjectConverter<ProcessVar> processVarConverter =
			new CombinedOWLConverter<ProcessVar>(ProcessVar.class, procVarCs);

		final OWLObjectConverter<Expression.SPARQL> sparqlExprConverter =
			InternalFactory.createExpressionConverter(Expression.SPARQL.class,
				OWLS.Expression.SPARQL_Expression, OWLS.Expression.Expression, OWLS.Expression.SPARQL);
		final OWLObjectConverter<Expression.SWRL> swrlExprConverter =
			InternalFactory.createExpressionConverter(Expression.SWRL.class,
				OWLS.Expression.SWRL_Expression, OWLS.Expression.Expression, OWLS.Expression.SWRL);
		final OWLObjectConverter<Condition.SPARQL> sparqlCondConverter =
			InternalFactory.createExpressionConverter(Condition.SPARQL.class,
				OWLS.Expression.SPARQL_Condition, OWLS.Expression.Condition, OWLS.Expression.SPARQL);
		final OWLObjectConverter<Condition.SWRL> swrlCondConverter =
			InternalFactory.createExpressionConverter(Condition.SWRL.class,
				OWLS.Expression.SWRL_Condition, OWLS.Expression.Condition, OWLS.Expression.SWRL);


		final List<OWLObjectConverter<? extends Expression>> exprCs =
			new ArrayList<OWLObjectConverter<? extends Expression>>(2);
		exprCs.add(sparqlExprConverter);
		exprCs.add(swrlExprConverter);
		final OWLObjectConverter<Expression> expressionConverter =
			new CombinedOWLConverter<Expression>(Expression.class, exprCs);

		final List<OWLObjectConverter<? extends QuotedExpression>> quotedExprCs =
			new ArrayList<OWLObjectConverter<? extends QuotedExpression>>(2);
		quotedExprCs.add(sparqlExprConverter);
		final OWLObjectConverter<QuotedExpression> quotedExpressionConverter =
			new CombinedOWLConverter<QuotedExpression>(QuotedExpression.class, quotedExprCs);

		final List<OWLObjectConverter<? extends Condition>> condCs =
			new ArrayList<OWLObjectConverter<? extends Condition>>(2);
		condCs.add(sparqlCondConverter);
		condCs.add(swrlCondConverter);
		final OWLObjectConverter<Condition> conditionConverter =
			new CombinedOWLConverter<Condition>(Condition.class, condCs);

		final GenericOWLConverter<VariableBinding> variableBindingConverter =
			new GenericOWLConverter<VariableBinding>(VariableBindingImpl.class, OWLS.Expression.VariableBinding);

		final OWLObjectConverter<AsProcess> asProcessConverter =
			new GenericOWLConverter<AsProcess>(AsProcessImpl.class, OWLS.Process.AsProcess);
		final OWLObjectConverter<Perform> performConverter =
			new GenericOWLConverter<Perform>(PerformImpl.class, OWLS.Process.Perform);
		final OWLObjectConverter<Sequence> sequenceConverter =
			new GenericOWLConverter<Sequence>(SequenceImpl.class, OWLS.Process.Sequence);
		final OWLObjectConverter<Choice> choiceConverter =
			new GenericOWLConverter<Choice>(ChoiceImpl.class, OWLS.Process.Choice);
		final OWLObjectConverter<AnyOrder> anyOrderConverter =
			new GenericOWLConverter<AnyOrder>(AnyOrderImpl.class, OWLS.Process.AnyOrder);
		final OWLObjectConverter<IfThenElse> ifThenElseConverter =
			new GenericOWLConverter<IfThenElse>(IfThenElseImpl.class, OWLS.Process.IfThenElse);
		final OWLObjectConverter<RepeatWhile> repeatWhileConverter =
			new GenericOWLConverter<RepeatWhile>(RepeatWhileImpl.class, OWLS.Process.RepeatWhile);
		final OWLObjectConverter<RepeatUntil> repeatUntilConverter =
			new GenericOWLConverter<RepeatUntil>(RepeatUntilImpl.class, OWLS.Process.RepeatUntil);
		final OWLObjectConverter<ForEach> forEachConverter =
			new GenericOWLConverter<ForEach>(ForEachImpl.class, OWLS.Process.ForEach);
		final OWLObjectConverter<Set> setConverter =
			new GenericOWLConverter<Set>(SetImpl.class, OWLS.Process.Set);
		final OWLObjectConverter<Split> splitConverter =
			new GenericOWLConverter<Split>(SplitImpl.class, OWLS.Process.Split);
		final OWLObjectConverter<SplitJoin> splitJoinConverter =
			new GenericOWLConverter<SplitJoin>(SplitJoinImpl.class, OWLS.Process.SplitJoin);
		final OWLObjectConverter<Produce> produceConverter =
			new GenericOWLConverter<Produce>(ProduceImpl.class, OWLS.Process.Produce);


		final List<OWLObjectConverter<? extends Iterate>> itCs =
			new ArrayList<OWLObjectConverter<? extends Iterate>>(3);
		itCs.add(repeatWhileConverter);
		itCs.add(repeatUntilConverter);
		itCs.add(forEachConverter);
		final OWLObjectConverter<Iterate> iterateConverter = new CombinedOWLConverter<Iterate>(Iterate.class, itCs);


		final List<OWLObjectConverter<? extends ControlConstruct>> ccCs =
			new ArrayList<OWLObjectConverter<? extends ControlConstruct>>(13);
		ccCs.add(performConverter);
		ccCs.add(sequenceConverter);
		ccCs.add(choiceConverter);
		ccCs.add(anyOrderConverter);
		ccCs.add(ifThenElseConverter);
		ccCs.add(repeatWhileConverter);
		ccCs.add(repeatUntilConverter);
		ccCs.add(forEachConverter);
		ccCs.add(setConverter);
		ccCs.add(splitConverter);
		ccCs.add(splitJoinConverter);
		ccCs.add(produceConverter);
		ccCs.add(asProcessConverter);
		final OWLObjectConverter<ControlConstruct> controlConstructConverter =
			new CombinedOWLConverter<ControlConstruct>(ControlConstruct.class, ccCs);


		final ListConverter<OWLIndividual> objListConverter = new ListConverter<OWLIndividual>(OWLS.ObjectList.List);
		final ListConverter<OWLValue> listConverter = new ListConverter<OWLValue>(RDF.ListVocabulary);

		final ListConverter<ControlConstruct> ccListConverter = new ListConverter<ControlConstruct>(OWLS.Process.CCList);
		final ListConverter<ControlConstruct> ccBagConverter = new ListConverter<ControlConstruct>(OWLS.Process.CCBag);


		final OWLObjectConverter<InputBinding> inputBindingConverter =
			new GenericOWLConverter<InputBinding>(InputBindingImpl.class, OWLS.Process.InputBinding);
		final OWLObjectConverter<LinkBinding> linkBindingConverter =
			new GenericOWLConverter<LinkBinding>(LinkBindingImpl.class, OWLS.Process.LinkBinding);
		final OWLObjectConverter<LocBinding> locBindingConverter =
			new GenericOWLConverter<LocBinding>(LocBindingImpl.class, OWLS.Process.LocBinding);
		final OWLObjectConverter<OutputBinding> outputBindingConverter =
			new GenericOWLConverter<OutputBinding>(OutputBindingImpl.class, OWLS.Process.OutputBinding);

		final List<OWLObjectConverter<? extends Binding>> biCs =
			new ArrayList<OWLObjectConverter<? extends Binding>>(4);
		biCs.add(inputBindingConverter);
		biCs.add(outputBindingConverter);
		biCs.add(locBindingConverter);
		biCs.add(linkBindingConverter);
		final OWLObjectConverter<Binding> bindingConverter = new CombinedOWLConverter<Binding>(Binding.class, biCs);


		final OWLObjectConverter<Result> resultConverter =
			new GenericOWLConverter<Result>(ResultImpl.class, OWLS.Process.Result);
		final OWLObjectConverter<ValueOf> valueOfConverter =
			new GenericOWLConverter<ValueOf>(ValueOfImpl.class, OWLS.Process.ValueOf);


		registry.registerConverter(Service.class, serviceConverter);
		registry.registerConverter(Profile.class, profileConverter);
		registry.registerConverter(ServiceParameter.class, serviceParamConverter);
		registry.registerConverter(ServiceCategory.class, serviceCategoryConverter);
		registry.registerConverter(Actor.class, actorConverter);


		registry.registerConverter(AtomicProcess.class, atomicProcessConverter);
		registry.registerConverter(CompositeProcess.class, compositeProcessConverter);
		registry.registerConverter(SimpleProcess.class, simpleProcessConverter);
		registry.extendByConverter(Process.class, processConverter);

		registry.registerConverter(AsProcess.class, asProcessConverter);
		registry.registerConverter(Perform.class, performConverter);
		registry.registerConverter(Sequence.class, sequenceConverter);
		registry.registerConverter(Choice.class, choiceConverter);
		registry.registerConverter(AnyOrder.class, anyOrderConverter);
		registry.registerConverter(IfThenElse.class, ifThenElseConverter);
		registry.registerConverter(Iterate.class, iterateConverter);
		registry.registerConverter(RepeatWhile.class, repeatWhileConverter);
		registry.registerConverter(RepeatUntil.class, repeatUntilConverter);
		registry.registerConverter(ForEach.class, forEachConverter);
		registry.registerConverter(Set.class, setConverter);
		registry.registerConverter(Split.class, splitConverter);
		registry.registerConverter(SplitJoin.class, splitJoinConverter);
		registry.registerConverter(Produce.class, produceConverter);
		registry.registerConverter(ControlConstruct.class, controlConstructConverter);

		registry.registerConverter(Existential.class, existentialConverter);
		registry.registerConverter(Input.class, inputConverter);
		registry.registerConverter(Link.class, linkConverter);
		registry.registerConverter(Loc.class, locConverter);
		registry.registerConverter(Output.class, outputConverter);
		registry.registerConverter(Participant.class, participantConverter);
		registry.registerConverter(ResultVar.class, resultVarConverter);
		registry.registerConverter(Local.class, localConverter);
		registry.registerConverter(Parameter.class, parameterConverter);
		registry.registerConverter(ProcessVar.class, processVarConverter);

		registry.registerConverter(InputBinding.class, inputBindingConverter);
		registry.registerConverter(OutputBinding.class, outputBindingConverter);
		registry.registerConverter(LinkBinding.class, linkBindingConverter);
		registry.registerConverter(LocBinding.class, locBindingConverter);
		registry.registerConverter(Binding.class, bindingConverter);

		registry.registerConverter(Result.class, resultConverter);
		registry.registerConverter(ValueOf.class, valueOfConverter);

		registry.registerListConverter(OWLS.ObjectList.List, objListConverter);
		registry.registerListConverter(RDF.ListVocabulary, listConverter);
		registry.registerListConverter(OWLS.Process.CCList, ccListConverter);
		registry.registerListConverter(OWLS.Process.CCBag, ccBagConverter);

		registry.registerConverter(Expression.SPARQL.class, sparqlExprConverter);
		registry.registerConverter(Expression.SWRL.class, swrlExprConverter);
		registry.registerConverter(Condition.SPARQL.class, sparqlCondConverter);
		registry.registerConverter(Condition.SWRL.class, swrlCondConverter);
		registry.extendByConverter(Condition.class, conditionConverter);
		registry.extendByConverter(Expression.class, expressionConverter);
		registry.extendByConverter(QuotedExpression.class, quotedExpressionConverter);
		registry.registerConverter(VariableBinding.class, variableBindingConverter);
	}
}
