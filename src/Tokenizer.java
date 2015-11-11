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
	//StringBuilder sb = new StringBuilder("% Syntaxfel: saknas punkt.\nDOWN \n% Om filen tar slut mitt i ett kommando\n% så anses felet ligga på sista raden");
	StringBuilder sb= new StringBuilder("up. down. %color bababa\n forw 1.	down.");
	
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
		//If repetition argument is started
		boolean repinit = false;
		//Number of nested citation characters
		int citedepth = 0;
		int index = 0; 

		boolean isComment = false;
		int linenumber = 1;
		StringBuilder temp = new StringBuilder();
		char[] inputChars = sb.toString().toCharArray();
		int charlength = inputChars.length;
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
					repinit = true;
					citedepth = 1;
					temp.append(c);
					break;
				default:
					temp.append(c);
				}
			}else if(repinit){
				switch(c){
				case '"': //Checks if end of token or beginning of nested rep
					if(index < charlength-1){
						char nextcharindex = inputChars[index + 1]; 
						if(nextcharindex == '"' || nextcharindex == ' ' || nextcharindex == '\n'){
							citedepth -= 1;
						}else{
							citedepth += 1;
						}
					}
					temp.append(c);
					break;
				default:
					temp.append(c);
				}
				if(citedepth == 0){
					repinit = false; 
					//token will be added next iteration
				}
			}
			
			if (c == '\n') { //reached new line
				isComment = false;
				linenumber++;
			}

			index++;
		}
		//OBS!! If there is anything left in the stringbuilder, it means that it will have an error
		//Since it does not end with a dot. This error is handled later in createCommands.
		if(temp.length() != 0){
			tokens.add(new Token(temp.toString(), linenumber)); 
			temp = new StringBuilder(); 
		}
		
   // 	for(Token token: tokens){
   // 		token.myprint();
   // 	}
		createCommands(tokens); //Call with list of tokens to achieve list of executable commands.
	}
	
	private void upDown(ListIterator<Token> li, LinkedList<Command> commands, Token t, String value, int line){
		try{
			Token dot = li.next();
			if(dot.getValue().equals(".")){
				commands.add(new Command(value, line));
			}else{
				printError(dot.getLineNumber()); //TODO: Get right linenumber
			}
		}catch(Exception e){
			printError(line); //TODO: Correct linenumber
		}
	}
	
	private void color(ListIterator<Token> li, LinkedList<Command> commands, Token t, String value, int line){
		try{
			Token color = li.next();
			Token colordot = li.next();
			String colorcode = color.getValue();
			if(colordot.getValue().equals(".")){
				if(colorcode.matches("^#[A-Fa-f0-9]{6}$")){ //Example: #123AbC
					commands.add(new Command(value, colorcode, line));
				}else{
					printError(color.getLineNumber()); //Failed on color line
				}
			}else{
				printError(colordot.getLineNumber()); //TODO: Get right linenumber
			}
		}catch(NoSuchElementException e){
			printError(line); //TODO: Correct linenumber
		}
	}
	
	private void leftRightForwBack(ListIterator<Token> li, LinkedList<Command> commands, Token t, String value, int line){
		if(knownCommands.contains(value)){
			try{
				Token parameter = li.next();
				Token defaultdot = li.next();
				if(defaultdot.getValue().equals(".")){
					try{
						commands.add(new Command(value, Integer.parseInt(parameter.getValue()), line));
					}catch(NumberFormatException e){ //parameter is not an int, parseInt fails.
						printError(parameter.getLineNumber()); //Fails on parameter line 
					}
				}else{
					printError(defaultdot.getLineNumber()); //TODO: Get right linenumber
				}
			}catch(NoSuchElementException e){
				printError(line); //TODO: Correct linenumber
			}
		}else{
			printError(line); //Fails on command line
		}
	}
	
	private void rep(ListIterator<Token> li, LinkedList<Command> commands, Token t, String value, int line){
		
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
				upDown(listIterator, commands, t, value, linenumber);
				break;
			case "color":
				color(listIterator, commands, t, value, linenumber);
				break;
			case "rep":
				//TODO: Add code for case rep
				try{
					Token parameter = listIterator.next();
					Token sequence = listIterator.next();
					
				}catch(NoSuchElementException e){
					printError(linenumber);
				}
				break;
			default:
				leftRightForwBack(listIterator, commands, t, value, linenumber);
			
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
