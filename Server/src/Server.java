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

public class Server {
	private static ServerSocket listener;
	
	public static void main(String[] args) throws Exception {
		tree<String> tree = new tree<String>("root");
		
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
				new ClientHandler(listener.accept(), clientNumber++, tree).start();
			}
		} finally {
			listener.close();
		}
	}
}

class ClientHandler extends Thread {
	private Socket socket;
	private int clientNumber;
	private tree<String> tree;
	
	public ClientHandler(Socket socket, int clientNumber, tree<String> tree) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.tree = tree;
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
		        		for (int i = 0; i < this.tree.getChildren().size(); i++) {
		        			System.out.println(this.tree.getChildren().get(i).getData());
		        		}
		        		break;
		        	case "mkdir":
		        		if (commands.length > 1) {
		        			this.tree.addChild(commands[1]);		        			
		        		} else {
		        			System.out.println("Please add a directory name after mkdir");
		        		}
		        		break;
		        	case "cd":
		        		if (commands.length > 1) {
		        			for (int i = 0; i < this.tree.getChildren().size(); i++) {
		        				if(this.tree.getChildren().get(i).getData() == commands[1]) {
		        					this.tree.move(this.tree.getChildren().get(i));
		        				}
		        			}		     	
		        		} else {
		        			System.out.println("Please add a directory name after cd");
		        		}
		        		break;
		        	case "exit": break;
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
