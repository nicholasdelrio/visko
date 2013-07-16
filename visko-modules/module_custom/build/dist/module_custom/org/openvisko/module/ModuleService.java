package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

	// custom Services
	public String sPARQLResultsToJSONGraph(
			@WebParam(name = "url") String url,
			@WebParam(name = "subjectVariable") String subjectVariable,
			@WebParam(name = "predicateVariable") String predicateVariable,
			@WebParam(name = "objectVariable") String objectVariable){
		SPARQLResultsToJSONGraph service = new SPARQLResultsToJSONGraph(url);
		return service.transform(subjectVariable, predicateVariable, objectVariable);
	}

	public String jsonBars_Instances(){
		JSONBars_Instances service = new JSONBars_Instances();
		return service.transform();
	}
	
	public String jsonGraph_DataTransformations(){
		JSONGraph_DataTransformations service = new JSONGraph_DataTransformations();
		return service.transform();
	}
	
	public String jsonGraph_OperatorPaths(){
		JSONGraph_OperatorPaths service = new JSONGraph_OperatorPaths();
		return service.transform();
	}
	
	public String csv2tabular(@WebParam(name = "url") String url){
		CSVToTabularASCII service = new CSVToTabularASCII(url);
		return service.transform();
	}
	
	public String XYZDataFieldFilter(
			@WebParam(name = "url") String url,
			@WebParam(name = "indexOfX") String indexOfX,
			@WebParam(name = "indexOfY") String indexOfY,
			@WebParam(name = "indexOfZ") String indexOfZ){
		XYZDataFieldFilter transformer = new XYZDataFieldFilter(url);
		return transformer.transform(indexOfX, indexOfY, indexOfZ);
	}
	
	public String int2Short(@WebParam(name="url") String url)
	{
		Int2Short transformer = new Int2Short(url);
		return transformer.transform();
	}
	
	public String float2ShortThr(
			@WebParam(name="url") String url,
			@WebParam(name="scalingFactor") String scalingFactor,
			@WebParam(name="offset") String offset)
	{
		Float2ShortThr transformer = new Float2ShortThr(url);
		return transformer.transform(scalingFactor, offset);
	}
	

}
