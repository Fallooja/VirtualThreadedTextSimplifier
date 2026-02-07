package ie.atu.sw;

/**
 * Game attempt
 * @author Eoghan Fallon
 * @version 1.0
 * @since 04/Jan/2025
 *
 */

import java.util.*;
import java.io.*;

public class Game {
	private Simplifier simplifier;
	private List<String> googleWords;
	private Random random;

	public Game(Simplifier simplifier) {
		this.simplifier = simplifier;
		this.googleWords = new ArrayList<>();
		this.random = new Random();
	}

	/**
	 * @param googleFilePath loadGoogleWords(String googleFilePath)
	 * @Time Complexity: O(n) reads lines
	 */
	public void loadGoogleWords(String googleFilePath) {
		try (Scanner scanner = new Scanner(new File(googleFilePath))) {
			while (scanner.hasNextLine()) {
				googleWords.add(scanner.nextLine().trim());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Words file not found.");
		}
	}

	/**
	 * start()
	 * 
	 * @Time Complexity: O(g + p * l) Several steps = much complexity
	 */
	public void start() {
		if (googleWords.isEmpty()) {
			System.out.println("Words list is empty. Please load the file.");
			return;
		}

		String targetWord = googleWords.get(random.nextInt(googleWords.size()));

		System.out.println("Welcome to the Word Guessing Game!");
		System.out.println("Try to guess a word related to: " + targetWord);

		List<String> relatedWords = simplifier.getRelatedWords(targetWord);

		System.out.println("Related words (hints): ");
		for (String word : relatedWords) {
			System.out.println(" - " + word);
		}

		Scanner scanner = new Scanner(System.in);
		int score = 0;
		boolean gameRunning = true;

		while (gameRunning) {
			System.out.print("\nEnter a word (or type 'exit' to quit): ");
			String playerGuess = scanner.nextLine().trim().toLowerCase();

			if (playerGuess.equals("exit")) {
				gameRunning = false;
				System.out.println("Game over! Your score: " + score);
				break;
			}

			if (googleWords.contains(playerGuess)) {
				double similarity = simplifier.calculateSimilarityForGame(targetWord, playerGuess);
				System.out.println("Similarity with target word: " + similarity);

				if (similarity >= 0.7) {
					System.out.println("Correct! Well done!");
					score++;
				} else {
					System.out.println("Not quite right, try again.");
				}
			} else {
				System.out.println("Invalid guess. Please use a valid word from the word list.");
			}
		}

		scanner.close();
	}
}
