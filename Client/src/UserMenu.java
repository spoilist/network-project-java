import java.util.Scanner;

public class UserMenu {

	public static void main(String[] args) {
		try {
			Scanner input = new Scanner(System.in);
			int portNumber = 0;
			
			System.out.println("Hello! Welcome to your new favorite file manager!");
			System.out.println("Please enter the port number you would like to use: ");
			
			for (;;) {
				portNumber = input.nextInt();
				
				if (portNumber >= 5000 && portNumber <= 5005) {
					break;
				}
				
				System.out.println("The port number must be an integer between 5000 and 5050.");
				System.out.println("Please enter a valid port number you would like to use: ");
			}
		} catch (Exception e) {
			System.out.println("The port number must be an integer between 5000 and 5050. Please try again.");
		}
	}
}
