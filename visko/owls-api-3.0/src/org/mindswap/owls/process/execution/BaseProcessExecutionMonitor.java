package org.mindswap.owls.process.execution;

import org.mindswap.owls.process.Process;

/**
 * This class servers as the base implementation of process monitors.
 * It merely provides methods to get and set the monitoring filter.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:52 $
 */
public abstract class BaseProcessExecutionMonitor implements ProcessExecutionMonitor
{
	protected int monitorFilter;

	public BaseProcessExecutionMonitor()
	{
		monitorFilter = Process.ANY;
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#getMonitorFilter() */
	public int getMonitorFilter()
	{
		return monitorFilter;
	}

	/* @see org.mindswap.owls.process.execution.ProcessExecutionMonitor#setMonitorFilter(int) */
	public void setMonitorFilter(final int processType)
	{
		this.monitorFilter = processType;
	}
}
