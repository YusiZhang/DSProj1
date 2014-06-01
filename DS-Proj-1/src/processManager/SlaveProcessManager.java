package processManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.TableColumn;

import process.MigratableProcess;

public class SlaveProcessManager implements Runnable {
	private String host;
	private int port;
	private SlaveBean slaveBean;
	private ArrayList<MigratableProcess> processList;

	public SlaveProcessManager(String host, int port) {
		this.host = host;
		this.port = port;
		slaveBean = new SlaveBean(host, port);
		processList = new ArrayList<MigratableProcess>();
	}

	/*
	 * receive process sent from master via socket
	 */
	public void receiveProcess() throws IOException, ClassNotFoundException {

		ServerSocket listener = null;
		Socket socket;
		ObjectInputStream in = null;
		
		try {
			//1. creating a server socket
			listener = new ServerSocket(getPort());
			
			while(true) {
				
				//2. wait for connection
				System.out.println("Waiting for connection");
				socket = listener.accept();
//				System.out.println("Connection received from " + listener.getInetAddress().getHostName());
				
				//3.read object from inputstream
				in = new ObjectInputStream(socket.getInputStream());
				MigratableProcess process = (MigratableProcess)in.readObject();
				//3.1 run process and add them to list
				processList.add(process);
				MigratableProcess mp = processList.get(0);
				Thread t = new Thread(mp);
				t.start();
				//system log
				System.out.println("Message Received from Master" + process.toString());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			//4.close connection
			in.close();
			listener.close();
		}
	}

	/*
	 * migrate process to master
	 */
	public void migrateProcess(String masterHost, int masterPort,
			MigratableProcess process) {
		Socket socket;
		OutputStream os;
		try {
			socket = new Socket(masterHost, masterPort);
			os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(process);
			oos.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void run()  {

		// log start
		System.out.println("Slave process manager starts!");
		
		Thread t_receive = new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					receiveProcess();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		};
		
		Thread t_migrate = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true) {
					if(processList.size() > 0) {
						for (int i = 0; i < processList.size(); i++) {
							processList.get(i).suspend();
							migrateProcess("127.0.0.1", 15640, processList.get(i));
							processList.remove(i);
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

//				while(true) {
//					if(processList.size() > 0) {
//						processList.get(0).suspend();
//						migrateProcess("127.0.0.1", 15640, processList.get(0));
//						processList.remove(0);
//					}
//				}
				
				
			}
		};
		
		
		try {
			t_receive.start();
			Thread.sleep(5000);
			t_migrate.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		MigratableProcess g1 = processList.get(0);
//
//		Thread t1 = new Thread(g1);
//		t1.start();
//		Thread.sleep(2000);
//		g1.suspend();
//
//		migrateProcess("127.0.0.1", 15640, g1);//to master
	}

}
