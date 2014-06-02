/*
 * author : yusi
 * version 2
 */
package communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MySocket{

	private String dest_Host;
	private int dest_Port;
	private ObjectOutputStream os;
	private Socket socket;

	public MySocket(String host, int port) {
		this.dest_Host = host;
		this.dest_Port = port;
		try {
			
			
//			socket = new Socket(this.dest_Host, this.dest_Port, InetAddress.getLocalHost(),this.dest_Port-200);
			socket = new Socket(this.dest_Host, this.dest_Port);
			os = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void send(Object object) {
		try {
			os.flush();
			os.writeObject(object);
			System.out.println("Sending " + object.toString()+ "to" + getDest_Port());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getDest_Host() {return dest_Host;}

	public void setDest_Host(String dest_Host) {this.dest_Host = dest_Host;}

	public int getDest_Port() {return dest_Port;}

	public void setDest_Port(int dest_Port) {this.dest_Port = dest_Port;}


}
