
public class Command {

	private String command;
	private int parameter;
	private String colorCode;
	private int lineNumber;
	
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
	public void print() {
		System.out.println(command + ", " + parameter + ", " + colorCode + ", " + lineNumber);
		
	}




}
