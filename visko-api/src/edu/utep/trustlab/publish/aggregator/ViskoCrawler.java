package edu.utep.trustlab.publish.aggregator;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

import edu.utep.trustlab.visko.util.GetURLContents;
public class ViskoCrawler{
	
	private static String MODEL_NAME = "TDB-VISKO";
	private Model model;
	
	public ViskoCrawler(String storesDirectory){
		model = TDBFactory.createModel(storesDirectory);
	}
	
	public static Document getDocument(String url){
		Document doc;
		try{
			doc = Jsoup.connect(url).get();
		}
		catch(Exception e){
			doc = null;
		}
		return doc;
	}
	
	public void createTripleStore(String url){
		List<String> urlListing = getListingFromIndexPage(url);
		buildTripleStore(urlListing);
	}
	
	private void buildTripleStore(List<String> viskoDocURLs){
		for(String url : viskoDocURLs){
			model.read(url);
		}
	}
	
	private List<String> getListingFromIndexPage(String url){
		
		Document doc = getDocument(url);
		Elements anchors = doc.select("a");
		ArrayList<String> viskoDocumentURLs = new ArrayList<String>();
		String viskoDocURL;
		Element anAnchor;
		for(int i = 1; i < anchors.size(); i ++){
			anAnchor = anchors.get(i);
			viskoDocURL = anAnchor.attr("href");
			viskoDocumentURLs.add(viskoDocURL);
		}
		
		return viskoDocumentURLs;
	}
}
