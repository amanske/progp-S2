
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ListIterator;

public class Command {

	private String command;
	private int parameter;
	private String colorCode;
	private int lineNumber;
	private LinkedList<Command> nestcommands;
	
	public Command(String command, int parameter, int lineNumber) {
		this.command = command;
		this.parameter = parameter;
		this.lineNumber = lineNumber;
	}
	public Command(String command, String colorCode, int lineNumber) {
		this.command = command;
		this.colorCode = colorCode;
		this.lineNumber = lineNumber;
	}
	public Command(String command, int lineNumber){
		this.command = command;
		this.lineNumber = lineNumber;
	}

	public Command(Token token, ListIterator<Token> li){
		command = token.getValue();
		lineNumber = token.getLineNumber();
		int errorline = lineNumber;
		switch(command){
		case "up":
		case "down":
			try{
				Token dot = li.next();
				if(!dot.getValue().equals(".")){
					syntaxError(dot.getLineNumber());
				}

			}catch(NoSuchElementException e){
				syntaxError(errorline);
			}
			break;
		case "forw":
		case "back":
		case "right":
		case "left":
			try{
				Token number = li.next();
				errorline = number.getLineNumber();//new error line has been found
				if(number.getValue().matches("^[0-9]+$")){ //is a number
					parameter = Integer.parseInt(number.getValue());
					if(parameter == 0){
						syntaxError(errorline);
					}
				}else{
					syntaxError(errorline);
				}
				Token dot = li.next();
				errorline = dot.getLineNumber();//new error line has been found
				if(!dot.getValue().equals(".")){
					syntaxError(errorline);
				}
			}catch(NoSuchElementException e){
				syntaxError(errorline);
			}
			break;
		case "color":
			try{
				Token color = li.next();
				errorline = color.getLineNumber(); //new error line has been found
				if(color.getValue().matches("^#[A-Fa-f0-9]{6}$")){ //is a number
					colorCode = color.getValue();
				}else{
					syntaxError(errorline);
				}
				Token dot = li.next();
				errorline = dot.getLineNumber(); //new error line has been found
				if(!dot.getValue().equals(".")){
					syntaxError(errorline);
				}
			}catch(NoSuchElementException e){
				syntaxError(errorline);
			}
			break;
		case "rep":
			try{
				Token number = li.next();
				errorline = number.getLineNumber(); //new error line has been found

				if(number.getValue().matches("^[0-9]+$")){ //is a number
					parameter = Integer.parseInt(number.getValue());
					if(parameter == 0){
						syntaxError(errorline);
					}
				}else{
					syntaxError(errorline);
				}

				nestcommands = new LinkedList<Command>();
				Token next = li.next();
				errorline = next.getLineNumber(); //new error line has been found

				if(next.getValue().equals("\"")){ //begin of nest
					boolean finished = false;
					while(!finished){
						next = li.next();
						errorline = next.getLineNumber(); //new error line has been found
						if(next.getValue().equals("\"")){
							if(nestcommands.size() == 0){ //quote cant be empty
								syntaxError(errorline);
							}
							finished = true; //hit end of quote
						}else{
							nestcommands.add(new Command(next, li));
						}
					}
				}else{
					nestcommands.add(new Command(next, li));
				}
			}catch(NoSuchElementException e){
				syntaxError(errorline);
			}
			break;
		default:
			syntaxError(errorline);
		}

	}

	public String getCommand(){
		return command;
	}
	public int getParameter(){
		return parameter;
	}
	public String getColorCode(){
		return colorCode;
	}
	public int getLineNumber(){
		return lineNumber;
	}
	public LinkedList<Command> getNestcommands(){
		return nestcommands;
	}
	public void print() {
		if(!command.equals("rep")){
			System.out.println(command + ", " + parameter + ", " + colorCode + ", " + lineNumber);
		}else{
			for(int i = 0; i < parameter; i++){
				for(Command cmd : nestcommands){
					cmd.print();
				}
			}
		}
		
	}


	private void syntaxError(int line){
		System.out.println("Syntaxfel på rad " + line);
		System.exit(0); //We dont want to continue if we get and error.
	}


}
