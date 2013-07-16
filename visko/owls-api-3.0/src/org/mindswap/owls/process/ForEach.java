/*
 * Created on Jan 4, 2005
 */
package org.mindswap.owls.process;

import org.mindswap.owls.process.variable.Loc;
import org.mindswap.owls.process.variable.Parameter;
import org.mindswap.owls.process.variable.ValueOf;


/**
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public interface ForEach extends Iterate
{
    public Loc getLoopVar();
    public void setLoopVar(Loc var);

    public ValueOf getListValue();
    public void setListValue(ValueOf value);

    public void setListValue(Perform perform, Parameter parameter);
}
