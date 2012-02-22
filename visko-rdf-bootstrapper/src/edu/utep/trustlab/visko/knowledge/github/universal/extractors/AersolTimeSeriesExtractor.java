package edu.utep.trustlab.visko.knowledge.github.universal.extractors;


import edu.utep.trustlab.repository.Repository;
import edu.utep.trustlab.visko.knowledge.NickConfigurations;
import edu.utep.trustlab.visko.ontology.service.writer.ExtractorWriter;

public class AersolTimeSeriesExtractor {

	public static void main(String[] args) {
		Repository.setRepository(NickConfigurations.getCIServer());
		String dumpURL;
		ExtractorWriter extractor = new ExtractorWriter(
				"AerosolTimeSeriesExtractor");
		extractor
				.addDataType("http://giovanni.gsfc.nasa.gov/data/aerosol/timeseries.owl#timeseries");
		extractor
				.setFormat("http://rio.cs.utep.edu/ciserver/ciprojects/pmlp/NETCDF.owl#NETCDF");
		extractor
				.setProfileURI("http://rio.cs.utep.edu/ciserver/ciprojects/visko/timeSeriesProfile.owl#timeSeriesProfile");
		dumpURL = extractor.saveDocument();
		System.out.println(dumpURL);
	}
}
