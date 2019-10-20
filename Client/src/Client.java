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
	// /Users/felix-antoinebourbonnais/Documents/A2019/inf3405/myfile.txt
	public static void main(String[] args) throws Exception {
		String SERVER_ADDRESS = "127.0.0.1";
		int PORT = 5000;
		
		socket = new Socket(SERVER_ADDRESS, PORT);
		
		System.out.format("The server is runnin on %s:%d%n", SERVER_ADDRESS, PORT);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		String command = "";
		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		Scanner scanner = new Scanner(System.in);
		
		while (!command.equals("exit")) {
		    System.out.println("Enter command");
		    command = scanner.nextLine();
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
		
		scanner.close();
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
