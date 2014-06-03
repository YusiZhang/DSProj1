/*
 * yusi 
 * version 2
 */
package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
	private ServerSocket listener;
	private Socket socket;
	private ObjectInputStream in;
	private Object object;
	private int host_Port;
	
	public  MySocketServer (int port) {
		this.host_Port = port;
		
		try {
			listener = new ServerSocket(this.host_Port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen () {
		Thread t = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true) {
					try {
						System.out.println("Listening " + getHost_Port());
						socket = listener.accept();
						System.out.println("Received from " + socket.getPort());
						in = new ObjectInputStream(socket.getInputStream());
						object = in.readObject();
						System.out.println("Received is" + object.toString());
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				
			}
		};
		t.start();
		
	}
	
	public void close() {
		try {
			in.close();
			socket.close();
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ServerSocket getListener() {
		return listener;
	}

	public void setListener(ServerSocket listener) {
		this.listener = listener;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getHost_Port() {
		return host_Port;
	}

	public void setHost_Port(int host_Port) {
		this.host_Port = host_Port;
	}
	
	
	
	
}
