package trustlab.visko.knowledge.universal.extractors;


import trustlab.server.Server;
import trustlab.visko.knowledge.NickCIServer;
import trustlab.visko.ontology.service.writer.ExtractorWriter;

public class AersolTimeSeriesExtractor {

	public static void main(String[] args) {
		Server.setServer(NickCIServer.getServer());
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
