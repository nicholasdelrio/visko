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


package edu.utep.trustlab.visko.planning.paths;

import java.util.*;

import edu.utep.trustlab.visko.sparql.ViskoTripleStore;
import edu.utep.trustlab.visko.util.ResultSetToVector;

import com.hp.hpl.jena.query.ResultSet;

public class OperatorPaths extends Vector<OperatorPath> {
	public boolean add(OperatorPath path) {
		if (!isExistingPath(path)) {
			return super.add(path);
		}

		return false;
	}

	private boolean isExistingPath(OperatorPath path) {
		for (OperatorPath aPath : this) {
			if (aPath.toString().equals(path.toString()))
				return true;
		}
		return false;
	}

	/*
	public void filterByView(String requiredViewURI, ViskoTripleStore ts) {
		boolean maintainsViewRestriction;
		boolean matchesAView;

		OperatorPaths badOperatorPaths = new OperatorPaths();
		OperatorPath aPath;

		for (int i = 0; i < size(); i++) {
			maintainsViewRestriction = false;
			aPath = this.get(i);

			if (aPath.size() > 0) {
				for (String operatorURI : aPath) {
					ResultSet views = ts.getViewsGeneratedFrom(operatorURI);
					Vector<String> viewURIs = ResultSetToVector.getVectorFromResultSet(views, "view");
					matchesAView = false;

					for (String viewURI : viewURIs) {
						matchesAView = matchesAView || requiredViewURI.equals(viewURI);
					}

					maintainsViewRestriction = maintainsViewRestriction || matchesAView;
				}

				if (!maintainsViewRestriction)
					badOperatorPaths.add(aPath);
			}
		}
		removeAll(badOperatorPaths);
	}*/
	
	public void filterByView(String requiredViewURI, ViskoTripleStore ts) {
		boolean maintainsViewRestriction;
		boolean matchesAView;

		OperatorPaths badOperatorPaths = new OperatorPaths();
		OperatorPath aPath;
		
		for (int i = 0; i < size(); i++) {
			maintainsViewRestriction = false;
			aPath = this.get(i);

			if (aPath.size() > 0) {
				for (int j = aPath.size() - 1; j >= 0; j --) {
					
					String operatorURI = aPath.get(j);
					
					ResultSet dataTypeResultSet = ts.getTransformedToDataType(operatorURI);
					Vector<String> dataTypes = ResultSetToVector.getVectorFromResultSet(dataTypeResultSet, "dataType");
					
					String dataTypeURI = dataTypes.firstElement();
					
					matchesAView = false;

					matchesAView = matchesAView || requiredViewURI.equals(dataTypeURI);

					maintainsViewRestriction = maintainsViewRestriction || matchesAView;
				}

				if (!maintainsViewRestriction)
					badOperatorPaths.add(aPath);
			}
		}
		removeAll(badOperatorPaths);
	}


	public void filterByType(String inputDataType, ViskoTripleStore ts) {
		boolean canBeOperatedOn = true;

		OperatorPaths badOperatorPaths = new OperatorPaths();
		OperatorPath aPath;
		
		for (int i = 0; i < size(); i++) {
		
			String inDataType = inputDataType;
			
			aPath = this.get(i);

			if (aPath.size() > 0) {
				for (String operatorURI : aPath) {
										
					canBeOperatedOn = ts.canOperateOnDataType(operatorURI, inDataType) || ts.canOperateOnSuperTypeOfDataType(operatorURI, inDataType);
					
					System.out.println("current in datatype: " + inDataType);
					
					if(!canBeOperatedOn){
						System.out.println(inDataType + " can't be consumed by: " + operatorURI);
						break;
					}
					
					ResultSet dataTypeResultSet = ts.getTransformedToDataType(operatorURI);
					Vector<String> dataTypes = ResultSetToVector.getVectorFromResultSet(dataTypeResultSet, "dataType");
					
					inDataType = dataTypes.firstElement();
				}
				
				boolean viewerCanOperateOn = ts.canOperateOnDataType(aPath.getViewerURI(), inDataType) || ts.canOperateOnSuperTypeOfDataType(aPath.getViewerURI(), inDataType);
				boolean maintainsTypeRestriction = canBeOperatedOn && viewerCanOperateOn;
				
				if (!maintainsTypeRestriction)
					badOperatorPaths.add(aPath);
			}
		}
		removeAll(badOperatorPaths);
	}
}
