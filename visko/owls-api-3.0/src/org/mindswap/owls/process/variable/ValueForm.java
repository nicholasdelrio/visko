/*
 * Created on Aug 26, 2004
 */
package org.mindswap.owls.process.variable;

import org.mindswap.owl.OWLIndividual;

/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public interface ValueForm extends ParameterValue
{
	public String getForm();

	public OWLIndividual getFormAsIndividual();

	public void setForm(String formLiteral);

	public void removeForm();
}
