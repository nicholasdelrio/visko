package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

	public String gsn_csm_contour_map(
			@WebParam(name="url") String url,
			@WebParam(name="plotVariable") String plotVariable,
			@WebParam(name="font") String font,
			@WebParam(name="lbOrientation") String lbOrientation, 
			@WebParam(name="cnLevelSpacingF") String cnLevelSpacingF,
			@WebParam(name="colorTable") String colorTable,
			@WebParam(name="cnLinesOn") String cnLinesOn,
			@WebParam(name="cnFillOn") String cnFillOn,
			@WebParam(name="latVariable") String latVariable,
			@WebParam(name="lonVariable") String lonVariable,
			@WebParam(name="indexOfX") String indexOfX,
			@WebParam(name="indexOfY") String indexOfY,
			@WebParam(name="indexOfZ") String indexOfZ

		)
	{
		Gsn_csm_contour_map service = new Gsn_csm_contour_map(url);
		return service.transform(plotVariable, font, lbOrientation, cnLevelSpacingF, colorTable, cnLinesOn, cnFillOn, latVariable, lonVariable, indexOfX, indexOfY, indexOfZ);
	}
	
	public String gsn_csm_contour_map_raster(
			@WebParam(name="url") String url,
			@WebParam(name="plotVariable") String plotVariable,
			@WebParam(name="font") String font,
			@WebParam(name="lbOrientation") String lbOrientation, 
			@WebParam(name="colorTable") String colorTable,
			@WebParam(name="latVariable") String latVariable,
			@WebParam(name="lonVariable") String lonVariable,
			@WebParam(name="indexOfX") String indexOfX,
			@WebParam(name="indexOfY") String indexOfY,
			@WebParam(name="indexOfZ") String indexOfZ
	)
	{
		Gsn_csm_contour_map_raster service = new Gsn_csm_contour_map_raster(url);
		return service.transform(plotVariable, font, lbOrientation, colorTable, latVariable, lonVariable, indexOfX, indexOfY, indexOfZ);
	}
	
	public String gsn_csm_xy2_time_series(
			@WebParam(name="url") String url,
			@WebParam(name="lPlotVariablesList") String lPlotVariablesList,
			@WebParam(name="rPlotVariablesList") String rPlotVariablesList,
			@WebParam(name="xDimName") String xDimName,
			@WebParam(name="xDimSize") String xDimSize,
			@WebParam(name="title") String title,
			@WebParam(name="yLAxisLabel") String yLAxisLabel,
			@WebParam(name="yRAxisLabel") String yRAxisLabel)
	{
		Gsn_csm_xy2_time_series service = new Gsn_csm_xy2_time_series(url);
		return service.transform(lPlotVariablesList, rPlotVariablesList, xDimName, xDimSize, title, yLAxisLabel, yRAxisLabel);
	}
	
}
