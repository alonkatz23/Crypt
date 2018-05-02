import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * This class encrypts and decrypts text files using one of 3 algorithms: Random
 * monoalphabet, Vigenere, or Playfair
 * 
 * 
 * @author
 * @version
 * 
 */
public class Crypt {

	/**
	 * 
	 * An integer representing the algorithm chosen. Set to: 1 for random
	 * monoalphabet 2 for Vigenere 3 for Playfair
	 * 
	 */
	public static final int algorithm = 3;
	public static final String lineSeparator = System.lineSeparator();

	/**
	 * Reads from the file specified, and writes out an encrypted version of the
	 * file. If the output file already exists, overwrite it.
	 * 
	 * @param inputFilename
	 *            The path of the file to be encrypted.
	 * @param outputFilename
	 *            The path of the encrypted file to be saved.
	 * @param keyword
	 *            The keyword to be used in the encryption algorithm.
	 * 
	 */
	public void encrypt(String inputFilename, String outputFilename, String keyword) {
		char[][] cipher = new char[5][5];
		StringBuffer key = new StringBuffer();

		for (int i = 0; i < keyword.length(); i++) {
			if (key.indexOf("" + keyword.charAt(i)) == -1) {
				key.append(keyword.charAt(i));
			}
		}

		char alpha = 97;
		for (int i = 0; i < 26; i++) {
			char letter = (char) (alpha + i);
			if (key.indexOf("" + letter) == -1 && letter != 'j') {
				key.append(letter);
			}
		}
		
//		System.out.println(key.toString());
		
		

//		System.out.println();
		
		int f = 0;
		for (int i = 0; i < cipher.length; i++) {
			for (int j = 0; j < cipher.length; j++) {
				cipher[j][i] = key.charAt(f);
				
				f++;
			}
		}

		

		Scanner scan = null;
		FileWriter writer = null;
		BufferedWriter bWriter = null;
		try {

			FileReader reader = new FileReader(inputFilename);
			BufferedReader bReader = new BufferedReader(reader);
		//	scan = new Scanner(bReader);
			writer = new FileWriter(outputFilename);

			bWriter = new BufferedWriter(writer);

			char firstLetter = '\u0000';
			char secondLetter = '\u0000';
			int firstLocation = 0;
			int secondLocation = 0;
			
			
			boolean firstLetterIsUpper = false;
			boolean secondLetterIsUpper = false;
			
			int offset = 32;
			int indexI = key.indexOf("" + 'i');
			String line;
			while ((line = bReader.readLine()) != null) {
				

				
				StringBuffer lyne = new StringBuffer(line);
				// ENCRYPT
				int counter = 0;
				int length = lyne.length();
				while (length > counter) {

					char charAtCounter = lyne.charAt(counter);
					if (charAtCounter >= 65 && charAtCounter <= 90
							|| charAtCounter >= 97 && charAtCounter <= 122) {
						if (firstLetter == '\u0000') {
							firstLetter = charAtCounter;
							firstLocation = counter;
							if (firstLetter >= 97) {
								firstLetterIsUpper = false;
							} else {
								firstLetterIsUpper = true;
								firstLetter += offset;
							}
						} else {
							secondLocation = counter;
							secondLetter = charAtCounter;
							
							if (secondLetter >= 97) {
								secondLetterIsUpper = false;
							} else {
								secondLetterIsUpper = true;
								secondLetter += offset;
							}
							
							
							int firstIndex = key.indexOf("" + firstLetter);
							
							int secondIndex = key.indexOf("" + secondLetter);
							
							
							if (firstIndex <= -1) {
								firstIndex = indexI;
							}
							if (secondIndex <= -1) {
								secondIndex = indexI;
							}
							
							int firstIndex1 = firstIndex % 5;
							int firstIndex2 = firstIndex / 5;
							int secondIndex1 = secondIndex % 5;
							int secondIndex2 = secondIndex / 5;
							
							
//							System.out.println(secondLetter + " " + secondIndex + " " + secondIndex1 + " " + secondIndex2);
							
							if (firstIndex1 == secondIndex1 || secondIndex2 == firstIndex2) {
								char temp = firstLetter;
								if(firstLetterIsUpper)
									firstLetter = (char) (secondLetter - offset);
								else
									firstLetter = secondLetter;
								
//								System.out.println(secondLetterIsUpper);
								
								if(secondLetterIsUpper)
									secondLetter = (char) (temp - offset);
								else
									secondLetter = temp;
								
								
								
							} else {
								
								if(firstLetterIsUpper)
									firstLetter = (char) (cipher[secondIndex1][firstIndex2] - offset);
								else
									firstLetter = cipher[secondIndex1][firstIndex2];
								
								if(secondLetterIsUpper)
									secondLetter = (char) (cipher[firstIndex1][secondIndex2] - offset);
								else
									secondLetter = cipher[firstIndex1][secondIndex2];
								
								
							}
//							System.out.println(lyne);
//							System.out.println(firstLocation);
//							System.out.println(secondLocation);
							lyne.replace(firstLocation, firstLocation + 1, "" + firstLetter);
							lyne.replace(secondLocation, secondLocation + 1, "" + secondLetter);
							
							//Remember to set letters to null
							
							firstLetter = '\u0000';
//							secondLetter = '\u0000';
						}
					}
					counter++;
					if(counter == lyne.length() && firstLetter != '\u0000') {
						while ((line = bReader.readLine()) != null){
							
							lyne.append("\n" + line);
							length = lyne.length();
							if (line.length() > 0) {
//								System.out.println(lyne);
								break;
							}
						}
					}
					
				}

				line = lyne.toString();
				
				bWriter.write(line);
				bWriter.write(lineSeparator);
//				System.out.println("\n" + line);
			}
//			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scan != null) {
				scan.close();
			}

			try {
				if (bWriter != null) {
					bWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads from the (previously encrypted) file specified, and writes out a
	 * decrypted version of the file. If the output file already exists, overwrite
	 * it.
	 * 
	 * @param inputFilename
	 *            The path of the encrypted file.
	 * @param outputFilename
	 *            The path of the decrypted file to be saved.
	 * @param keyword
	 *            The keyword to be used in the decryption algorithm.
	 * 
	 */
	public void decrypt(String inputFilename, String outputFilename, String keyword) {
		encrypt(inputFilename, outputFilename, keyword);
	}

}
