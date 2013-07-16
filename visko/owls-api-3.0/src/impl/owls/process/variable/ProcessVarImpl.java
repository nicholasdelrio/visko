/*
 * Created 17.06.2009
 *
 * (c) 2009 Thorsten Möller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package impl.owls.process.variable;

import impl.owl.WrappedIndividual;

import org.mindswap.owl.OWLDataValue;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLType;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.process.Process;
import org.mindswap.owls.process.variable.ProcessVar;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.utils.RDFUtils;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public abstract class ProcessVarImpl extends WrappedIndividual implements ProcessVar
{

	public ProcessVarImpl(final OWLIndividual ind)
	{
		super(ind);
	}

	/* @see org.mindswap.common.Variable#getName() */
	public String getName()
	{
		return getLocalName();
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#getParamType() */
	public OWLType getParamType()
	{
		return getKB().getPropertyAsOWLType(this, OWLS.Process.parameterType);
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#setParamType(org.mindswap.owl.OWLType) */
	public void setParamType(final OWLType type) {
		setProperty(OWLS.Process.parameterType, type.getURI());
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#getService() */
	public Service getService() {
		Process process = getProcess();
		return (process == null)? null : process.getService();
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#getProfile() */
	public Profile getProfile()
	{
		return getIncomingPropertyAs(OWLS.Profile.hasInput, Profile.class);
	}

	/* @see impl.owl.WrappedIndividual#toString() */
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		Object o = getLabel(null);
		if (o != null) str.append(o.toString());
		o = getParamType();
		if (o != null) str.append(" ").append(o.toString());
		o = getURI();
		if (o != null) str.append(" ").append(o.toString());
		return str.toString();
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#getConstantValue() */
	public OWLValue getConstantValue()
	{
		OWLDataValue dataValue = getProperty(OWLS.Process.parameterValue);
		if (dataValue != null)
		{
			OWLType paramType = getParamType();
			OWLValue owlValue = null;
			if ((paramType == null) || paramType.isDataType()) owlValue = dataValue;
			else
			{
				String rdf = RDFUtils.addRDFTag(dataValue.getLexicalValue());
				owlValue = getOntology().parseLiteral(rdf);
			}

			return owlValue;
		}

		return null;
	}

	/* @see org.mindswap.owls.process.variable.ProcessVar#setConstantValue(org.mindswap.owl.OWLValue) */
	public void setConstantValue(final OWLValue value)
	{
		if (value.isDataValue()) setProperty(OWLS.Process.parameterValue, (OWLDataValue) value);
		else setProperty(OWLS.Process.parameterValue, RDFUtils.toXMLLiteral((OWLIndividual) value));
	}

}
