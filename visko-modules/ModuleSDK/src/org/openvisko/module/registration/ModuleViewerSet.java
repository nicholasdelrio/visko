package org.openvisko.module.registration;

import java.util.ArrayList;

import edu.utep.trustlab.visko.ontology.model.ViskoModel;
import edu.utep.trustlab.visko.ontology.viskoOperator.Viewer;
import edu.utep.trustlab.visko.ontology.viskoOperator.ViewerSet;

public class ModuleViewerSet {
	
	private ViskoModel model;
	private ViewerSet viewerSet;
	private String baseFileURL;
	private ArrayList<Viewer> viewers;
	
	protected ModuleViewerSet(String name, ViskoModel viskoModel, String url){
		model = viskoModel;	
		baseFileURL = url;
		viewers = new ArrayList<Viewer>();
		viewerSet = new ViewerSet(baseFileURL, name, model);
	}
	
	protected void addToModel(){
		for(Viewer viewer : viewers){
			viewer.addBelongsToViewerSet(viewerSet);
			viewer.getIndividual();
		}
		
		viewerSet.getIndividual();
	}
	
	public void setComment(String comment){
		viewerSet.setComment(comment);
	}
	
	public void setLabel(String label){
		viewerSet.setLabel(label);
	}
	
	public Viewer createNewViewer(String name){
		Viewer viewer = new Viewer(baseFileURL, name, model);
		viewer.setName(name);
		viewers.add(viewer);
		return viewer;
	}
}
