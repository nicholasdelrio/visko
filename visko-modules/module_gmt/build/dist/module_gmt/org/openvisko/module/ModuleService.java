package org.openvisko.module;

import javax.jws.WebParam;


public class ModuleService {

	public String grd2xyz(
			@WebParam(name="url") String url,
			@WebParam(name="N")String N){
		Grd2xyz service = new Grd2xyz(url);
		return service.transform(N);
	}

	public String grd2xyz_esri(
			@WebParam(name="url") String url,
			@WebParam(name="N")String N){
		Grd2xyz_esri service = new Grd2xyz_esri(url);
		return service.transform(N);
	}
	
	public String psxyz(
			@WebParam(name="url") String url,
			@WebParam(name="B") String B,
			@WebParam(name="J") String J,
			@WebParam(name="JZ") String JZ,
			@WebParam(name="R") String R,
			@WebParam(name="E") String E,
			@WebParam(name="S") String S,
			@WebParam(name="W") String W,
			@WebParam(name="G") String G
			){
		Psxyz service = new Psxyz(url);
		return service.transform(B, J, JZ, R, E, S, W, G);
	}
	
	public String grdimage(
			@WebParam(name="url") String url,
			@WebParam(name="C")String C,
			@WebParam(name="J")String J,
			@WebParam(name="B")String B,
			@WebParam(name="T")String T,
			@WebParam(name="R")String R){
		
		Grdimage service = new Grdimage(url);
		return service.transform(C, J, B, T, R);
	}

	public String psxy(
			@WebParam(name="url")String url,
			@WebParam(name="S")String S,
			@WebParam(name="J")String J,
			@WebParam(name="G")String G,
			@WebParam(name="B")String B,
			@WebParam(name="R")String R,
			@WebParam(name="indexOfX")String indexOfX,
			@WebParam(name="indexOfY")String indexOfY
			){
		Psxy service = new Psxy(url);
		return service.transform(S, J, G, B, R, indexOfX, indexOfY);
	}
	
	public String grdcontour(
			@WebParam(name="url")String url,
			@WebParam(name="C")String C,
			@WebParam(name="A")String A,
			@WebParam(name="J")String J,
			@WebParam(name="S")String S,
			@WebParam(name="B")String B,
			@WebParam(name="Wa")String Wa,
			@WebParam(name="Wc")String Wc){
		Grdcontour service = new Grdcontour(url);
		return service.transform(C, A, J, S, B, Wa, Wc);
	}
	
	public String surface(
			@WebParam(name="url")String url,
			@WebParam(name="I")String I,
			@WebParam(name="T")String T,
			@WebParam(name="C")String C,
			@WebParam(name="R")String R){
		Surface service = new Surface(url);
		return service.transform(I, T, C, R);
	}
	
	public String nearneighbor(
			@WebParam(name="url")String url,
			@WebParam(name="I")String I,
			@WebParam(name="S")String S,
			@WebParam(name="R")String R){
		Nearneighbor service = new Nearneighbor(url);
		return service.transform(I, S, R);
	}
	

}
