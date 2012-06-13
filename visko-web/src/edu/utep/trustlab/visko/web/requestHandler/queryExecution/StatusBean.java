package edu.utep.trustlab.visko.web.requestHandler.queryExecution;
/**
 * Bean for passing status values from the servlet to the JSP.
 * Putting all of these values in a bean instead of storing 
 * them in requestAttributes is not only cleaner but allows us
 * to interpret some of the data and prevent the need for branching
 * in the JSP by providing some readonly properties.
 */
public class StatusBean{

    //------------------------------------------------------ Instance Variables
    private String  message        = ""; 
    private boolean processRunning = false;
    private int     refreshRate    = 5;

    /**
     * @return the status message from the long running process.
     */
    public String getMessage(){
        return this.message;
    }    
    
    /**
     * @param message The status message from the long running process.
     */
    public void setMessage(String message){
        this.message = message;
    }


    /**
     * @return true if the process is still running.
     */
    public boolean isProcessRunning(){
        return this.processRunning;
    }

    /**
     * @param processRunning true if the process is still running.
     */
    public void setProcessRunning(boolean processRunning){
        this.processRunning = processRunning;
    }


    /**
     * Readonly property for JSP developers to use for 
     * conditionally showing the form that 
     * kicks off the long running process.
     * Eliminates the need to put branching code (scriptlet,
     * JSTL, or otherwise) in the JSP without having to write a custom tag.
     * @return Either " visible " or " hidden " depending on the 
     *         status of the running process.
     */
    public String getIsStartFormVisible(){
        return isProcessRunning() ? " hidden " : " visible ";
    }
    

    /**
     * @return refreshRate
     */
    public int getRefreshRate(){
        return this.refreshRate;
    }

    /**
     * Enables the JSP developer to change the refresh rate of the
     * META Refresh tag without having to recompile any Java code.
     * @param refreshRate
     */
    public void setRefreshRate(int refreshRate){
        this.refreshRate = refreshRate;
    }

    /**
     * Returns a refresh tag to the JSP.
     * By writing a small (non look and feel) amount of HTML 
     * and presenting it as a readonly property that can be accessed
     * by the JSP, we have eliminated the need to put branching code
     * in the JSP.
     * @return Either a fully functional meta refresh tag or 
     *         an empty string if the process isn't running.
     */
    public String getRefreshTag(){
        if(isProcessRunning())
            return "<meta http-equiv=\"refresh\""
                   + " content=\""
                   + getRefreshRate()
                   + "\";URL=\"\">";

        return "";
    }
}