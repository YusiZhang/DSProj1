package processManager;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import process.MigratableProcess;
import process.TestProcess;

public class MasterProcessManager implements Runnable {

	private String host; // host of current master server
	private int port; // port of current master server
	public HashMap<SlaveBean,Integer> slaveList; //integer is running process on the slave
	
	public ArrayList<MigratableProcess> processList;

	public MasterProcessManager(String host, int port) {
		this.host = host;
		this.port = port;
		slaveList = new HashMap<SlaveBean,Integer>();
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
		slaveList.put(slave, 0);
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
	 * migrate to the best
	 */
	
	public void migrateProcessBest(MigratableProcess process) {
		SlaveBean bestSlave = getBestSlave();
		migrateProcess(bestSlave.getHost(), bestSlave.getPort(), process);
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
	 * check every slave and its availability
	 */
	
	public void checkSlave() throws IOException, ClassNotFoundException{
		ServerSocket listener = null;
		Socket socket;
		ObjectInputStream in = null;
		
		try {
			//1. creating a server socket
			listener = new ServerSocket(15440);
			
			while(true) {
				
				//2. wait for connection
				System.out.println("Waiting for connection");
				socket = listener.accept();
				
				//3.read object from inputstream
				in = new ObjectInputStream(socket.getInputStream());
				SlaveBean slave = (SlaveBean)in.readObject();
				slaveList.put(slave, slave.getCurCount());

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
	 * get best slave to migarate/min of process on a slave
	 */
	public SlaveBean getBestSlave(){
		Entry<SlaveBean, Integer> min = null;
		for(Entry<SlaveBean, Integer> entry : slaveList.entrySet()) {
			if (min == null || min.getValue() > entry.getValue()) {
		        min = entry;
		    }
		}
		return min.getKey();
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
		
		
		Thread t_checkSlave = new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					checkSlave();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		try {
			t_receive.start();
			Thread.sleep(5000);
			t_checkSlave.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
