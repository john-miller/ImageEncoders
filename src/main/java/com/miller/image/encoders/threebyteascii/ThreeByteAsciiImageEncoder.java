package com.miller.image.encoders.threebyteascii;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.miller.image.encoders.ImageEncoder;
import com.miller.image.encoders.ImageEncoderException;

/**
 * Converts a 3-byte ascii byte array to an image or converts an image to a
 * 3-byte ascii byte array
 * 
 * @author Jonathan
 */
public class ThreeByteAsciiImageEncoder implements ImageEncoder {

	/* Indicates a pen up occurrence per the specification */
	private static final char PEN_UP = 0x70;
	
	/* When in this range, indicates a start segment occurrence per the specification */
	private static final char[] START_SEGMENT_RANGE = new char[]{0x60, 0x6F};
	
	/* When in this range, indicates a character segment occurrence per the specification */
	private static final char[] CHARACTER_DATA_RANGE = new char[]{0x20, 0x5F}; 
	
	/* The offset for all characters in a character segment per the specification */
	private static final char CHARACTER_DATA_OFFSET = 0x20;
			
	public static void main(String[] args) {
		System.out.println(PEN_UP);
	}
	
	public Image decode(int width, int height, byte[] data) throws ImageEncoderException {
		
		/* Get data as character array */
		char[] characterArray = new String(data).toCharArray();
		
		/* Create a buffered image */
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		/* Call recursive method to draw image */
		draw(bufferedImage.createGraphics(), characterArray, 0);
		
		/* Return the buffered image */
		return bufferedImage;
	}
	
	/**
	 * Recursive method used to draw the image
	 * @param graphics
	 * @param characterArray
	 * @param currentIndex
	 * @throws ImageEncoderException
	 */
	private void draw(Graphics2D graphics, char[] characterArray, int currentIndex) throws ImageEncoderException {
		
		/* On start segment */
		if(isStartSegment(characterArray[currentIndex])) {
			
		} 
		
		/* On a pen up */
		else if(isPenUp(characterArray[currentIndex])) {
			
		} 
		
		/* On a character segment */
		else if (isCharacterSegment(characterArray[currentIndex])) {
			
		} 
		
		/* This is an unknown occurrence, throw exception */
		else {
			throw new ImageEncoderException("Data at position " + currentIndex + " is unknown format");
		}
		
	}
	
	/**
	 * Is this a pen up character
	 * @param character
	 * @return
	 */
	private boolean isPenUp(char character) {
		return character == PEN_UP;
	}
	
	/**
	 * Is this a start segment character
	 * @param character
	 * @return
	 */
	private boolean isStartSegment(char character) {
		return character >= START_SEGMENT_RANGE[0] && character <= START_SEGMENT_RANGE[1];
	}
	
	/**
	 * Is this a character segment character
	 * @param character
	 * @return
	 */
	private boolean isCharacterSegment(char character) {
		return character >= CHARACTER_DATA_RANGE[0] && character <= CHARACTER_DATA_RANGE[1];
	}

	public byte[] encode(Image image) throws ImageEncoderException {
		//TODO implement
		return null;
	}

}
