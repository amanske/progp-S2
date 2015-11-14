/**
 * Defines the turtle Leonardo and his behaviour. Is able to use his pen, move and
 * change the pens color. 
 *
 */

public class Leonardo {

	private double xPos = 0;
	private double xPosOld = 0;
	private double yPos = 0;
	private double yPosOld = 0;
	private int facingAngle = 0;
	private boolean penEnabled = false;
	private String color = "#0000FF";

	public Leonardo() {}
	
	public void changePos(int i) {
		xPosOld = xPos;
		yPosOld = yPos;
		xPos = (xPos + i*Math.cos((Math.PI*facingAngle)/180)); //Basic trigonometry
		yPos = (yPos + i*Math.sin((Math.PI*facingAngle)/180)); //Basic trigonometry
	}

	public void changeAngle(int i) {
		facingAngle += i;
	}

	public void enablePen(boolean b) {
		penEnabled = b;
	}

	public void changeColor(String s) {
		color = s;
	}
	
	public void printInfo(){
		//The expression '%.4f' forces four decimals 
		System.out.print(color + " ");
		System.out.printf("%.4f", xPosOld); 
		System.out.print(" ");
		System.out.printf("%.4f", yPosOld);
		System.out.print(" ");
		System.out.printf("%.4f", xPos);
		System.out.print(" ");
		System.out.printf("%.4f", yPos);
		System.out.println(" ");

	}
	
	public boolean isPenEnabled(){
		return penEnabled;
	}


}
