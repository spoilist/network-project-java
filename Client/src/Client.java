import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	
	private static String ipAddress = "";
	private static int portNumber = 0;
	// /Users/felix-antoinebourbonnais/Documents/A2019/inf3405/myfile.txt
	
	public void checkIpAddress(Scanner input) {
		try {
			
			System.out.println("Please enter the server's IP address: ");
			
			for (;;) {
				ipAddress = input.nextLine();
				
				if (validIP(ipAddress)) {
					break;
				}
				
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
	
	public void checkPortNumber(Scanner input) {
		try {
			
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
	
	
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Hello! Welcome to your new favorite file manager! \n");
		client.checkIpAddress(input);
		client.checkPortNumber(input);
		
		socket = new Socket(ipAddress, portNumber);
		
		System.out.format("The server is running on %s:%d%n", ipAddress, portNumber);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		String command = "";
		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		while (!command.equals("exit")) {
		    System.out.println("Enter command: \n");
		    command = input.nextLine();
		    String[] commands = command.split(" ", 2);
		    
		    if (commands[0].equals("upload")) {
		    	// upload(commands[1], command, dataOutputStream, outputStream);
		    	File file = new File(commands[1]);
		    	if (file.exists()) {
		    		dataOutputStream.writeUTF(command);
		    		byte[] bytes = new byte[16 * 1024];
		    		bytes = Files.readAllBytes(file.toPath());
		    		
		    		outputStream.write(bytes);
		    		
		    		String message = in.readUTF();
			    	System.out.println(message);
		    	} else {
		    		System.out.println("This file doesn't exist.");
		    	}	
		    } else if (commands[0].equals("download")) {
		    	dataOutputStream.writeUTF(command);
		    	String message = in.readUTF();
		    	if (!message.equals("This file doesn't exist.")) {
		    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    		byte buffer[] = new byte[16 * 1024];
		    		baos.write(buffer, 0 , in.read(buffer));		    			
		    		byte result[] = baos.toByteArray();		    		
		    		Files.write(Paths.get(commands[1]), result);
		    		baos.close();	
		    	}
    			
		    	System.out.println(message);
		    	
		    } else {
		    	dataOutputStream.writeUTF(command);		    	
		    	String message = in.readUTF();
		    	System.out.println(message);
		    }	        
		}
		
		input.close();
		dataOutputStream.close();
		
		socket.close();
	}
	/*
	public static void upload(String fileName, String command, DataOutputStream dataOutputStream, 
			OutputStream outputStream) {
		File file = new File(fileName);
    	if (file.exists()) {
    		dataOutputStream.writeUTF(command);
    		byte[] bytes = new byte[16 * 1024];
    		bytes = Files.readAllBytes(file.toPath());
    		
    		outputStream.write(bytes);
    		
    		String message = in.readUTF();
	    	System.out.println(message);
    	} else {
    		System.out.println("This file doesn't exist.");
    	}	
	}*/
}
