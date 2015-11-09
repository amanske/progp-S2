import java.util.LinkedList;
import java.util.Scanner;

public class Tokenizer {

	String[] knownCommands = { "forw", "back", "left", "right", "down", "up", "color", "rep" };
	StringBuilder sb = new StringBuilder("");
	LinkedList<String> tokens = new LinkedList<String>();

	public Tokenizer() {
	}

	public void parseInput() { // starting approach

		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
			sb.append("\n");

			System.out.println(parseTokens().toString());
		}
		sc.close();
	}

	public LinkedList<String> parseTokens() {
		boolean isComment = false;
		int linenumber = 1;
		StringBuilder temp = new StringBuilder();
		char[] inputChars = sb.toString().toCharArray();
		for (char c : inputChars) {
			c = Character.toLowerCase(c);
			if (!isComment) {
				switch (c) {
				case '%':
					isComment = true;
					break;
				case '\r': // windows fix
					break;
				case '\t':
				case ' ':
				case '\n':
					if(temp.length() != 0){ //to handle multiple whitespaces
						tokens.add(temp.toString()); // add to token list
						temp = new StringBuilder(); // empty the stringbuilder
					}
					break;
				default:
					temp.append(c);
				}

				if (c == '\n') {
					isComment = false;
					linenumber++;
				}
			}

		}
		return tokens;
	}
}
