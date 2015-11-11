
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

	/*
	 * for debugging purposes
	 */
	public void myprint(){
		System.out.println(value + " - " + lineNumber);
	}
}
