import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Tokenizer {

	String[] commands = new String[]{ "forw", "back", "left", "right"};
	List<String> knownCommands = Arrays.asList(commands);
	
	//OBS!!! For testing purposes
	StringBuilder sb = new StringBuilder("% Syntaxfel: saknas punkt.\nDOWN \n% Om filen tar slut mitt i ett kommando\n% så anses felet ligga på sista raden");
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
				try{
					Token dot = listIterator.next();
					if(dot.getValue().equals(".")){
						commands.add(new Command(value, linenumber));
					}else{
						printError(dot.getLineNumber()); //TODO: Get right linenumber
					}
				}catch(Exception e){
					printError(linenumber); //TODO: Correct linenumber
				}
				break;
			case "color":
				try{
					Token color = listIterator.next();
					Token colordot = listIterator.next();
					String colorcode = color.getValue();
					if(colordot.getValue().equals(".")){
						if(colorcode.matches("^#[A-Za-z0-9]{6}$")){ //Example: #123AbC
							commands.add(new Command(value, colorcode, linenumber));
						}else{
							printError(color.getLineNumber()); //Failed on color line
						}
					}else{
						printError(colordot.getLineNumber()); //TODO: Get right linenumber
					}
				}catch(NoSuchElementException e){
					printError(linenumber); //TODO: Correct linenumber
				}
				break;
			case "rep":
				//TODO: Add code for case rep
				break;
			default:
				if(knownCommands.contains(value)){
					try{
						Token parameter = listIterator.next();
						Token defaultdot = listIterator.next();
						if(defaultdot.getValue().equals(".")){
							try{
								commands.add(new Command(value, Integer.parseInt(parameter.getValue()), linenumber));
							}catch(NumberFormatException e){ //parameter is not an int, parseInt fails.
								printError(parameter.getLineNumber()); //Fails on parameter line 
							}
						}else{
							printError(defaultdot.getLineNumber()); //TODO: Get right linenumber
						}
					}catch(NoSuchElementException e){
						printError(linenumber); //TODO: Correct linenumber
					}
				}else{
					printError(linenumber); //Fails on command line
				}
			}
			
		}
		for(Command c : commands){ //for testing
			c.print();
		}
		return commands; //use this list later to go through commands and execute them
	}
	
	private void printError(int line){
		System.out.println("Syntaxfel på rad " + line);
		System.exit(1); //We dont want to continue if we get and error.
	}
	
	
}
