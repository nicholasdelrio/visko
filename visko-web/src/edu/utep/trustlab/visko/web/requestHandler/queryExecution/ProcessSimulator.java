package edu.utep.trustlab.visko.web.requestHandler.queryExecution;

/**
 * The purpose of this class is to simulate a long running
 * process.  In real life, this could be a stored procedure in a database that
 * generates a huge report, a webservice call to a legacy system, or anything
 * that would take longer than a typical web hit. 
 * The code in this class is not really the focus of this demonstration.
 * Rather, you should be focusing on how the servlet and JSP work together to
 * manage the experience for the end user without leaving them sitting at blank 
 * browser window, trying to figure out if their browser/connection is hung or 
 * if your code is just taking a long time.
 * <br />
 * NOTE: If you're running an OS with the tail program (or if you have a text 
 *       editor that is capable of automatically updating a file when it changes,
 *       you can watch this process in action by tailing (with the -f option) the
 *       catalina.out file while this app is running.
 */
public class ProcessSimulator implements Runnable{
    
    // -------------------------------------------------------------- Constants
    static final int SECOND = 1000;

    // ----------------------------------------------------- Instance Variables
    private int     processTime   = 20;
    private boolean complete      = false;
    private boolean running       = false;
    private String  statusMessage = "Process has not begun";

    // ----------------------------------------------------------- Constructors
    /**
     * Creates a new ProcessSimulator object.
     */
    public ProcessSimulator(){
        // make sure the container can kill this process during app restarts or
        // container shutdown.
        System.out.println("New Process Created");
    }

    /**
     * Creates a new ProcessSimulator object.
     * @param processTime The amount of time this simulated process will take 
     *        in seconds.
     */
    public ProcessSimulator(int processTime){
        this();
        this.setProcessTime(processTime);
    }


    // ----------------------------------------------------------- Core methods

    /**
     * Hides threading details from the web app.
     */
    public void process(){
        if(!isRunning()){
            this.statusMessage = "Starting process";
            try{
                Thread t = new Thread(this);
                t.setDaemon(true);
                t.start();

                //
                // don't return until the new thread is running.
                //
                while(false == this.isRunning()){}
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Triggers the long running process.
     */
    public void run(){
        System.out.println("Running process");
        try{
            this.running = true;
            this.complete = false;
            this.statusMessage = "0  of " 
                               + this.processTime 
                               + " seconds completed";
            for(int i = 0; i < this.processTime; i++){
                Thread.sleep(1 * SECOND);
                this.statusMessage = (i + 1)
                                   + " of " 
                                   + this.processTime 
                                   + " seconds completed";
                System.out.println("STATUS: " + this.statusMessage);
            }
            this.statusMessage = "Process Complete";
            this.complete = true;
            this.running  = false;
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }


    // ------------------------------------------------------- Property Methods

    /**
     * The time it will take for this simulated process to complete in seconds.
     * @return processTime
     */
    public int getProcessTime(){
        return this.processTime;
    }

    /**
     * The time it will take for this simulated process to complete in seconds.
     * @param processTime
     */
    public void setProcessTime(int processTime){
        this.processTime = processTime;
    }

    /**
     * Read only property that returns the status of the long running process.
     * @return true if this process has finished running.
     */
    public boolean isComplete(){
        return this.complete;
    }

    /**
     * Read only property that returns the status of the long running process.
     * @return true if this process has started running.
     */
    public boolean isRunning(){
        return this.running;
    }

    /**
     * Read only property that returns a description of the status of this long 
     * running process.
     * @return A status message.
     */
    public String getStatusMessage(){
        return this.statusMessage;
    }

    // ----------------------------------------------------------- Test methods
    /**
     * Main method for testing and debugging only.
     * NOTE: Again, one of the strengths of MVC is that it allows us to 
     *       build components that can be tested outside of the app server
     *       environment before moving forward with actual web development.
     * Call from the command line with the first and only arg being the number
     * of seconds this process will take.
     */
     public static void main(String[] args){
        int time = 0;
        try{
            time = Integer.parseInt(args[0]);
        }catch(Exception e){
            System.out.println(
                "Usage: "
                + "java us.souther.notsosimple.longrunningprocess.ProcessSimulator "
                + "<process time in seconds>");
        }

        try{
            ProcessSimulator sim = new ProcessSimulator(time);
            sim.process();

            //
            // Check the status every 5 seconds
            //
            while(!sim.isComplete()){
                Thread.sleep(5 * SECOND);
                System.out.println(sim.getStatusMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
     }
}
