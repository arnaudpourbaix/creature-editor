package com.pourbaix.creature.script.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextFileReader {

	private final File file;
	private final FileInputStream fileInputStream;
	private final InputStreamReader inputStreamReader;
	private final BufferedReader bufferedReader;
	private int lineNumber;

	public TextFileReader(String filePath) throws FileNotFoundException {
		file = new File(filePath);
		fileInputStream = new FileInputStream(file);
		inputStreamReader = new InputStreamReader(fileInputStream);
		bufferedReader = new BufferedReader(inputStreamReader);
		lineNumber = 0;
	}

	public String readLine() throws TextFileReaderException {
		lineNumber++;
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			throw new TextFileReaderException(e, lineNumber);
		}
	}

	public void close() throws TextFileReaderException {
		try {
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		} catch (IOException e) {
			throw new TextFileReaderException(e, null);
		}
	}

	public int getLineNumber() {
		return lineNumber;
	}

}
