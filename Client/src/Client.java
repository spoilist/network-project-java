import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	
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
		    System.out.println(command);
		    
	        dataOutputStream.writeUTF(command);
	        dataOutputStream.flush();
	        
	        String message = in.readUTF();
	        System.out.println(message);
		}
		
		scanner.close();
		dataOutputStream.close();
		
		socket.close();
	}
}