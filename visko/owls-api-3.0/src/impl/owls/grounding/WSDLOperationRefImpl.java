/*
 * Created on Dec 21, 2004
 */
package impl.owls.grounding;

import impl.owl.WrappedIndividual;

import java.net.URI;

import org.mindswap.owl.OWLIndividual;
import org.mindswap.owls.grounding.WSDLOperationRef;
import org.mindswap.owls.vocabulary.OWLS;

/**
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public class WSDLOperationRefImpl extends WrappedIndividual implements WSDLOperationRef {
	public WSDLOperationRefImpl(final OWLIndividual ind) {
		super(ind);
	}

	public void setOperation(final URI op) {
		setProperty(OWLS.Grounding.operation, op);
	}

	public URI getOperation() {
		return getPropertyAsURI(OWLS.Grounding.operation);
	}

	public void setPortType(final URI port) {
		setProperty(OWLS.Grounding.portType, port);
	}

	public URI getPortType() {
		return getPropertyAsURI(OWLS.Grounding.portType);
	}

	public void removeOperation() {
		if (hasProperty(OWLS.Grounding.wsdlOperation))
			removeProperty(OWLS.Grounding.wsdlOperation, null);
	}

	public void removePortType() {
		if (hasProperty(OWLS.Grounding.portType))
			removeProperty(OWLS.Grounding.portType, null);
	}

	@Override
	public void delete() {
		removeOperation();
		removePortType();
		super.delete();
	}
}
