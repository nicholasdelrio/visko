package edu.utep.trustlab.visko.web.requestHandler.queryExecution;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Servlet for handling a situation involving some sort of long running process.
 * This demo shows one way of improving the user's experience by not leaving them 
 * sitting at a blank browser screen, wondering if their browser or network connection 
 * is hung or if the process is indeed running.
 * In a nutshell the app will:
 * <ol>
 * <li>
 *  Present the user with a form on a webpage that they can submit to kick off 
 *  the long running process.
 * </li>
 * <li>
 *  Kick off the process (in this case, in a new thread) and immediately return
 *  the user to a page that shows them the status of the process.
 * </li>
 * <li>
 *  Refresh the status page every <i>n</i> seconds to let them know that the
 *  process is still running.  If possible, give the user some idea of how much
 *  longer they will need to wait for the process to complete.
 * </li>
 * <li>
 *  Upon completion of the long running process, let the user know that it is 
 *  complete and display a link or form that allows them to move forward within
 *  the application.
 * </li>
 * </ol>
 * <br />
 * In addition to demonstrating this core functionality, this application shows
 * some techniques that I commonly use to eliminate the need for branching in JSP pages.
 * By placing all of the return data in a scoped bean with code that analyses the data,
 * I was able to provide some readonly properties that the JSP designer can use to conditionally
 * display user controls without the need for scripting code (scriptlets, jstl, or otherwise).
 * <br />
 * It also shows one technique for enforcing the MVC pattern by placing the JSP pages under
 * the WEB-INF directory (which can't be viewed directly from a browser) and creating a servlet mapping
 * that intercepts the request for any jsp pages in the context root and instead, pushes flow
 * to the controller servlet.
 * <br />
 * <b>NOTE:</b> The one thing this code is missing right now is synchronization blocks.
 * If you plan on using any of this code in a production environment (anywhere that counts),
 * you will need to handle this yourself.
 * <br />
 * Another issue is that the processSimulator object is bound to the user's session
 * This means that if the session times out before the process is done the user has
 * no way to monitor it.  A more robust method would be to create a context scoped 
 * (or singleton) process manager that holds a map to all of the running process objects
 * or to manage the state of the running jobs from within a database. Each refresh of the
 * status screen would involve a database query that checks value in a status table.
 * The long running process would update that value when it is done so that the next 
 * query will return a status of 'complete'.
 * <br />
 * Again, this is not meant to be production ready code. 
 * It's a way to demonstrate a pattern for dealing with long jobs from a webapp.
 * @author <a href="mailto:ben@souther.us">Ben Souther</a>
 * @since Fri Aug 19 17:27:37 EDT 2005
 */
public class LongRunningProcessServlet extends HttpServlet{
    
    static final String JSP_PAGE = "/WEB-INF/jsp/long-running-process.jsp";
        
    /**
     * Handles the form post that kicks off the long running process.
     */ 
    public void doPost(HttpServletRequest   request,
                       HttpServletResponse  response)
                  throws ServletException, IOException{

        HttpSession session = request.getSession();

        if("true".equals(request.getParameter("start_process"))){

            //
            // We only want one processSimulator per session.
            //
            ProcessSimulator sim = (ProcessSimulator)session.getAttribute("processSimulator");
            if(null == sim){
                sim = new ProcessSimulator();
                session.setAttribute("processSimulator", sim);
                sim.setProcessTime(30);
            }

            sim.process();
            
            //
            // Use sendRedirect to generate another GET request
            // this keeps someone from re-submitting the form 
            // with the refresh button on their browser.
            // 
            response.sendRedirect("");
            return;
        }
    }


    /**
     * Handles the inital hit and the status check, if the process has 
     * already been kicked off.
     */
    public void doGet(HttpServletRequest   request,
                      HttpServletResponse  response)
                  throws ServletException, IOException{

        HttpSession      session    = request.getSession();
        ProcessSimulator sim        = (ProcessSimulator)session.getAttribute("processSimulator");
        StatusBean       statusBean = new StatusBean();


        //
        // If the ProcessSimulator (sim) object exists, the process is running.
        //
        if(null !=  sim){
            statusBean.setProcessRunning(sim.isRunning());
            statusBean.setMessage(sim.getStatusMessage());

            //
            // Once the process is complete, remove the binding from 
            // the session.
            //
            if(sim.isComplete()){
                session.removeAttribute("processSimulator");
            }
        }

        request.setAttribute("statusBean", statusBean);
        forward(JSP_PAGE, request, response);
        return;
    } 




    /*
     * private method for forwarding to the view.
     */
    private void forward(String              resource,
                         HttpServletRequest  request,
                         HttpServletResponse response)
                     throws ServletException, IOException{
        getServletContext().getRequestDispatcher(resource).forward(request, response);
    }
}
