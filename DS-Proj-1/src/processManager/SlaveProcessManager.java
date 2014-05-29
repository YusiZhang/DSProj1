package processManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import process.MigratableProcess;

public class SlaveProcessManager {
	private String host;
	private int port;

	SlaveProcessManager(String host, int port) {
		this.host = host;
		this.port = port;

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
				MigratableProcess testProcess = (MigratableProcess) in
						.readObject();
				// console log
				System.out.println("Slave receive" + testProcess.toString());
				in.close();
				socket.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
			listener.close();
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

}
