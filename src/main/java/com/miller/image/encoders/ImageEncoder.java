package com.miller.image.encoders;

import java.awt.Image;

public interface ImageEncoder {
	
	/**
	 * Converts a byte array to an image
	 * @param data
	 * @return
	 * @throws ImageEncoderException
	 */
	public Image decode(int width, int height, byte[] data) throws ImageEncoderException;
	
	/**
	 * Converts an image to a byte array
	 * @param image
	 * @return
	 * @throws ImageEncoderException
	 */
	public byte[] encode(Image image) throws ImageEncoderException;

}
