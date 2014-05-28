package lab1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class MasterProcessManager implements Runnable{
	
	
	private String host; //host of current master server
	private int port; //port of current master server
	private ArrayList<SlaveBean> slaveList;

	/*
	 * launch process
	 */
	public void launchProcess(){
		//using test class for testing
		TestProcess testProcess = new TestProcess();
		testProcess.run();
		testProcess.suspend();
		
		migrateProcess("127.0.0.0", 9998, testProcess);
		
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
