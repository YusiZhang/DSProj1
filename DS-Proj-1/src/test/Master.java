package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		ServerSocket listener = null;
		Socket socket;
		ObjectInputStream in = null;
		
		try {
			//1. creating a server socket
			listener = new ServerSocket(15640);
			
			while(true) {
				
				//2. wait for connection
				System.out.println("Waiting for connection");
				socket = listener.accept();
				System.out.println("Connection received from " + listener.getInetAddress().getHostName());
				
				//3.read object from inputstream
				in = new ObjectInputStream(socket.getInputStream());
				String s = (String)in.readObject();
				System.out.println("Message Received from slave" + s);
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			//4.close connection
			in.close();
			listener.close();
		}
	}
}
