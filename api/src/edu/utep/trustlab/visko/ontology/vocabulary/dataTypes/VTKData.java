/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.ontology.vocabulary.dataTypes;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import edu.utep.trustlab.visko.ontology.vocabulary.Visko;

public class VTKData {

	// Classes
	public static final String CLASS_URI_vtkDataObject = Visko.DATA_VTK + "#vtkDataObject";
	public static final String CLASS_URI_vtkArrayData = Visko.DATA_VTK + "#vtkArrayData";
	public static final String CLASS_URI_vtkDataSet = Visko.DATA_VTK + "#vtkDataSet";
	public static final String CLASS_URI_vtkImageDataFloats = Visko.DATA_VTK + "#vtkImageDataFloats";
	public static final String CLASS_URI_vtkImageDataShortIntegers = Visko.DATA_VTK + "#vtkImageDataShortIntegers";
	public static final String CLASS_URI_vtkImageData = Visko.DATA_VTK + "#vtkImageData";
	public static final String CLASS_URI_vtkPointSet = Visko.DATA_VTK + "#vtkPointSet";
	public static final String CLASS_URI_vtkPath = Visko.DATA_VTK + "#vtkPath";
	public static final String CLASS_URI_vtkPolyData = Visko.DATA_VTK + "#vtkPolyData";
	public static final String CLASS_URI_vtkStructuredGrid = Visko.DATA_VTK + "#vtkStructuredGrid";
	public static final String CLASS_URI_vtkUnstructuredGrid = Visko.DATA_VTK + "#vtkUnstructuredGrid";
	public static final String CLASS_URI_vtkGraph = Visko.DATA_VTK + "#vtkGraph";
	public static final String CLASS_URI_vtkUndirectedGraph = Visko.DATA_VTK + "#vtkUndirectedGraph";
	public static final String CLASS_URI_vtkDirectedGraph = Visko.DATA_VTK + "#vtkDirectedGraph";
	
	private static OntModel model;
	private static Ontology ontology;

	static {
		model = ModelFactory.createOntologyModel();
		model.read(Visko.DATA_VTK);
		ontology = model.getOntology(Visko.DATA_VTK);
	}

	public static OntModel getModel() {
		return model;
	}

	public static Ontology getOntology() {
		return ontology;
	}
}