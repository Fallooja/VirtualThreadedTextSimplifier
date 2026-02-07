package ie.atu.sw;

/**
 * Virtual threads attempt
 * @author Eoghan Fallon
 * @version 1.0
 * @since 04/Jan/2025
 *
 */

import java.lang.Thread;

public class SimplifierSource extends Simplifier implements Runnable {
	private String embeddingsFile;

	/**
	 * Constructor for SimplifierSource
	 * 
	 * @param simplifications
	 */
	public SimplifierSource() {
		super();
	}

	/**
	 * Runs the task of loading embeddings using a virtual thread.
	 */
	@Override
	public void run() {
		try {
			if (embeddingsFile == null) {
				System.out.println("\nError: File path for embeddings is not set. Please provide the path.");
				return;
			}

			// Virtual thread to load embeddings async
			@SuppressWarnings("preview")
			Thread virtualThread = Thread.ofVirtual().start(() -> {
				try {
					loadEmbeddings(embeddingsFile);
				} catch (Exception e) {
					System.out.println("\nError: Unable to load files. Please check the paths.");
				}
			});

			virtualThread.join();
		} catch (Exception e) {
			System.out.println("\nError: Unable to start virtual thread.");
		}
	}

	/**
	 * Sets the path for the embeddings file.
	 * 
	 * @param path Path to the embeddings file.
	 */
	public void setEmbeddingsPath(String path) {
		this.embeddingsFile = path;
	}
}
