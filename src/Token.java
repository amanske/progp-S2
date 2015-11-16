/**
 * Defines a Token, including its value and its line number.
 */
public class Token {
	private String value;
	private int lineNumber;
	
	public Token(String value, int lineNumber) {
		this.value = value;
		this.lineNumber = lineNumber;
	}
	
	public String getValue(){
		return value;
	}
	public int getLineNumber(){
		return lineNumber;
	}
}
