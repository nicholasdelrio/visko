package org.openvisko.module;

import javax.jws.WebParam;

public class ModuleService {

	public String surfacePlotter(
				@WebParam(name="url") String url,
				@WebParam(name="plotWidth") String plotWidth,
				@WebParam(name="polygonMultiplier") String polygonMultiplier,					
				@WebParam(name="showWireframe") String showWireframe,
				@WebParam(name="showGrayscale") String showGrayscale,
				@WebParam(name="showAxis") String showAxis,
				@WebParam(name="whiteBackground") String whiteBackground,
				@WebParam(name="blackFill") String blackFill,
				@WebParam(name="smooth") String smooth) {

		SurfacePlotterService service = new SurfacePlotterService(url);
		return service.transform(plotWidth, polygonMultiplier, showWireframe, showGrayscale,
				     	showAxis, whiteBackground, blackFill, smooth);
	}

}
