import java.util.LinkedList;


public class Executer {
	
	Tokenizer t = new Tokenizer();
	Leonardo leo = new Leonardo();
	LinkedList<Command> commands = t.parseInput();

	public Executer() {
		
	}
	
	public void execute(){
		for(Command c : commands){
			//c.getNestcommands();
			executeCommand(c);
		}
	}
	
	private void executeCommand(Command c) {
		//System.out.println(c.getCommand());
		switch(c.getCommand()){
		case "forw":
			leo.changePos(c.getParameter());
			if(leo.isPenEnabled()){
				leo.printInfo();
			}
			
			break;
		case "back":
			leo.changePos(-c.getParameter()); //negative int for backing
			if(leo.isPenEnabled()){
				leo.printInfo();
			}
			
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
		case "rep":
			LinkedList<Command> list = c.getNestcommands();
			for(int i = 0; i < c.getParameter(); i++){
				for(Command com : list){
				executeCommand(com);
				}
			}
			break;
		default:
			//System.out.println("error: " + c.getCommand());
			System.out.println("Fatal error in commands list");
		}
		
	}

}
