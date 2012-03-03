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

public class CartesianProduct {
	public static Vector<Vector<String>> cartesianProduct(
			Vector<Vector<String>> sets, int index) {
		if (sets == null || sets.isEmpty()) {
			System.out
					.println("Null or empty set: cannot perform cartesian product...");
			return null;
		} else if (sets.size() - index == 1) {
			return setToSetOfSets(sets.get(index));
		} else if (sets.size() - index == 2) {
			return cartesianProduct(sets.get(index),
					setToSetOfSets(sets.get(index + 1)));
		}

		else {
			Vector<String> set1 = sets.get(index);
			Vector<Vector<String>> partialProductSet = cartesianProduct(sets,
					index + 1);
			return cartesianProduct(set1, partialProductSet);
		}
	}

	private static Vector<Vector<String>> cartesianProduct(Vector<String> set,
			Vector<Vector<String>> partialProductSets) {
		if (set == null || set.size() == 0) {
			System.out.println("Input Set is empty...");
			return null;
		}
		if (partialProductSets == null || partialProductSets.size() == 0) {
			System.out.println("PartialProductSets input is empty...");
			return null;
		}

		Vector<Vector<String>> productSets = new Vector<Vector<String>>();
		Vector<String> aProductSet;
		for (String element : set) {
			for (Vector<String> aSet : partialProductSets) {
				aProductSet = new Vector<String>();
				aProductSet.add(element);

				for (String anElement : aSet) {
					aProductSet.add(anElement);
				}

				productSets.add(aProductSet);
			}

		}
		return productSets;
	}

	private static Vector<Vector<String>> setToSetOfSets(Vector<String> set) {
		Vector<Vector<String>> partialProductSets = new Vector<Vector<String>>();
		Vector<String> productSet;
		for (String element : set) {
			productSet = new Vector<String>();
			productSet.add(element);
			partialProductSets.add(productSet);
		}
		return partialProductSets;
	}
}
