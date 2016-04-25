package com.miller.signature.decoders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
					
	public static void main(String[] args) throws ImageEncoderException, IOException {
		
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(
				new ThreeByteAsciiImageEncoder().decode(200, 100, 
						readFile("src/test/resources/signatures/3byteascii.signature").getBytes()))));
		frame.pack();
		frame.setVisible(true);
	}
	
	public static String readFile(String file) throws IOException {
		InputStream in = new FileInputStream(new File(file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		reader.close();
		return out.toString();
	}
	
	public Image decode(int width, int height, byte[] data) throws ImageEncoderException {
		
		/* Get data as character array */
		char[] characterArray = new String(data).toCharArray();
		
		if(characterArray.length == 0)
			throw new ImageEncoderException("Character array cannot be empty");
		
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
		if(currentIndex == characterArray.length -1) {
			
		}
		else if(isStartSegment(characterArray[currentIndex])) {
			System.out.println("Start Segment");
			
			/* Read start segment character into binary string */
			int[] bits = extractBits(characterArray[currentIndex++]);
			
			/* Binary Format: 0 1 1 0 X10 X9 Y10 Y9 */
			int x10 = bits[4];
			int x9 = bits[5];
			int y10 = bits[6];
			int y9 = bits[7];
			
			graphics.drawLine(x9, y9, x10, y10);
			
			draw(graphics, characterArray, currentIndex);
		} 
		
		/* On a pen up */
		else if(isPenUp(characterArray[currentIndex])) {
			draw(graphics, characterArray, ++currentIndex);
		} 
		
		/* On a character segment */
		else if (isCharacterSegment(characterArray[currentIndex])) {
			System.out.println("Character segment");
			
			char offsetChar1 = (char) (characterArray[currentIndex++] - CHARACTER_DATA_OFFSET);
			char offsetChar2 = (char) (characterArray[currentIndex++] - CHARACTER_DATA_OFFSET);
			char offsetChar3 = (char) (characterArray[currentIndex++] - CHARACTER_DATA_OFFSET);
			
			int[] bits1 = extractBits(offsetChar1);
			int[] bits2 = extractBits(offsetChar1);
			int[] bits3 = extractBits(offsetChar1);
			
			graphics.drawLine(bits3[4], bits3[7], bits3[3], bits3[6]);
			graphics.drawLine(bits3[3], bits3[6], bits3[2], bits3[5]);
			
			draw(graphics, characterArray, currentIndex);
		} 
		
		/* This is an unknown occurrence, throw exception */
		else {
			throw new ImageEncoderException("Data at position " + currentIndex + " is unknown format");
		}
		
	}
	
	private int[] extractBits(char character) {
		int[] characterArray = new int[8];
		for (int i = 7; i > 0; i--)
			characterArray[7 - i] = ((character >> i) & 1);
		return characterArray;
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

}
