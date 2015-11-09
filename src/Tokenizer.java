import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Tokenizer {

	String[] commands = new String[]{ "forw", "back", "left", "right"};
	List<String> knownCommands = Arrays.asList(commands);
	
	//OBS!!! For testing purposes
	StringBuilder sb = new StringBuilder("DOWN.\nFORW 1. LEFT 90.\nFORW 1. LEFT 90.\nFORW 1. LEFT 90.\nFORW 1. LEFT 90. COLOR #123FEq.");
	//StringBuilder sb = new StringBuilder();
	LinkedList<Token> tokens = new LinkedList<Token>();

	public Tokenizer() {
	}

	public void parseInput() { // starting approach

		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
			sb.append("\n");
			parseTokens();
		}
		sc.close();
	}

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
					break;
				case '\r': // windows fix
					break;
				case '\t':
				case ' ':
				case '.': //TODO: Implement the dot's function.
				case '\n':
					if(temp.length() != 0){ //to handle multiple whitespaces
						tokens.add(new Token(temp.toString(), linenumber)); // add to token list
						temp = new StringBuilder(); // empty the stringbuilder
					}
					break;
				default:
					temp.append(c);
				}

				if (c == '\n') { //reached new line
					isComment = false;
					linenumber++;
				}
			}

		}
		createCommands(tokens); //Call with list of tokens to achieve list of executable commands.
	}
	
	public LinkedList<Command> createCommands(LinkedList<Token> tokens){
		LinkedList<Command> commands = new LinkedList<Command>();
		ListIterator<Token> listIterator = tokens.listIterator();
		while (listIterator.hasNext()) {
			Token t = listIterator.next(); //First token
			String value = t.getValue(); // Gets value of token
			int linenumber = t.getLineNumber(); //Get the tokens linenumber
			switch (value){
			case "down":
			case "up":
				commands.add(new Command(value, linenumber));
				break;
			case "color":
				String colorcode = listIterator.next().getValue();
				if(colorcode.matches("^#[A-Za-z0-9]{6}$")){ //Example: #123AbC
					commands.add(new Command(value, colorcode, linenumber));
				}else{
					printError(linenumber);
				}
				break;
			case "rep":
				//TODO: Add code for case rep
				break;
			default:
				if(knownCommands.contains(value)){
					try{
						commands.add(new Command(value, Integer.parseInt(listIterator.next().getValue()), linenumber));
					}catch(NumberFormatException e){ //parameter is not an int, parseInt fails.
						printError(linenumber); 
					}
				}
				else{
					printError(linenumber);
				}
			}
			
		}
		for(Command c : commands){ //for testing
			c.print();
		}
		return commands; //use this list to go through commands and execute them
	}
	
	private void printError(int line){
		System.out.println("Syntaxfel på rad " + line);
		System.exit(1); //We dont want to continue if we get and error.
	}
	
	
}
