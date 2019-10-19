package src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.File;

public class Server {
	private static ServerSocket listener;
	
	public static void main(String[] args) throws Exception {
		
		new File("c:/test");

		int clientNumber = 0;
		
		String serverAddress = "127.0.0.1";
		int serverPort = 5000;
		
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIp = InetAddress.getByName(serverAddress);
		
		listener.bind(new InetSocketAddress(serverIp, serverPort));
		
		System.out.format("Server running on %s:%d%n", serverAddress, serverPort);
		
		try {
			while(true) {
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}
}

class ClientHandler extends Thread {
	private Socket socket;
	private int clientNumber;
	private File currentFile;
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.currentFile = new File("test");
		if(!this.currentFile.exists()) {
			boolean created = this.currentFile.mkdir();
			if(created) {
				System.out.println("Created base File!");
			} else {
				System.out.println("Error while creating base File!");
			}			
		}
		System.out.println("New connection with client #" + clientNumber + " at " + socket);
	}
	
	public void run() {
		try {
			
			// OUTPUT
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			out.writeUTF("Hello from server - you are client #" + clientNumber + " on thread " + Thread.currentThread().getId());
			
			// INPUT
			String message = "";
			InputStream inputStream = socket.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			while(!message.equals("exit")) {
		        message = dataInputStream.readUTF();
		        String baseMessage = "[" + this.socket.getInetAddress() + ":" + this.socket.getLocalPort() + " - " + LocalDate.now() + "@" + LocalTime.now() + "]: ";
		        System.out.println(baseMessage + message);
		        String[] commands = message.split(" ", 2); 

		        switch(commands[0]) {
		        	case "ls":
		        		File[] lsFile = this.currentFile.listFiles();
		        		String lsMessage = "";
		        		String enterKey = System.getProperty("line.separator");
		        		if(lsFile.length == 0) {
		        			lsMessage += "No File/Directory found";
		        		}
		        		for(int i = 0; i < lsFile.length; i++) {
		        			System.out.print(lsFile[i] + " ");
		        			if(!lsFile[i].isFile()) {
		        				lsMessage += "[Folder] " + lsFile[i] + enterKey;		        				
		        			} else {
		        				lsMessage += "[File] " + lsFile[i] + enterKey;
		        			}
		        		}
		        		System.out.println();
		        		out.writeUTF(lsMessage);
		        		break;
		        	case "mkdir":
		        		if (commands.length > 1) {
		        			File newFile = new File(this.currentFile.getPath() + "/" + commands[1]);
		        			newFile.mkdir();
		        			out.writeUTF("Le dossier " + commands[1] + "a été créé.");
		        		} else {
		        			System.out.println("Please add a directory name after mkdir");
		        		}
		        		break;
		        	case "cd":
		        		if (commands.length > 1) {
		        			File testFile = new File(this.currentFile.getPath() + "/" + commands[1]);
		        			if(testFile.exists()) {
		        				this.currentFile = new File(this.currentFile.getPath() + "/" + commands[1]);
		        				out.writeUTF("Vous êtes dans le dossier " + commands[1] + ".");
		        			} else {
		        				System.out.println("This path doesn't exist!");
		        			}
		        		} else {
		        			System.out.println("Please add a directory name after cd");
		        		}
		        		break;
		        	case "upload":
		        		
		        		break;
		        	case "download":
		        		
		        		break;
		        	case "exit": 
		        		socket.close();
		        		break;
		        	default: System.out.println("This command doesn't exist!");
		        }
			}
		} catch(IOException e) {
			System.out.println("Error handling client #" + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				System.out.println("Couldn't close socket!");				
			}
			System.out.println("Connection with client #" + clientNumber + " closed!");
		}
	}
}