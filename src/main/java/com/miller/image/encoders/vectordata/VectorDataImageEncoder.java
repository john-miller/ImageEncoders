package com.miller.image.encoders.vectordata;

import java.awt.Image;

import com.miller.image.encoders.ImageEncoder;
import com.miller.image.encoders.ImageEncoderException;

/**
 * Vector data image encoder format:
 * 
 * X1,Y1^X2,Y2^X3,Y3^Xn,Yn^0,65530^~
 * 
 * 0,65530 = PEN UP
 * 
 * @author Jonathan
 */
public class VectorDataImageEncoder implements ImageEncoder {

	private static final int[] PEN_UP = new int[]{0, 65530};
	private static final char END_OF_FILE = '~';
	
	public Image decode(int width, int height, byte[] data)
			throws ImageEncoderException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] encode(Image image) throws ImageEncoderException {
		// TODO Auto-generated method stub
		return null;
	}

}
