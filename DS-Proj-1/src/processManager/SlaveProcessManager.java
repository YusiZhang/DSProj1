package processManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import process.MigratableProcess;

public class SlaveProcessManager implements Runnable{
	private String host;
	private int port;
	private SlaveBean slaveBean;
	private ArrayList<MigratableProcess> processList;

	SlaveProcessManager(String host, int port) {
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
		ObjectInputStream in;

		try {
			listener = new ServerSocket(getPort());
			while (true) {
				socket = listener.accept();
				in = new ObjectInputStream(new ObjectInputStream(
						socket.getInputStream()));
				MigratableProcess receivedProcess = (MigratableProcess) in
						.readObject();
				// console log
				System.out.println("Slave receive" + receivedProcess.toString());
				
				//add process to arraylist
				if(slaveBean.addProcess()){//slaveBean's count is added here
					processList.add(receivedProcess);
				}else {
					//exception!!!!!!!
					throw new Exception("Slave machine is full!");
				}

					

				
				in.close();
				socket.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
			listener.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * migrate process to master
	 */
	public void migrateProcess(String masterHost, int masterPort, MigratableProcess process) {
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

	/*
	 * launch process on slave machine
	 */
	public void launchProcess(){
		for (MigratableProcess process : processList) {
			//risk of running twice????????????
			process.run();	
		}
	}
	
	public String getHost() {return host;}

	public void setHost(String host) {this.host = host;}

	public int getPort() {return port;}

	public void setPort(int port) {this.port = port;}

	
	public void run() {

		//log start
		System.out.println("Slave process manager starts!");
	}

}
