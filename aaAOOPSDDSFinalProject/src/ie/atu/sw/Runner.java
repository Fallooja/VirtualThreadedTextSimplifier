package ie.atu.sw;

/**
 * Starts the program
 * 
 * @author Eoghan Fallon
 * @version 1.0
 * @since 04/Jan/2025
 *
 */
//classic Runner main method only
public class Runner {
	/**
	 * Main Method is here
	 * 
	 * @Time complexity: O(1)
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Menu menu = new Menu();
		menu.heading();
		menu.setEmbed();
		menu.setGoogle();
		menu.setInputFile();
		menu.setOutputFile();
		menu.simplifyText();
		System.out.println("Program completed successfully.");
	}
}