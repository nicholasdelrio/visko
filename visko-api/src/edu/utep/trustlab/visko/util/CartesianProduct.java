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
