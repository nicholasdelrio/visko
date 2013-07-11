/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.web.requestHandler.queryExecutionService;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import edu.utep.trustlab.visko.web.context.ContextListener;
import edu.utep.trustlab.visko.web.requestHandler.RequestHandlerURL;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.planning.pipelines.Pipeline;
import edu.utep.trustlab.visko.planning.pipelines.PipelineSet;

public class IFeelLuckyServlet extends RequestHandlerURL {

	private URL getErrorURL(){
		try{return new URL(ContextListener.serverBaseURL + "visko-web/empty-visualization/null-result.jpg");}
		catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	private URL getURL(String url){
		try{return new URL(url);}
		catch(Exception e){e.printStackTrace();}
		return null;

	}
	
	public URL doGet(HttpServletRequest request){
		// TODO Auto-generated method stub

		String stringQuery = request.getParameter("query");
		URL resultURL = getErrorURL();
		
		Query query;
		
		if (stringQuery != null) {
			query = new Query(stringQuery);
			
			PipelineSet pipelines = getPipelines(query);

			for(Pipeline pipe : pipelines){
				
				if(pipe.hasAllInputParameters()){
					PipelineExecutor executor = new PipelineExecutor();
					
					PipelineExecutorJob job = new PipelineExecutorJob(pipelines.firstElement());
					job.setProvenanceLogging(false);
						
					executor.setJob(job);
					executor.process();
						
					while(executor.isAlive());
					
					String finalResultURL = job.getFinalResultURL();
					if(finalResultURL != null)
						resultURL = getURL(finalResultURL);
					break;
				}
			}
		}
		return resultURL;
	}
	
	public PipelineSet getPipelines(Query query){
		if (query.isValidQuery()) {
			QueryEngine engine = new QueryEngine(query);
			PipelineSet pipelines = engine.getPipelines();
			return pipelines;
		}
		else
			return null;
	}
}