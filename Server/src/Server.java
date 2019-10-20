package src;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.io.File;

public class Server {
    private static ServerSocket listener;
    private static int serverPort = 0;

    public void checkPortNumber(Scanner input) {
        try {

            System.out.println("Please enter the port number you would like to use: ");

            for (;;) {
                serverPort = input.nextInt();

                if (serverPort >= 5000 && serverPort <= 5050) {
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
        Server server = new Server();
        Scanner input = new Scanner(System.in);

        new File("c:/test");

        int clientNumber = 0;

        String serverAddress = "127.0.0.1";
        server.checkPortNumber(input);

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
		        		String indicator = null;
		        		String enterKey = System.getProperty("line.separator");
		        		if (commands.length > 1) {
		        			lsMessage += "Please do not enter any parameters after ls command.";
		        		} else {
		        			if(lsFile.length == 0) {
		        				lsMessage += "No File/Directory found";
		        			}
		        			for(int i = 0; i < lsFile.length; i++) {
		        				System.out.print(lsFile[i] + " ");
		        				if(lsFile[i].isFile()) {
		        					indicator = "[File] ";
		        				} else {
		        					indicator = "[Folder] ";
		        				}
		        				lsMessage += indicator + lsFile[i].toString().substring(lsFile[i].toString().lastIndexOf("/") + 1) + enterKey;
		        			}
		        		}
		        		out.writeUTF(lsMessage);
		        		break;
		        	case "mkdir":
		        		if (commands.length > 1) {
		        			File newFile = new File(this.currentFile.getPath() + "/" + commands[1]);
		        			newFile.mkdir();
		        			out.writeUTF("Le dossier " + commands[1] + " a été créé.");
		        		} else {
		        			out.writeUTF("Please add a directory name after mkdir");
		        		}
		        		break;
		        	case "cd":
		        		if (commands.length > 1) {
		        			String currentPath = this.currentFile.getAbsolutePath();
		        			File testFile = new File(currentPath + "/" + commands[1]);
		        			if(!testFile.exists()) {
		        				out.writeUTF("This path doesn't exist!");
		        			}
		        			if (commands[1].contains("..")) {
		        				int counter = commands[1].split("/").length;
		        				System.out.print(counter);
		        				for (int i = 0; i < counter; i++) {
		        					this.currentFile = this.currentFile.getParentFile();
		        				}
		        			} else {
		        				this.currentFile = testFile;
		        			}
		        			out.writeUTF("Vous êtes dans le dossier " + this.currentFile.getPath().substring(this.currentFile.getPath().lastIndexOf("/") + 1) + ".");		        					
		        		} else {
		        			out.writeUTF("Please add a directory name after cd");
		        		}
		        		break;
		        	case "upload":
		        		String fileNameU = commands[1]; 
		        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    			byte buffer[] = new byte[16 * 1024];
		    			baos.write(buffer, 0 , inputStream.read(buffer));		    			
		    			byte result[] = baos.toByteArray();
		    			out.writeUTF("Le fichier " + fileNameU + " a bien été téléversé.");		    			
		    			Files.write(Paths.get(this.currentFile.getAbsolutePath() + "/" + fileNameU), result);
		    			baos.close();
		        		break;
		        	case "download":
		        		String fileNameD = commands[1]; 
		        		File file = new File(this.currentFile.getAbsolutePath() + "/" + fileNameD);
				    	if (file.exists()) {
				    		byte[] bytes = new byte[16 * 1024];
				    		bytes = Files.readAllBytes(file.toPath());
				    		out.writeUTF("Le fichier " + fileNameD + " a bien été téléversé.");	
				    		out.write(bytes);
				    	} else {
				    		out.writeUTF("This file doesn't exist.");
				    	}
		        		break;
		        	case "exit": 
		        		out.writeUTF("Vous avez été déconnecté avec succès.");
		        		socket.close();
		        		break;
		        	default: out.writeUTF("This command doesn't exist!");
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
