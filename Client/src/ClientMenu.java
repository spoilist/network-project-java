import java.util.Scanner;

public class ClientMenu {
	
	private static int portNumber = 0;
	private static String ipAddress = "";

	public void checkPortNumber() {
		try (Scanner input = new Scanner(System.in)) {
			
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
		System.out.println("Valid port number entered. \n");
	}
	
	public void checkIpAddress() {
		try (Scanner input = new Scanner(System.in)) {
			
			System.out.println("Please enter the server's IP address: ");
			
			for (;;) {
				portNumber = input.nextInt();
				System.out.println("1");
				if (validIP(ipAddress)) {
					System.out.println("2");
					break;
				}
				System.out.println("3");
				System.out.println("The IP address must be composed of 4 bytes separated by periods.");
				System.out.println("Please enter a valid IP address: ");
			}
			
		} catch (Exception e) {
			System.out.println("The IP address must be composed of 4 bytes separated by periods. "
					+ "Please restart the application and try again.");
		}
		System.out.println("Valid IP address entered. \n");
	}
	
	public boolean validIP (String ip) {
	    try {
	        if ( ip == null || ip.isEmpty() ) {
	            return false;
	        }

	        String[] parts = ip.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt( s );
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if ( ip.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	public static void main(String[] args) {
		ClientMenu clientMenu = new ClientMenu();
		
		System.out.println("Hello! Welcome to your new favorite file manager! \n");
		clientMenu.checkPortNumber();
		clientMenu.checkIpAddress();
	
			
	}
	
}
