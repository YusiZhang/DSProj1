package processManager;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import process.MigratableProcess;


public class MasterProcessManager implements Runnable{
	
	
	private String host; //host of current master server
	private int port; //port of current master server
	private ArrayList<SlaveBean> slaveList;

	/*
	 * launch process
	 */
	public void launchProcess(String className){
		Class<?> processClass;
		try {
			processClass = Class.forName(className);
			Constructor[] ctors = processClass.getConstructors();
			Constructor ctor = null;
			ctor = ctors[0];
			String [] args = {"zhang","inFile.txt","outFile.txt"};
			MigratableProcess process = (MigratableProcess) ctor.newInstance(args);


			process.run();
			process.suspend();
//			migrateProcess(slaveHost, slavePort, process);
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
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
	public void addSlave(String slaveHost, int slavePort){
		SlaveBean slave = new SlaveBean(slaveHost, slavePort);
		slaveList.add(slave);
	}
	
	/**
	 * disconnect with slave and collect all processes
	 */
	public void removeSlave(String slaveHost, int slavePort){
		slaveList.remove(slaveHost);
		//collect remaining process
	}
	
	/**
	 * build connection to slave via socket
	 */
	public void connSlave(String slaveHost, int slavePort){
		
	}
	
	public SlaveBean getSlave(){
		String host = "127.0.0.1";
		int port = 9998;
		return new SlaveBean(host,port);
	}
	 
	/*
	 * receive process sent from slaves via socket
	 */
	public void receiveProcess() throws IOException, ClassNotFoundException{
		ServerSocket listener = null;
		Socket socket;
		ObjectInputStream in;
		
		try {
			listener = new ServerSocket(getPort());
			while(true){
				socket = listener.accept();
				in = new ObjectInputStream(new ObjectInputStream(socket.getInputStream()));
				MigratableProcess testProcess = (MigratableProcess) in.readObject();
				//console log
				System.out.println("MasterProcess receive" + testProcess.toString());
				in.close();
				socket.close();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			listener.close();
		} 
	}
	
	/*
	 * migrate process to slave
	 */
	public void migrateProcess(String slaveHost, int slavePort, MigratableProcess process){
		Socket socket;
		OutputStream os;
		try {
			socket = new Socket(slaveHost,slavePort);
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
	 * check all slaves and balance load of slaves
	 */
	public void heartBeat(String slaveHost, int slavePort){
		
	}
	
	/*
	 * balance load
	 */
	public void balanceSlave(){
		
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
		
		
	}

}
