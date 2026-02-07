package ie.atu.sw;

/**
 * Processes the text
 * @author Eoghan Fallon
 * @version 1.0
 * @since 04/Jan/2025
 *
 */

import java.io.*;

public class TextProcessor {
	private final Simplifier simplifier;

	public TextProcessor(Simplifier simplifier) {
		this.simplifier = simplifier;
	}

	/**
	 * processFile(String inputFile, String outputFile)
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException
	 * @Time Complexity: O(l * n) Line length of line x number
	 */
	// largely taken from previous OOP project I did Caesar Cipher
	public void processFile(String inputFile, String outputFile) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

			String line;
			while ((line = reader.readLine()) != null) {
				String simplifiedLine = simplifyLine(line);
				writer.write(simplifiedLine);
				writer.newLine();
			}
		}

		System.out.println("Simplification complete. Output saved to: " + outputFile);
	}

	/**
	 * simplifyLine(String line)
	 * 
	 * @param line
	 * @return
	 * @Time Complexity: O(l * w) Must call multiple times, l length of line x w
	 *       average words per line
	 */
	// https://stackoverflow.com/questions/34123139/i-want-to-split-up-the-strings-in-stringbuilder
	private String simplifyLine(String line) {
		StringBuilder simplifiedLine = new StringBuilder();
		for (String word : line.split("\\s+")) {
			simplifiedLine.append(simplifier.simplifyWord(word)).append(" ");
		}
		return simplifiedLine.toString().trim();
	}
}
