package edu.utep.trustlab.visko.util;

import java.io.File;

import edu.utep.trustlab.contentManagement.ContentManager;
import edu.utep.trustlab.contentManagement.LocalFileSystem;
import edu.utep.trustlab.visko.execution.PipelineExecutor;
import edu.utep.trustlab.visko.execution.PipelineExecutorJob;
import edu.utep.trustlab.visko.planning.pipelines.*;
import edu.utep.trustlab.visko.planning.Query;
import edu.utep.trustlab.visko.planning.QueryEngine;
import edu.utep.trustlab.visko.sparql.SPARQL_EndpointFactory;

public class SystemLevelDriver {

	public static void main(String[] args) {

		String queryFile = args[0];
		int maxPipelines = Integer.valueOf(args[1]);
		String provDumpDir = args[2];
		String tripleStoreDir = args[3];

		SPARQL_EndpointFactory.setUpEndpointConnection(tripleStoreDir);

		if (!provDumpDir.endsWith("/"))
			provDumpDir = provDumpDir + "/";

		File dumpDirectory = new File(provDumpDir);
		String fullDumpDirectory = dumpDirectory.getAbsolutePath();
		fullDumpDirectory = fullDumpDirectory.replaceAll("\\\\", "/");

		String provURIBase;
		if (fullDumpDirectory.startsWith("c:"))
			provURIBase = fullDumpDirectory.replaceAll("c:", "file://");
		else if (fullDumpDirectory.startsWith("C:"))
			provURIBase = fullDumpDirectory.replaceAll("C:", "file://");
		else
			provURIBase = "file://" + provDumpDir;

		System.out.println("prov base URI: " + provURIBase);

		LocalFileSystem fs = new LocalFileSystem(provURIBase, provDumpDir);
		ContentManager.setWorkspacePath(provDumpDir);
		ContentManager.setProvenanceContentManager(fs);

		String queryContents = FileUtils.readTextFile(queryFile);

		Query query = new Query(queryContents);

		QueryEngine engine = new QueryEngine(query);

		PipelineSet pipes = engine.getPipelines();
		for (Pipeline pipe : pipes)
			System.out.println(pipe);

		PipelineExecutor executor = new PipelineExecutor();

		for (int i = 0; i < pipes.size() && i < maxPipelines; i++) {
			System.out.println(pipes.firstElement());

			PipelineExecutorJob job = new PipelineExecutorJob(pipes.get(i));
			job.setAsSimulatedJob();
			job.setProvenanceLogging(true);
			
			executor.setJob(job);

			executor.process();

			while (executor.isAlive()) {
			}

			System.out.println("Final Result = " + job.getFinalResultURL());
			System.out.println("PML Query URI: " + job.getPMLQueryURI());
			System.out.println("PML Query URI: " + job.getProvQueryURI());
		}
	}
}
