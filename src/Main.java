import java.util.LinkedList;

public class Main {

	public Main() {
		//This is a test
		//THIS IS THE SECOND EDIT
	}

	public static void main(String[] args) {
		Tokenizer t = new Tokenizer();
		Leonardo leo = new Leonardo();
		LinkedList<Command> commands = t.parseInput(); //Test tokenizer class. Assumes that System.in -> StringBuilder works (which it does).
		for(Command c : commands){
			switch(c.getCommand()){
			case "forw":
				if(leo.isPenEnabled()){
					leo.changePos(c.getParameter());
				}
				leo.printInfo();
				break;
			case "back":
				if(leo.isPenEnabled()){
					leo.changePos(-c.getParameter()); //negative int for backing
				}
				leo.printInfo();
				break;
			case "down":
				leo.enablePen(true);
				break;
			case "up":
				leo.enablePen(false);
				break;
			case "right":
				leo.changeAngle(-(c.getParameter()%360)); //negative int for right turn, mod 360 for periodic match
				break;
			case "left":
				leo.changeAngle(c.getParameter()%360); //int for left turn, mod 360 for periodic match
				break;
			case "color":
				leo.changeColor(c.getColorCode());	
				break;
			default:	
				System.out.println("Fatal error in commands list");
			}
			
		}
		//t.parseInput(); //Reading from System.in
		

	}

}
