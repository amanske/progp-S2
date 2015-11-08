import java.util.Scanner;

public class FileReader {
	Scanner sc = new Scanner(System.in);
	String[] knownCommands = {"forw", "back", "left", "right", "down", "up", "color", "rep"};

	public FileReader() {
	}
	
	public String[] parseFile(){ //starting approach
		while(sc.hasNext()){
			//regex implementation here
		}
		return knownCommands; //dummy
		
	}

}
