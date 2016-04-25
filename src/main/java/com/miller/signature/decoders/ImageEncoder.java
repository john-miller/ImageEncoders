package com.miller.signature.decoders;

import java.awt.Image;

public interface ImageEncoder {
	
	/**
	 * Converts a byte array to an image
	 * @param data
	 * @return
	 * @throws ImageEncoderException
	 */
	public Image decode(int width, int height, byte[] data) throws ImageEncoderException;
	
}
