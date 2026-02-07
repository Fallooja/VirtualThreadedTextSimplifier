package ie.atu.sw;

/**
 * Menu code
 * @author Eoghan Fallon
 * @version 1.0
 * @since 04/Jan/2025
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Menu
 * 
 * @Time O(1): heading(), menu(), redirectMenu(int), setOutputFile(), exit()
 *       Simple calls O(n): setEmbed(), setGoogle(), setInputFile() Likewise O(m
 *       * n): simplifyText(), startGame() Slighter longer complexity due to
 *       multiple calls
 */
//largely from the code stubs?
public class Menu {

	private boolean embeddingsSet = false;
	private boolean googleSet = false;
	private boolean inputSet = false;
	private boolean outputSet = false;

	private Scanner s = new Scanner(System.in);

	public void heading() throws Exception {
		System.out.println(ConsoleColour.WHITE);
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*             Virtual Threaded Text Simplifier             *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println(ConsoleColour.RESET);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		menu();
	}

	private void menu() throws Exception {
		int menuOption = 0;

		do {
			try {
				System.out.println("===================================================");
				System.out.println("Main Menu\n===================================================");
				System.out.println("Select option:");
				System.out.println("1) Set a path and name for the embeddings and word files to use");
				System.out.println("2) Start the game");
				System.out.println("3) Exit");

				menuOption = s.nextInt();
				if (menuOption > 3)
					System.out.println("This option does not exist.");
			} catch (InputMismatchException e) {
				System.out.println("Value not valid. Please, select a valid option [1-3]\n");
			}
			s.nextLine();
		} while (menuOption < 1 || menuOption > 3);

		redirectMenu(menuOption);
	}

	private void redirectMenu(int menuOption) throws Exception {
		switch (menuOption) {
		case 1:
			setEmbed();
			break;
		case 2:
			startGame();
			break;
		case 3:
			exit();
			break;
		default:
			break;
		}
	}

	private String embeddingsFilePath;
	private String googleFilePath;
	private String inputFilePath;
	private String outputFilePath;

	void setEmbed() {
		if (!embeddingsSet) {
			boolean valid = false;
			while (!valid) {
				System.out.print("Enter the full path for the embeddings file: ");
				embeddingsFilePath = s.nextLine();
				File file = new File(embeddingsFilePath);
				if (file.exists()) {
					System.out.println("Embeddings file set to: " + embeddingsFilePath);
					embeddingsSet = true;
					valid = true;
				} else {
					System.out.println("Error: Embeddings file not found. Please enter a valid path.");
				}
			}
		} else {
			System.out.println("Embeddings file already set to: " + embeddingsFilePath);
		}
	}

	void setGoogle() {
		if (!googleSet) {
			boolean valid = false;
			while (!valid) {
				System.out.print("Enter the full path for the words list file: ");
				googleFilePath = s.nextLine();
				File file = new File(googleFilePath);
				if (file.exists()) {
					System.out.println("Words file set to: " + googleFilePath);
					googleSet = true;
					valid = true;
				} else {
					System.out.println("Error: Words file not found. Please enter a valid path.");
				}
			}
		} else {
			System.out.println("Words file already set to: " + googleFilePath);
		}
	}

	void setInputFile() {
		if (!inputSet) {
			boolean valid = false;
			while (!valid) {
				System.out.print("Enter the full path for the input text file: ");
				inputFilePath = s.nextLine();
				File file = new File(inputFilePath);
				if (file.exists()) {
					System.out.println("Input file set to: " + inputFilePath);
					inputSet = true;
					valid = true;
				} else {
					System.out.println("Error: Input file not found. Please enter a valid path.");
				}
			}
		} else {
			System.out.println("Input file already set to: " + inputFilePath);
		}
	}

	void setOutputFile() {
		if (!outputSet) {
			System.out.print("Enter the full path for the output file: ");
			outputFilePath = s.nextLine();
			System.out.println("Output file set to: " + outputFilePath);
			outputSet = true;
		} else {
			System.out.println("Output file already set to: " + outputFilePath);
		}
	}

	void simplifyText() throws Exception {
		if (embeddingsFilePath == null || googleFilePath == null || inputFilePath == null || outputFilePath == null) {
			System.out.println("Please set all file paths before simplifying the text.");
			return;
		}

		try {
			Simplifier simplifier = new Simplifier();

			simplifier.loadEmbeddings(embeddingsFilePath);

			simplifier.loadSimpleWords(googleFilePath);

			TextProcessor processor = new TextProcessor(simplifier);
			processor.processFile(inputFilePath, outputFilePath);

			System.out.println("Text simplification completed. Output saved to: " + outputFilePath);

			menu();

		} catch (IOException e) {
			System.out.println("Error processing files: " + e.getMessage());
		}
	}

	private void startGame() {

		if (!googleSet || !embeddingsSet) {
			System.out.println("Please set the embeddings and words files before starting the game.");
			return;
		}

		Simplifier simplifier = new Simplifier();

		try {

			simplifier.loadEmbeddings(embeddingsFilePath);
			simplifier.loadSimpleWords(googleFilePath);

			Game game = new Game(simplifier);
			game.loadGoogleWords(googleFilePath);
			game.start();

		} catch (Exception e) {
			System.out.println("Error starting the game: " + e.getMessage());
		}
	}

	private void exit() {
		System.out.println("\n* Virtual Threaded Text Simplifier has been closed * \nGoodbye!");
		s.close();
		System.exit(0);
	}
}
