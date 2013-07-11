package edu.utep.trustlab.visko.util;

public class ReplaceTextCaller {

	public static void main(String[] args){
		
		ReplaceText rt = new ReplaceText("../resources/formats/","visko-rdf/master/rdf","visko/master/resources");
		rt.replace();
	}
}
