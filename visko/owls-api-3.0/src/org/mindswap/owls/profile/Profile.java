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
package org.mindswap.owls.profile;

import java.util.List;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLObjectProperty;
import org.mindswap.owls.process.MultiConditional;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.Result;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;

/**
 * Represents the OWL-S profile.
 * <p>
 * Corresponding OWL-S concept: {@link org.mindswap.owls.vocabulary.OWLS.Profile#Profile}
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:54 $
 */
public interface Profile extends OWLIndividual, MultiConditional {
	/**
	 * @return The service this profile presents.
	 */
	public Service getService();

	/**
	 * @param service The service this profile presents.
	 */
	public void setService(Service service);

	/**
	 * Removes the service by breaking the link <code>service:presentedBy</code>.
	 * The service itself remains untouched.
	 */
	public void removeService();

	/**
	 * Get the process associated with this profile. This process is the one that is the
	 * value of profile:has_process parameter
	 *
	 * @return
	 */
	public Process getProcess();

	/**
	 * Set profile's text description. No language identifier will be used.
	 *
	 * @param desc
	 */
	public void setTextDescription(String desc);

	/**
	 * Get profile's text description. See {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 * to learn how the language identifiers will be resolved when searching for a text description.
	 *
	 * @return
	 */
	public String getTextDescription();

	/**
	 * Get profile's text description. The associated textDescription should have the same language
	 * identifier as given in the parameter. If a textDescription for that labguage is not found null
	 * value will be returned even if there is another textDescription with a different language
	 * identifier.
	 *
	 * @param lang
	 * @return
	 */
	public String getTextDescription(String lang);

	/**
	 * Return all text descriptions written in any language.
	 *
	 * @return
	 */
	public List<OWLDataValue> getTextDescriptions();

	/**
	 * Get the service name defined in the profile. See {@link org.mindswap.owl.OWLConfig#DEFAULT_LANGS OWLConfig}
	 * to learn how the language identifiers will be resolved when searching for a name.
	 *
	 * @return
	 */
	public String getServiceName();

	/**
	 * Get the service name defined in the profile. The associated serviceName should have the same
	 * language identifier as given in the parameter. If a serviceName for that language is not found
	 * null value will be returned even if there is another serviceName with a different language
	 * identifier.
	 *
	 * @param lang
	 * @return
	 */
	public String getServiceName(String lang);

	/**
	 * Return all service names written in any language.
	 *
	 * @return
	 */
	public List<OWLDataValue> getServiceNames();

	/**
	 * Set the service name. No language identifier will be used.
	 *
	 * @param desc
	 */
	public void setServiceName(String desc);

	public void addInput(Input input);

	public void addOutput(Output output);

	public void addInputs(OWLIndividualList<Input> inputs);

	public void addOutputs(OWLIndividualList<Output> inputs);

	public void addResult(Result result);

	/**
	 * @return The inputs for this profile.
	 */
	public OWLIndividualList<Input> getInputs();

	/**
	 * @return The outputs for this profile.
	 */
	public OWLIndividualList<Output> getOutputs();

	/**
	 * @return The results for this profile.
	 */
	public OWLIndividualList<Result> getResults();

	/**
	 * Get the result for this profile. In case there are multiple results defined for this
	 * profile a random one is returned
	 *
	 * @return
	 */
	public Result getResult();

	/**
	 * getCategory
	 *
	 * @return
	 */
	public ServiceCategory getCategory();

	/**
	 * @return The list of all categories defined for this profile.
	 */
	public OWLIndividualList<ServiceCategory> getCategories();

	/**
	 * Add a new category for this profile.
	 *
	 * @param category
	 */
	public void addCategory(ServiceCategory category);

	/**
	 * Removes the given service category by breaking the property
	 * <code>servCategory:serviceCategory</code>. The category itself is not
	 * touched at all.
	 *
	 * @param category The service category to remove. A value of
	 * 	<code>null</code> will remove all categories.
	 */
	public void removeCategory(ServiceCategory category);

	/**
	 * getContactInfo
	 *
	 * @return
	 */
	public OWLIndividual getContactInfo();

	/**
	 * @return All the contact info values.
	 */
	public OWLIndividualList<?> getContactInfos();

	/**
	 * @param ind The contact info to add.
	 */
	public void addContactInfo(OWLIndividual ind);

	/**
	 * Removes the given contact info individual by breaking the property
	 * <code>profile:contactInformation</code>. The result itself is not
	 * touched at all.
	 *
	 * @param ind The contact info individual to remove. A value of
	 * 	<code>null</code> will remove all contact infos.
	 */
	public void removeContactInfo(OWLIndividual ind);

	/**
	 * getServiceParameters
	 *
	 * @return
	 */
	public OWLIndividualList<ServiceParameter> getServiceParameters();

	/**
	 * getServiceParameter
	 *
	 * @return
	 */
	public ServiceParameter getServiceParameter(OWLObjectProperty prop);

	public ServiceParameter getServiceParameter(String name);

	public OWLIndividual getServiceParameterValue(OWLObjectProperty prop);

	public OWLIndividual getServiceParameterValue(String name);

	public void addServiceParameter(ServiceParameter serviceParam);

	/**
	 * Removes the given input by breaking the property <code>profile:hasInput</code>
	 * The input itself is not touched at all.
	 *
	 * @param input The input to remove. A value of <code>null</code> will remove
	 * 	all inputs.
	 */
	public void removeInput(Input input);

	/**
	 * Removes the given output by breaking the property <code>profile:hasOutput</code>
	 * The output itself is not touched at all.
	 *
	 * @param output The output to remove. A value of <code>null</code> will remove
	 * 	all outputs.
	 */
	public void removeOutput(Output output);

	/**
	 * Removes the given result by breaking the property <code>profile:hasResult</code>.
	 * The result itself is not touched at all.
	 *
	 * @param result The result to remove. A value of <code>null</code> will remove
	 * 	all results.
	 */
	public void removeResult(Result result);

	/**
	 * Removes the given service parameter by breaking the property <code>servParam:serviceParameter</code>.
	 * The service parameter itself is not touched at all.
	 *
	 * @param param The service parameter to remove. A value of <code>null</code> will remove
	 * 	all service parameters.
	 */
	public void removeServiceParameter(ServiceParameter param);


}
