import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Tokenizer {

	String[] validcommands = new String[]{ "forw", "back", "left", "right"};
	List<String> knownCommands = Arrays.asList(validcommands);
	StringBuilder sb = new StringBuilder();
	
	//StringBuilder sb = new StringBuilder();
	LinkedList<Token> tokens = new LinkedList<Token>();
	LinkedList<Command> commands = new LinkedList<Command>();

	public Tokenizer() {
	}

	public LinkedList<Command> parseInput() { // starting approach

		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
			sb.append("\n");
		}
		parseTokens();
		sc.close();
		return commands;
	}

	public void parseTokens() {
		//If repetition argument is started
		boolean repinit = false;
		//Number of nested citation characters

		boolean isComment = false;
		int linenumber = 1;
		StringBuilder temp = new StringBuilder();
		char[] inputChars = sb.toString().toCharArray();
		for (char c : inputChars) {
			c = Character.toLowerCase(c); // case insensitive
			if (!isComment && !repinit) {
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
					if(temp.length() != 0){ //to handle multiple whitespaces
						tokens.add(new Token(temp.toString(), linenumber));
					}
					temp = new StringBuilder();
					tokens.add(new Token("\"", linenumber));
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
		//Since it does not end with a dot. This error is handled later in createCommands.
		if(temp.length() != 0){
			tokens.add(new Token(temp.toString(), linenumber)); 
			temp = new StringBuilder(); 
		}
		
//    	for(Token token: tokens){
//    		token.myprint();
//    	}
		createCommands(tokens);
		
	}
	
	
	private void createCommands(LinkedList<Token> tokens){
		ListIterator<Token> li = tokens.listIterator();
		while(li.hasNext()){
			Token token = li.next();
			commands.add(new Command(token, li));
		}
//		for(Command command: commands){
//			command.print();
//		}
	}
	
	private void printError(int line){
		System.out.println("Syntaxfel på rad " + line);
		System.exit(0); //We dont want to continue if we get and error.
	}
	
	
}
