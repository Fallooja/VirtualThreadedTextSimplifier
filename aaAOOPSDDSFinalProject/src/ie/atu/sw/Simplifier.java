package ie.atu.sw;

/**
 * Simplification logic
 * @author Eoghan Fallon
 * @version 1.0
 * @since 04/Jan/2025
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Simplifier {
	private final Map<String, double[]> embeddings = new TreeMap<>();
	private final Map<String, double[]> googleEmbeddings = new TreeMap<>();

	public Simplifier() {
	}

	/**
	 * loadEmbeddings(String path)
	 * 
	 * @param path
	 * @throws Exception
	 * @Time Complexity: O(m * p) m lines x line split into p parts etc
	 */
	// line trim
	// https://stackoverflow.com/questions/44619023/using-scanner-hasnext-and-hasnextline-to-retrieve-2-elements-per-line

	public void loadEmbeddings(String path) throws Exception {

		System.out.println("Loading embeddings from path: " + path);
		File file = new File(path);

		if (!file.exists()) {
			throw new FileNotFoundException("Embeddings file not found: " + path);
		}

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] parts = line.split(",");

				if (parts.length < 2) {
					continue;
				}

				String word = parts[0].trim().toLowerCase();
				double[] vector = new double[parts.length - 1];

				try {
					for (int i = 1; i < parts.length; i++) {
						vector[i - 1] = Double.parseDouble(parts[i].trim());
					}
					embeddings.put(word, vector);
				} catch (NumberFormatException e) {
					System.err.println("Malformed embedding for word: " + word);
				}
			}
		}

		embeddings.entrySet().stream().limit(10)
				.forEach(entry -> System.out.println(entry.getKey() + " -> " + Arrays.toString(entry.getValue())));

		System.out.println("Total embeddings loaded: " + embeddings.size());
	}

	/**
	 * loadSimpleWords(String path)
	 * 
	 * @param path
	 * @throws Exception
	 * @Time Complexity: O(n * m) n1000 words x m vectors
	 */
	public void loadSimpleWords(String path) throws Exception {
		System.out.println("Loading words...");
		File file = new File(path);

		if (!file.exists()) {
			throw new FileNotFoundException("Words file not found: " + path);
		}

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine().trim().toLowerCase();

				double[] embedding = embeddings.get(word);

				if (embedding != null) {
					googleEmbeddings.put(word, embedding);
				} else {
					System.out.println("No embedding found for word: " + word);
				}
			}
		}

		System.out.println("Loaded " + googleEmbeddings.size() + " Words with embeddings.");
	}

	/**
	 * simplifyWord(String word)
	 * 
	 * @param word
	 * @return
	 * @Time Complexity: O(k * n) Word vectors x number of words 1000
	 */
	// Deep learning
	// https://github.com/deeplearning4j/deeplearning4j/blob/374609b2672e97737b9eb3ba12ee62fab6cfee55/deeplearning4j-scaleout/deeplearning4j-nlp/src/main/java/org/deeplearning4j/models/embeddings/loader/WordVectorSerializer.java#L113
	public String simplifyWord(String word) {
		word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
		double[] wordVector = embeddings.get(word);

		if (wordVector == null) {
			System.out.println("No embedding found for input word: " + word);
			return word;
		}

		String bestMatch = word;
		double bestScore = 0.1;

		for (Map.Entry<String, double[]> entry : googleEmbeddings.entrySet()) {
			double score = computeCosineSimilarity(wordVector, entry.getValue());
			System.out.println("Comparing '" + word + "' with '" + entry.getKey() + "', Similarity: " + score);

			if (score > bestScore) {
				bestScore = score;
				bestMatch = entry.getKey();
			}
		}

		if (bestScore > 0.5) {
			return word;
		}

		System.out.println("Word: " + word + ", Best Match: " + bestMatch + ", Score: " + bestScore);
		return bestMatch.equals(word) ? word : bestMatch;
	}

	/**
	 * computeCosineSimilarity(double[] vec1, double[] vec2)
	 * 
	 * @param vec1
	 * @param vec2
	 * @return
	 * @Time Complexity: O(k) Multiple vector * vector
	 */
	// inspired by
	// https://github.com/keon/algorithms/blob/master/algorithms/maths/cosine_similarity.py
	public double computeCosineSimilarity(double[] vec1, double[] vec2) {
		double dotProduct = 0;
		double normVec1 = 0;
		double normVec2 = 0;

		for (int i = 0; i < vec1.length; i++) {
			dotProduct += vec1[i] * vec2[i];
			normVec1 += vec1[i] * vec1[i];
			normVec2 += vec2[i] * vec2[i];
		}

		double denominator = Math.sqrt(normVec1) * Math.sqrt(normVec2);
		if (denominator == 0)
			return 0;
		return dotProduct / denominator;
	}

	/**
	 * getRelatedWords(String word)
	 * 
	 * @param word
	 * @return
	 * @Time Complexity: O(n * k * log n) n 1000 words x k number of vectors
	 */
	public List<String> getRelatedWords(String word) {
		List<String> relatedWords = new ArrayList<>();
		double[] wordVector = embeddings.get(word);

		if (wordVector == null) {
			System.out.println("No embedding found for word: " + word);
			return relatedWords;
		}

		googleEmbeddings.entrySet().stream().sorted((entry1, entry2) -> {
			double score1 = computeCosineSimilarity(wordVector, entry1.getValue());
			double score2 = computeCosineSimilarity(wordVector, entry2.getValue());
			return Double.compare(score2, score1);
		}).limit(5).forEach(entry -> relatedWords.add(entry.getKey()));

		return relatedWords;
	}

	/**
	 * calculateSimilarityForGame(String word1, String word2)
	 * 
	 * @param word1
	 * @param word2
	 * @return
	 * @Time Complexity: O(k) Both Strings O(1) but multiplied up
	 */
	public double calculateSimilarityForGame(String word1, String word2) {
		double[] vector1 = embeddings.get(word1);
		double[] vector2 = embeddings.get(word2);

		if (vector1 == null || vector2 == null) {
			System.out.println("One or both words are not found in embeddings.");
			return 0;
		}

		return computeCosineSimilarity(vector1, vector2);
	}
}
