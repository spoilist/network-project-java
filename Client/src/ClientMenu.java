import java.util.Scanner;

public class ClientMenu {
	
	private static int portNumber = 0;

	public void checkPortNumber() {
		try {
			Scanner input = new Scanner(System.in);
			
			System.out.println("Please enter the port number you would like to use: ");
			
			for (;;) {
				portNumber = input.nextInt();
				
				if (portNumber >= 5000 && portNumber <= 5050) {
					break;
				}
				
				System.out.println("The port number must be an integer between 5000 and 5050.");
				System.out.println("Please enter a valid port number you would like to use: ");
			}
			
		} catch (Exception e) {
			System.out.println("The port number must be an integer between 5000 and 5050. Please try again.");
		}
		System.out.println("Valid port number entered.");
	}
	
	public void checkIpAddress() {
		try {
			Scanner input = new Scanner(System.in);
			
			System.out.println("Please enter the port number you would like to use: ");
			
			for (;;) {
				portNumber = input.nextInt();
				
				if (portNumber >= 5000 && portNumber <= 5050) {
					break;
				}
				
				System.out.println("The port number must be an integer between 5000 and 5050.");
				System.out.println("Please enter a valid port number you would like to use: ");
			}
			
		} catch (Exception e) {
			System.out.println("The port number must be an integer between 5000 and 5050. Please try again.");
		}
	}
	
	public static void main(String[] args) {
		ClientMenu clientMenu = new ClientMenu();
		
		System.out.println("Hello! Welcome to your new favorite file manager!");
		clientMenu.checkPortNumber();
	
			
	}
	
}
