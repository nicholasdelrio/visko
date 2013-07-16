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
 * Created on Dec 27, 2003
 */
package impl.owls.profile;

import impl.owl.WrappedIndividual;

import java.util.List;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.expression.Condition;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.profile.Actor;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.profile.ServiceCategory;
import org.mindswap.owls.profile.ServiceParameter;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.Utils;

/**
 *
 * @author unascribed
 * @version $Rev: 2323 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ProfileImpl extends WrappedIndividual implements Profile
{
	public ProfileImpl(final OWLIndividual ind) {
		super(ind);
	}

	/* @see org.mindswap.owls.profile.Profile#getService() */
	public Service getService() {
		return getPropertyAs(OWLS.Service.presentedBy, Service.class);
	}

	/* @see org.mindswap.owls.profile.Profile#setTextDescription(java.lang.String) */
	public void setTextDescription(final String desc) {
		setProperty(OWLS.Profile.textDescription, desc);
	}

	/* @see org.mindswap.owls.profile.Profile#getTextDescription() */
	public String getTextDescription() {
		return getPropertyAsString(OWLS.Profile.textDescription);
	}

	/* @see org.mindswap.owls.profile.Profile#getTextDescription(java.lang.String) */
	public String getTextDescription(final String lang) {
		return getPropertyAsString(OWLS.Profile.textDescription, lang);
	}

	/* @see org.mindswap.owls.profile.Profile#getTextDescriptions() */
	public List<OWLDataValue> getTextDescriptions() {
		return getProperties(OWLS.Profile.textDescription);
	}

	/* @see org.mindswap.owls.profile.Profile#addInput(org.mindswap.owls.process.Input) */
	public void addInput(final Input input) {
		addProperty(OWLS.Profile.hasInput, input);
	}

	/* @see org.mindswap.owls.profile.Profile#addOutput(org.mindswap.owls.process.Output) */
	public void addOutput(final Output output) {
		addProperty(OWLS.Profile.hasOutput, output);
	}

	/* @see org.mindswap.owls.profile.Profile#addInputs(org.mindswap.owl.OWLIndividualList) */
	public void addInputs(final OWLIndividualList<Input> inputs) {
		for (final Input input : inputs) addInput(input);
	}

	/* @see org.mindswap.owls.profile.Profile#addOutputs(org.mindswap.owl.OWLIndividualList) */
	public void addOutputs(final OWLIndividualList<Output> outputs) {
		for (final Output output : outputs) addOutput(output);
	}

	/* @see org.mindswap.owls.profile.Profile#addResult(org.mindswap.owls.process.Result) */
	public void addResult(final Result result) {
		addProperty(OWLS.Profile.hasResult, result);
	}

	/* @see org.mindswap.owls.profile.Profile#getInputs() */
	public OWLIndividualList<Input> getInputs()
	{
		return getPropertiesAs(OWLS.Profile.hasInput, Input.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getOutputs() */
	public OWLIndividualList<Output> getOutputs() {
		return getPropertiesAs(OWLS.Profile.hasOutput, Output.class);
	}

	/* @see org.mindswap.owls.process.MultiConditional#getConditions() */
	public OWLIndividualList<Condition> getConditions()
	{
		return getPropertiesAs(OWLS.Profile.hasPrecondition, Condition.class);
	}

	/* @see org.mindswap.owls.process.MultiConditional#addCondition(org.mindswap.owls.process.Condition) */
	public void addCondition(final Condition<?> condition) {
		addProperty(OWLS.Profile.hasPrecondition, condition);
	}

	/* @see org.mindswap.owls.process.MultiConditional#removeCondition(org.mindswap.owls.expression.Condition) */
	public void removeCondition(final Condition<?> condition)
	{
		removeProperty(OWLS.Profile.hasPrecondition, null);
	}

	/* @see org.mindswap.owls.profile.Profile#getResults() */
	public OWLIndividualList<Result> getResults()
	{
		return getPropertiesAs(OWLS.Profile.hasResult, Result.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getResult() */
	public Result getResult() {
		return getPropertyAs(OWLS.Profile.hasResult, Result.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getProcess() */
	public Process getProcess() {
		return getService().getProcess();
	}

	/* @see org.mindswap.owls.profile.Profile#setService(org.mindswap.owls.service.Service) */
	public void setService(final Service service) {
		if (hasProperty(OWLS.Service.presentedBy, service)) return;

		setProperty(OWLS.Service.presentedBy, service);
		service.addProfile(this);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		Object o = getLabel(null);
		if (o != null) str.append(o).append(" ");
		o = getURI();
		if (o != null) str.append(o).append(" ");
		str.append(getType());
		str.append(Utils.LINE_SEPARATOR);

		str.append("Inputs  ").append(getInputs()).append(Utils.LINE_SEPARATOR);
		str.append("Outputs ").append(getOutputs()).append(Utils.LINE_SEPARATOR);

		return str.toString();
	}

	/* @see org.mindswap.owls.profile.Profile#getCategory() */
	public ServiceCategory getCategory() {
		return getPropertyAs(OWLS.ServiceCategory.serviceCategory, ServiceCategory.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getCategories() */
	public OWLIndividualList<ServiceCategory> getCategories() {
		return getPropertiesAs(OWLS.ServiceCategory.serviceCategory, ServiceCategory.class);
	}

	/* @see org.mindswap.owls.profile.Profile#addCategory(org.mindswap.owls.profile.ServiceCategory) */
	public void addCategory(final ServiceCategory category) {
		addProperty(OWLS.ServiceCategory.serviceCategory, category);
	}

	/* @see org.mindswap.owls.profile.Profile#removeCategory(org.mindswap.owls.profile.ServiceCategory) */
	public void removeCategory(final ServiceCategory category) {
		removeProperty(OWLS.ServiceCategory.serviceCategory, category);
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceParameters() */
	public OWLIndividualList<ServiceParameter> getServiceParameters() {
		return getPropertiesAs(OWLS.ServiceParameter.serviceParameter, ServiceParameter.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getContactInfos() */
	public OWLIndividualList<Actor> getContactInfos() {
		return getPropertiesAs(OWLS.Profile.contactInformation, Actor.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getContactInfo() */
	public Actor getContactInfo() {
		return getPropertyAs(OWLS.Profile.contactInformation, Actor.class);
	}

	/* @see org.mindswap.owls.profile.Profile#removeContactInfo(org.mindswap.owl.OWLIndividual) */
	public void removeContactInfo(final OWLIndividual contact) {
		removeProperty(OWLS.Profile.contactInformation, contact);
	}

	/* @see org.mindswap.owls.profile.Profile#addContactInfo(org.mindswap.owl.OWLIndividual) */
	public void addContactInfo(final OWLIndividual contact) {
		addProperty(OWLS.Profile.contactInformation, contact);
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceParameter(org.mindswap.owl.OWLObjectProperty) */
	public ServiceParameter getServiceParameter(final OWLObjectProperty prop) {
		return getPropertyAs(prop, ServiceParameter.class);
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceParameter(java.lang.String) */
	public ServiceParameter getServiceParameter(final String name) {
		final OWLIndividualList<ServiceParameter> serviceParams = getServiceParameters();
		for (final ServiceParameter serviceParam : serviceParams)
		{
			final String serviceParamName = serviceParam.getName();
			if (name.equals(serviceParamName)) return serviceParam;
		}
		return null;
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceParameterValue(org.mindswap.owl.OWLObjectProperty) */
	public OWLIndividual getServiceParameterValue(final OWLObjectProperty prop) {
		final ServiceParameter serviceParam = getServiceParameter(prop);
		return (serviceParam == null) ? null : serviceParam.getParameter();
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceParameterValue(java.lang.String) */
	public OWLIndividual getServiceParameterValue(final String name) {
		final ServiceParameter serviceParam = getServiceParameter(name);
		return (serviceParam == null) ? null : serviceParam.getParameter();
	}

	/* @see org.mindswap.owls.profile.Profile#addServiceParameter(org.mindswap.owls.profile.ServiceParameter) */
	public void addServiceParameter(final ServiceParameter serviceParam) {
		addProperty(OWLS.ServiceParameter.serviceParameter, serviceParam);
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceName() */
	public String getServiceName() {
		return getPropertyAsString(OWLS.Profile.serviceName);
	}

	/*  @see org.mindswap.owls.profile.Profile#getServiceName(java.lang.String) */
	public String getServiceName(final String lang) {
		return getPropertyAsString(OWLS.Profile.serviceName, lang);
	}

	/* @see org.mindswap.owls.profile.Profile#getServiceNames() */
	public List<OWLDataValue> getServiceNames() {
		return getProperties(OWLS.Profile.serviceName);
	}

	/* @see org.mindswap.owls.profile.Profile#setServiceName(java.lang.String) */
	public void setServiceName(final String name) {
		setProperty(OWLS.Profile.serviceName, name);
	}

	/* @see org.mindswap.owls.profile.Profile#removeService() */
	public void removeService() {
		removeProperty(OWLS.Service.presentedBy, getService());
	}

	/* @see org.mindswap.owls.profile.Profile#removeInput(org.mindswap.owls.process.Input) */
	public void removeInput(final Input input) {
		removeProperty(OWLS.Profile.hasInput, input);
	}

	/* @see org.mindswap.owls.profile.Profile#removeOutput(org.mindswap.owls.process.Output) */
	public void removeOutput(final Output output) {
		removeProperty(OWLS.Profile.hasOutput, output);
	}

	/* @see org.mindswap.owls.profile.Profile#removeResult(org.mindswap.owls.process.Result) */
	public void removeResult(final Result result) {
		removeProperty(OWLS.Profile.hasResult, result);
	}

	/* @see org.mindswap.owls.profile.Profile#removeServiceParameter(org.mindswap.owls.profile.ServiceParameter) */
	public void removeServiceParameter(final ServiceParameter param)
	{
		removeProperty(OWLS.ServiceParameter.serviceParameter, param);
	}

}
