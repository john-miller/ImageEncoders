package com.miller.signature.decoders;

import static org.junit.Assert.assertNotNull;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class VectorDataImageEncoderTest {
	
	@Test
	public void instantiate() {
		assertNotNull(new VectorDataImageEncoder());
	}
	
	@Test(expected=ImageEncoderException.class)
	public void emptyArray() throws ImageEncoderException {
		VectorDataImageEncoder encoder = new VectorDataImageEncoder();
		encoder.decode(200, 100, new byte[]{});
	}
	
	@Test
	public void decode() throws ImageEncoderException, IOException {
		VectorDataImageEncoder encoder = new VectorDataImageEncoder();
		byte[] encoded = readFile("src/test/resources/signatures/vectordata.signature").getBytes();
		Image image = encoder.decode(300, 100, encoded);
		assertNotNull(image);
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

}
