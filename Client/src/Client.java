import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
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
		    	dataOutputStream.writeUTF(command);
		    	//File file = new File("myfile.txt");
		        //byte[] bytes = new byte[16 * 1024];
		        //bytes = Files.readAllBytes(file.toPath());
		    	String a = "a";
		        
		        // sending data to server
		        outputStream.write(a.getBytes());
		        
		        // Receiving reply from server
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte buffer[] = new byte[1024];
				baos.write(buffer, 0 , in.read(buffer));
				
				byte result[] = baos.toByteArray();

				String res = Arrays.toString(result);

				// printing reply to console
				System.out.println("Recieved from server : " + res);
				baos.close();
		    } else {
		    	dataOutputStream.writeUTF(command);		    	
		    }
	        
	        // String message = in.readUTF();
	        // System.out.println(message);
		}
		
		scanner.close();
		dataOutputStream.close();
		
		socket.close();
	}
}
