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


package edu.utep.trustlab.visko.util;

import java.util.Vector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ResultSetToVector {
	public static Vector<String> getVectorFromResultSet(ResultSet rs, String variableName) {
		Vector<String> vector = new Vector<String>();

		if (rs == null) {
			System.out.println("The result set is null.");
		}
		else if (rs.getResultVars().contains(variableName)) {
			while (rs.hasNext()) {
				RDFNode node = rs.next().get("?" + variableName);
				if (node != null){
					vector.add(node.toString());
				}
				else
					System.out.println("Value of variable is null. Variable: "
							+ variableName);
			}
		}
		else
			System.out.println("Variable: " + variableName + " is not part the input ResultSet!");

		return vector;
	}

	public static Vector<String[]> getVectorPairsFromResultSet(ResultSet rs,
			String variableName1, String variableName2) {
		Vector<String[]> vector = new Vector<String[]>();

		if (rs == null) {
			System.out.println("The result set is null.");
		}

		else if (rs.getResultVars().contains(variableName1)
				&& rs.getResultVars().contains(variableName2)) {
			RDFNode node1;
			RDFNode node2;
			QuerySolution solution;
			while (rs.hasNext()) {
				solution = rs.next();
				node1 = solution.get("?" + variableName1);
				node2 = solution.get("?" + variableName2);

				if (node1 != null && node2 != null)
					vector.add(new String[] { node1.toString(),
							node2.toString() });
				else if (node1 == null)
					System.out.println("Value of variable is null. Variable: "
							+ variableName1);
				else
					System.out.println("Value of variable is null. Variable: "
							+ variableName2);
			}
		} else if (!rs.getResultVars().contains(variableName1))
			System.out.println("Variable: " + variableName1
					+ " is not part the input ResultSet!");
		else
			System.out.println("Variable: " + variableName2
					+ " is not part the input ResultSet!");

		return vector;
	}

	public static Vector<String[]> getVectorTriplesFromResultSet(ResultSet rs,
			String variableName1, String variableName2, String variableName3) {
		Vector<String[]> vector = new Vector<String[]>();

		if (rs == null) {
			System.out.println("The result set is null.");
		}

		else if (rs.getResultVars().contains(variableName1)
				&& rs.getResultVars().contains(variableName2)
				&& rs.getResultVars().contains(variableName3)) {
			RDFNode node1;
			RDFNode node2;
			RDFNode node3;

			QuerySolution solution;
			while (rs.hasNext()) {
				solution = rs.next();
				node1 = solution.get("?" + variableName1);
				node2 = solution.get("?" + variableName2);
				node3 = solution.get("?" + variableName3);

				if (node1 != null && node2 != null && node3 != null)
					vector.add(new String[] { node1.toString(),
							node2.toString(), node3.toString() });
				if (node1 == null)
					System.out.println("Value of variable is null. Variable: "
							+ variableName1);
				if (node2 == null)
					System.out.println("Value of variable is null. Variable: "
							+ variableName2);
				if (node3 == null)
					System.out.println("Value of variable is null. Variable: "
							+ variableName3);
			}
		} else if (!rs.getResultVars().contains(variableName1))
			System.out.println("Variable: " + variableName1
					+ " is not part the input ResultSet!");
		else if (!rs.getResultVars().contains(variableName2))
			System.out.println("Variable: " + variableName2
					+ " is not part the input ResultSet!");
		else
			System.out.println("Variable: " + variableName3
					+ " is not part of the input ResultSet!");
		return vector;
	}
}
