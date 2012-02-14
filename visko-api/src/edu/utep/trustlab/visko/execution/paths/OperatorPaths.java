package edu.utep.trustlab.visko.execution.paths;

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
					Vector<String> viewURIs = ResultSetToVector
							.getVectorFromResultSet(views, "view");
					matchesAView = false;

					for (String viewURI : viewURIs) {
						matchesAView = matchesAView
								|| requiredViewURI.equals(viewURI);
					}

					maintainsViewRestriction = maintainsViewRestriction
							|| matchesAView;
				}

				if (!maintainsViewRestriction)
					badOperatorPaths.add(aPath);
			}
		}
		removeAll(badOperatorPaths);
	}

	/*
	 * public void filterByType(String requiredTypeURI, ViskoTripleStore ts) {
	 * boolean maintainsTypeRestriction; boolean matchesAType;
	 * 
	 * OperatorPaths badOperatorPaths = new OperatorPaths(); OperatorPath aPath;
	 * 
	 * for(int i = 0; i < size(); i ++) { maintainsTypeRestriction = true; aPath
	 * = this.get(i);
	 * 
	 * if(aPath.size() > 0) { for(String operatorURI : aPath) { ResultSet types
	 * = ts.getTypesOperatedOn(operatorURI); Vector<String> typeURIs =
	 * ResultSetToVector.getVectorFromResultSet(types, "type");
	 * 
	 * matchesAType = false;
	 * 
	 * for(String typeURI : typeURIs) {matchesAType = matchesAType ||
	 * typeURI.equals(requiredTypeURI +
	 * "^^http://www.w3.org/2001/XMLSchema#anyURI") ||
	 * typeURI.equals(ViskoS.URI_ANYTYPE +
	 * "^^http://www.w3.org/2001/XMLSchema#anyURI");}
	 * 
	 * maintainsTypeRestriction = maintainsTypeRestriction && matchesAType; }
	 * 
	 * if(!maintainsTypeRestriction) badOperatorPaths.add(aPath); } }
	 * removeAll(badOperatorPaths); }
	 */
}
