package test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Slave {
	public static void main(String[] args) {
		Socket socket;
		ObjectOutputStream os;
		
		try {
			socket = new Socket("127.0.0.1",15640);
			os = new ObjectOutputStream(socket.getOutputStream());
			os.flush();
			String s = "Greeting from Slave";
			os.writeObject(s);
			
			os.close();
			socket.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
