package com.miller.signature.decoders;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
	private final Color backgroundColor = Color.gray;
	private final Color foregroundColor = Color.blue;
	
	public Image decode(int width, int height, byte[] data)	throws ImageEncoderException {
		
		String signatureAsString = new String(data);
		
		/* Create the buffered image */
	    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = bufferedImage.createGraphics();
	    graphics.setColor(backgroundColor);
	    
	    /* Create the background image */
	    graphics.fillRect(0, 0, width, height);
	    
	    /* Set the boldness of the line */
	    graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    
	    /* Set the color */
	    graphics.setColor(foregroundColor);
	    
	    /* Break up data into segments */
	    String[] segments = signatureAsString.split("\\^");
	    
	    /* There has to be more than one segment */
	    if(segments.length == 1)
	    	throw new ImageEncoderException("Signature data is in incorrect format");
	    
	    /* Multiplier values based off max x and y */
	    double xScale = (Double.valueOf(bufferedImage.getWidth()) / Double.valueOf(getXMax(segments)));
	    double yScale = (Double.valueOf(bufferedImage.getHeight()) / Double.valueOf(getYMax(segments)));
    	
	    /* Sentinel values for no previous x or y */
	    int cursorCurrentX = -1;
		int cursorCurrentY = -1;
		
	    for(int i = 0; i < segments.length; i++) {
	    	
	    	/* Split the array int x and y coordinates*/
	    	String[] segmentData = segments[i].split(",");
	    	
	    	/* double check length */
	    	if(segmentData.length > 1) {
	    		
    			/* Compute new X and Y coordinates using data and multiplier */
	    		int newX = Integer.parseInt(segmentData[0]);
	    		int newY = Integer.parseInt(segmentData[1]);

	    		/* Check for pen up */
    			boolean penup = new int[]{newX,newY}.equals(PEN_UP);
    			
    			/* If not pen up then keep drawing */
    			if(!penup) {
    				
    				int scaledX = (int)(newX * xScale);
        			int scaledY = (int)(newY * yScale);
    				
	    			if(cursorCurrentX == -1 && cursorCurrentY == -1) {
	    				cursorCurrentX = scaledX;
	    				cursorCurrentY = scaledY;
	    			}
	    			
	    			/* Draw the line */
	    			graphics.drawLine(cursorCurrentX, cursorCurrentY, scaledX, scaledY);
	    			
	    			/* Set the last x and y */
	    			cursorCurrentX = scaledX;
	    			cursorCurrentY = scaledY;
	    			
    			} 
    			
    			/* Reset the cursors */
    			else {
    				cursorCurrentX = -1;
    				cursorCurrentY = -1;
    			}
	    		
	    	}
	    }

		return bufferedImage;
	}
	
	private int getXMax(String[] segments) {
		int max = 0;
		for(String segment : segments) {
			String[] segmentData = segment.split(",");
			if(segmentData.length > 1) {
				if(!segmentData[1].equals(END_OF_FILE)) {
	    			int newX = Integer.parseInt(segmentData[0]);
	    			if(newX > max && newX > 0)
	    				max = newX;
				}
			}
		}
		return max;
	}
	
	private int getYMax(String[] segments) {
		int max = 0;
		for(String segment : segments) {
			String[] segmentData = segment.split(",");
			if(segmentData.length > 1) {
				if(!segmentData[1].equals(END_OF_FILE)) {
	    			int newY = Integer.parseInt(segmentData[1]);
		    			if(newY > max && newY < 65000)
		    				max = newY;
				}
			}
		}
		return max;
	}

}
