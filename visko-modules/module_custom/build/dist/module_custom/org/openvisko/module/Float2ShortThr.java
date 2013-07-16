package org.openvisko.module;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.openvisko.module.operators.ToolkitOperator;

public class Float2ShortThr extends ToolkitOperator{
		
	public Float2ShortThr(String datasetOfFloatsURL){
		super(datasetOfFloatsURL, "binaryFloatArray.bin", false, true, "unsignedShortsFromFloats.bin");
	}
	
	public String transform(String scalingFactor, String offset){
		
		float factor = Float.valueOf(scalingFactor);
		float bias = Float.valueOf(offset);
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(binaryData);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		
		char[] shortArray = new char[binaryData.length/4];

		int counter = 0;
		while(byteBuffer.hasRemaining()){
			float aFloatValue = (byteBuffer.getFloat() * factor) + bias;
			char aShortValue = (char)aFloatValue;
			shortArray[counter++] = aShortValue;
		}
		
		ByteBuffer finalByteBuffer = ByteBuffer.allocate(binaryData.length/2);
		finalByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		for(char shortValue : shortArray)
			finalByteBuffer.putChar(shortValue);
		
		byteBuffer.clear();
		finalByteBuffer.clear();
		try{
			FileOutputStream fileOutput = new FileOutputStream(new File(outputPath));
			DataOutputStream dataOut = new DataOutputStream(fileOutput);
			dataOut.write(finalByteBuffer.array(), 0, binaryData.length/2);
			dataOut.close();
		}catch(IOException e){
				e.printStackTrace();
		}

		return outputURL;
	}
}