package processManager;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import process.MigratableProcess;
import process.TestProcess;

public class MasterProcessManager implements Runnable {

	private String host; // host of current master server
	private int port; // port of current master server
	private ArrayList<SlaveBean> slaveList;
	private ArrayList<MigratableProcess> processList;

	public MasterProcessManager(String host, int port) {
		this.host = host;
		this.port = port;
		slaveList = new ArrayList<SlaveBean>();
		processList = new ArrayList<MigratableProcess>();
	}

	/*
	 * launch process config input: className and args
	 */
	public void launchProcessConfig(String className, String[] args)
			throws SecurityException, NoSuchMethodException {

		try {
			Class<?> processClass = Class.forName(className);
			// System.out.print("processClass is " + processClass.toString());
			MigratableProcess process;
			process = (MigratableProcess)processClass.getConstructor(String[].class).newInstance((Object)args);
//			process = (MigratableProcess) processClass.newInstance();
			System.out.println("MP is " + process.toString());
			processList.add(process);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // TestProcess test = new TestProcess();
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * add slave info to slaveList
	 */
	public void addSlave(String slaveHost, int slavePort) {
		SlaveBean slave = new SlaveBean(slaveHost, slavePort);
		slaveList.add(slave);
	}

	/**
	 * disconnect with slave and collect all processes
	 */
	public void removeSlave(String slaveHost, int slavePort) {
		slaveList.remove(slaveHost);
		// collect remaining process
	}

	/**
	 * build connection to slave via socket
	 */
	public void connSlave(String slaveHost, int slavePort) {

	}

	public SlaveBean getSlave() {
		String host = "127.0.0.1";
		int port = 15641;
		return new SlaveBean(host, port);
	}

	/*
	 * receive process sent from slaves via socket
	 */
	public void receiveProcess() throws IOException, ClassNotFoundException {
		ServerSocket listener = null;
		Socket socket;
		ObjectInputStream in = null;

		try {
			// 1. creating a server socket
			listener = new ServerSocket(getPort());

			while (true) {

				// 2. wait for connection
				System.out.println("Master Waiting for Slave");
				socket = listener.accept();
				System.out.println("Connection received from "
						+ listener.getInetAddress().getHostName());

				// 3.read object from inputstream
				in = new ObjectInputStream(socket.getInputStream());
//				String s = (String) in.readObject();
//				System.out.println("Message Received from slave" + s);

				 MigratableProcess process = (MigratableProcess)
				 in.readObject();
				 processList.add(process);


			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 4.close connection
			in.close();
			listener.close();
		}
	}

	/*
	 * migrate process to slave
	 */
	public void migrateProcess(String slaveHost, int slavePort,
			MigratableProcess process) {
		Socket socket;
		ObjectOutputStream os;
		System.out.println("Master mig to slave " + slaveHost + "with port "
				+ slavePort);
		try {
			socket = new Socket(slaveHost, slavePort);
			os = new ObjectOutputStream(socket.getOutputStream());
			os.flush();
			String s = "Greeting from Master";
			// os.writeObject(s);

			System.out.println("Mig " + process.toString());
			os.writeObject(process);
			os.close();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * check all slaves and balance load of slaves
	 */
	public void heartBeat(String slaveHost, int slavePort) {

	}

	/*
	 * balance load
	 */
	public void balanceSlave() {

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

	@Override
	public void run() {
		System.out.println("MasterManager Running!");
		for(MigratableProcess process : processList) {
			migrateProcess("127.0.0.1", 15644, process);
		}

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
		
		Thread t_migrate = new Thread () {
			@Override
			public void run() {
				super.run();
				while (true) {
					if (processList.size() > 0) {
						for (int i = 0; i < processList.size(); i++) {
							processList.get(i).suspend();
							migrateProcess("127.0.0.1", 15640, processList.get(i));
							processList.remove(i);
						}
					}
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

	}

}
