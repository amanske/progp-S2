/**
 * This class is systematically parsing through the input and
 * creates tokens of character pairs. A LinkedList, commands, is
 * build so that the Executer-class can handle it.
 */

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Tokenizer {

	StringBuilder sb = new StringBuilder();
	LinkedList<Token> tokens = new LinkedList<Token>();
	LinkedList<Command> commands = new LinkedList<Command>();

	public Tokenizer() {}
	
	/**
	 * Parses the input from System.in and calls parseTokens() to
	 * create: 1. a list of tokens and 2. a list of commands.
	 * @return commands The list of commands to execute.
	 */
	public LinkedList<Command> parseInput() { 

		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
			sb.append("\n");
		}
		parseTokens();
		sc.close();
		return commands;
	}

	/**
	 * Parses the tokens to a complete token list to be able to create commands 
	 * using the createCommands method.
	 */
	public void parseTokens() {
		boolean isComment = false;
		int linenumber = 1;
		StringBuilder temp = new StringBuilder();
		char[] inputChars = sb.toString().toCharArray();
		for (char c : inputChars) {
			c = Character.toLowerCase(c); // case insensitive
			if (!isComment) {
				switch (c) {
				case '%':
					isComment = true;
					if(temp.length() != 0){ //to handle multiple whitespaces
						tokens.add(new Token(temp.toString(), linenumber)); // add to token list
					}
					temp = new StringBuilder(); //And flush the stringbuilder
					break;
				case '\r': // windows fix
					break;
				case '\t':
				case ' ':
				case '\n':
					if(temp.length() != 0){ //to handle multiple whitespaces
						tokens.add(new Token(temp.toString(), linenumber)); // add to token list
						temp = new StringBuilder(); // empty the stringbuilder
					}
					break;
				case '.':
					if(temp.length() != 0){ //to handle multiple whitespaces
						tokens.add(new Token(temp.toString(), linenumber)); // add to token list
					}
					tokens.add(new Token(".", linenumber)); //add dot as token
					temp = new StringBuilder(); //And flush the stringbuilder
					break;
				case '"': //Starts a repetition token
					if(temp.length() == 0){ //to handle multiple whitespaces
						temp = new StringBuilder();
						tokens.add(new Token("\"", linenumber));
					}else{
						temp.append(c);
					}
					break;
				default:
					temp.append(c);
				}
			}	
			if (c == '\n') { //reached new line
				isComment = false;
				linenumber++;
			}
		}
		//OBS!! If there is anything left in the stringbuilder, it means that it will have an error
		//Since it does not end with a dot. This error is handled later in Commands.
		if(temp.length() != 0){
			tokens.add(new Token(temp.toString(), linenumber)); 
			temp = new StringBuilder(); 
		}
		//create executionable Commands
		createCommands(tokens);
		
	}
	/**
	 * Creates a list of commands to be executed by the Executer.
	 * @param tokens The tokens to create commands out of.
	 */
	private void createCommands(LinkedList<Token> tokens){
		ListIterator<Token> li = tokens.listIterator();
		while(li.hasNext()){
			Token token = li.next();
			commands.add(new Command(token, li));
		}
	}	
}
