package trustlab.visko.execution;

import java.util.HashMap;
import java.util.Vector;
import trustlab.visko.ontology.model.OWLSModel;
import trustlab.visko.ontology.model.ViskoModel;
import trustlab.visko.ontology.operator.Viewer;
import trustlab.visko.ontology.service.OWLSService;

public class Pipeline extends Vector<OWLSService> {
	private Viewer viewer;
	private OWLSModel owlsLoadingModel;
	private ViskoModel viskoLoadingModel;
	private PipelineSet parentContainer;

	public Pipeline(String viewerURI, PipelineSet parent) {
		super();
		viskoLoadingModel = new ViskoModel();
		owlsLoadingModel = new OWLSModel();
		parentContainer = parent;
		viewer = new Viewer(viewerURI, viskoLoadingModel);
	}

	public Viewer getViewer() {
		return viewer;
	}

	public HashMap<String, String> getParameterBindings() {
		return parentContainer.getParameterBindings();
	}

	public String getArtfifactURL() {
		return parentContainer.getArtifactURL();
	}

	public void addOWLSServiceURI(String serviceURI) {
		add(new OWLSService(serviceURI, owlsLoadingModel));
	}

	public void setOWLSServiceURIs(Vector<String> serviceURIs) {
		for (String serviceImplURI : serviceURIs) {
			addOWLSServiceURI(serviceImplURI);
		}
	}

	public String executePath(boolean provenance) {
		PipelineExecutor executor = new PipelineExecutor(provenance);

		String artifactURL = parentContainer.getArtifactURL();

		String visualizationURL = "NULL: Artifact to be visualized was never specified!!!";

		if (artifactURL != null) {
			// null object behavior
			if (size() == 0) {
				visualizationURL = parentContainer.getArtifactURL();
			}

			try {
				visualizationURL = executor.executeServiceChain(this,
						parentContainer.getArtifactURL());
			} catch (Exception e) {
				e.printStackTrace();
				visualizationURL = e.getMessage();
			}
		}

		return visualizationURL;
	}
}
