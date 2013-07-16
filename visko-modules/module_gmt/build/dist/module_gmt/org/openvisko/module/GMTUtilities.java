package org.openvisko.module;
import gravityMapScenario.gravityDataset.Dataset;

import java.io.*;

public class GMTUtilities
{

	public static String rangesToRegion(String[][] ranges) {
		String rangesString = "";

		for (int i = 0; i < 1; i++) {
			rangesString = rangesString + ranges[i][0] + "/";
			rangesString = rangesString + ranges[i][1] + "/";
		}
		rangesString = rangesString + ranges[1][0] + "/";
		rangesString = rangesString + ranges[1][1];

		return rangesString;
	}

	public static String esriGrid2xyz(String gridFile) {
		BufferedReader rdr = new BufferedReader(new StringReader(gridFile));
		StringWriter wtr = new StringWriter();

		try {
			// read the header
			String[] line = rdr.readLine().trim().split(" ");
			int numCols = Integer.parseInt(line[1]);

			line = rdr.readLine().trim().split(" ");
			int numRows = Integer.parseInt(line[1]);

			line = rdr.readLine().trim().split(" ");
			float xllcorner = Float.parseFloat(line[1]);

			line = rdr.readLine().trim().split(" ");
			float yllcorner = Float.parseFloat(line[1]);

			line = rdr.readLine().trim().split(" ");
			float cellsize = Float.parseFloat(line[1]);

			line = rdr.readLine().trim().split(" ");
			float no_value = Float.parseFloat(line[1]);

			String[] xcoords = new String[numCols];
			String[] ycoords = new String[numRows];

			for (int col = 0; col < numCols; col++) {
				xcoords[col] = new Double(xllcorner + (0.5 * cellsize)
						+ (col * cellsize)).toString();
			}

			for (int row = 0; row < numRows; row++) {
				ycoords[row] = new Double(yllcorner - (0.5 * cellsize)
						+ (numRows - row + 1) * cellsize).toString();
			}

			String value;
			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < numCols; col++) {
					if (col == 0)
						line = rdr.readLine().trim().split(" ");
					value = line[col];

					if (Float.parseFloat(value) == no_value)
						value = "NaN";

					wtr.write(xcoords[col] + " " + ycoords[row] + " " + value
							+ "\n");
				}
			}
			String xyzFile = wtr.toString();
			rdr.close();
			wtr.close();
			return xyzFile;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getRegion(String[][] ranges) {
		String rangesString = "";

		for (int i = 0; i < 1; i++) {
			rangesString = rangesString + ranges[i][0] + "/";
			rangesString = rangesString + ranges[i][1] + "/";
		}
		rangesString = rangesString + ranges[1][0] + "/";
		rangesString = rangesString + ranges[1][1];

		return rangesString;
	}

	public static String getColorRange(String[][] ranges) {
		float minValue = Float.parseFloat(ranges[2][0]);
		float maxValue = Float.parseFloat(ranges[2][1]);
		String colorRange;

		if (Math.abs(minValue - maxValue) < 100) {
			colorRange = minValue + "/" + maxValue + "/" + 5;
		} else if (Math.abs(minValue - maxValue) < 300) {
			colorRange = minValue + "/" + maxValue + "/" + 10;
		} else if (Math.abs(minValue - maxValue) < 500) {
			colorRange = minValue + "/" + maxValue + "/" + 20;
		} else {
			colorRange = minValue + "/" + maxValue + "/" + 50;
		}
		return colorRange;
	}
	
	public static boolean isRenderable(Dataset dataset) {
		if (dataset.getNumRecords() >= 11)
			return true;
		return false;
	}

	public static float getCellSpacing(String esriiGridFile) {
		BufferedReader rdr = new BufferedReader(new StringReader(esriiGridFile));
		String[] tokens;
		try {
			// read the header
			rdr.readLine();
			rdr.readLine();
			rdr.readLine();
			rdr.readLine();
			tokens = rdr.readLine().trim().split(" ");
			float cellsize = Float.parseFloat(tokens[1]);
			return cellsize;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
