package comp1206.sushi;

public class Launcher {

	public static void main(String[] argv) {		
		if(argv.length > 0 && argv[0].equals("client")) {
			System.out.println("Running client");
			ClientApplication.main(argv);
		} else {
			System.out.println("Running server");
			ServerApplication.main(argv);
		}
	}
}
