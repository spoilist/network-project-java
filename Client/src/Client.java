import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
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
		    	File file = new File("myfile.txt");
		    	System.out.println(file.getAbsolutePath());
		    	byte[] fileContent = Files.readAllBytes(file.toPath());
		    	dataOutputStream.writeLong(file.length());
		    	dataOutputStream.write(fileContent);
		    } else {
		    	dataOutputStream.writeUTF(command);		    	
		    }
	        
	        String message = in.readUTF();
	        System.out.println(message);
		}
		
		scanner.close();
		dataOutputStream.close();
		
		socket.close();
	}
}
