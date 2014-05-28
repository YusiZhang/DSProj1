package lab1;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterProcessManager implements Runnable{
	
	
	private String host; //host of current master server
	private int port; //port of current master server
	private HashMap<String,Integer> slave;
	private ArrayList<HashMap<String, Integer>> slaveList;

	/*
	 * launch process
	 */
	public void launchProcess(){
		
	}
	

	/*
	 * add slave info to slaveList
	 */
	public void addSlave(String slaveHost, int slavePort){
		slave.put(slaveHost,slavePort);
		slaveList.add(slave);
	}
	
	/**
	 * disconnect with slave and collect all processes
	 */
	public void removeSlave(String slaveHost, int slavePort){
		
	}
	
	/**
	 * build connection to slave
	 */
	public void connSlave(String slaveHost, int slavePort){
		
	}
	 
	/*
	 * receive process sent from slaves
	 */
	public void receiveProcess(){
		
	}
	
	/*
	 * migrate process to slave
	 */
	public void migrateProcess(String slaveHost, int slavePort){
		
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
