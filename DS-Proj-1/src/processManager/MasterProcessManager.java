/*
 * author yusi
 * version 2
 */
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

import communication.MySocket;
import communication.MySocketServer;
import process.MigratableProcess;
import process.RequestBean;
import process.TestProcess;

public class MasterProcessManager implements Runnable {

	private String host; // host of current master server
	private int port; // port of current master server
	public HashMap<SlaveBean,Integer> slaveList; //integer is running process on the slave
	public MySocketServer server;
	public MySocket socekt;
	public ArrayList<MigratableProcess>initProcessList;
	public HashMap<Integer, HashMap<Integer,MigratableProcess>> slaveProcessList;

	public MasterProcessManager(String host, int port) {
		this.host = host;
		this.port = port;
		slaveList = new HashMap<SlaveBean,Integer>();
		initProcessList = new ArrayList<MigratableProcess>();
		server = new MySocketServer(this.port);
		slaveProcessList = new HashMap<Integer, HashMap<Integer,MigratableProcess>>();
	}

	/*
	 * launch process config input: className customized name and  args
	 */
	public void launchProcessConfig(String className,String[] args)
			throws SecurityException, NoSuchMethodException {

		try {
			Class<?> processClass = Class.forName(className);
			MigratableProcess process;
			process = (MigratableProcess)processClass.getConstructor(String[].class).newInstance((Object)args);
			System.out.println("MP is " + process.toString());
			initProcessList.add(process);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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
	}




	/*
	 * listening port
	 */
	public void listen() {
		//thead
		server.listen();
		
		Thread t = new Thread (){
			MigratableProcess process;
			SlaveBean slave;
			HashMap<Integer,MigratableProcess>processList;
			@Override
			public void run() {
				super.run();
				while(true) {
					if(server.getObject()!=null && server.getObject().toString().equals("grep")){
						//get process
						//check best slave and migrate process to it.
						MigratableProcess process = (MigratableProcess)server.getObject();
						migrateProcessBest(process);
						server.setObject(null);
					}
					if (server.getObject()!=null && server.getObject().toString().equals("bean")){
						// get slave bean
						//update slave bean
						
						SlaveBean slave = (SlaveBean) server.getObject();
						slaveList.put(slave, slave.getCurCount());
						System.out.println("get count" + slave.getPort() + " count " + slave.getCurCount());
						server.setObject(null);
					} else if(server.getObject()!=null && server.getObject().getClass().isInstance(processList)){
						HashMap<Integer,MigratableProcess> processList = (HashMap<Integer, MigratableProcess>) server.getObject();
						
						slaveProcessList.put(server.getSocket().getPort(), processList);
						server.setObject(null);
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		t.start();
	}
	
	/*
	 * send to slave
	 */
	public void send(String host, int port, Object obj) {
		socekt = new MySocket(host, port);
		socekt.send(obj);		
	}
	
	/*
	 * migrate to the best
	 */
	public void migrateProcessBest(MigratableProcess process)  {
		try {
			checkSlave();
			Thread.sleep(1000);
			SlaveBean bestSlave = getBestSlave();
			Thread.sleep(1000);
			send(bestSlave.getHost(), bestSlave.getPort(), process);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * init migrate process
	 */
	public void initMigrate() throws InterruptedException {
		for(MigratableProcess process : initProcessList) {
			migrateProcessBest(process);
			Thread.sleep(1000);
		}
	}
	
	/*
	 * check every slave and its availability
	 */
	public void checkSlave () {
		for(SlaveBean slave : slaveList.keySet()) {
			send(slave.getHost(),slave.getPort(),"checkAval");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	/*
	 * get best slave to migarate/min of process on a slave
	 */
	public SlaveBean getBestSlave(){
		Entry<SlaveBean, Integer> min = null;
		for(Entry<SlaveBean, Integer> entry : slaveList.entrySet()) {
			System.out.println("ip "+ entry.getKey().getPort() + "value" + entry.getValue());
			if (min == null || min.getValue() > entry.getValue()) {
		        min = entry;
		    }
		}
		System.out.println("Min ip "+ min.getKey().getPort() + "value" + min.getValue());
		return min.getKey();
	}
	
	/*
	 * request mig 
	 */
	public void requestMig(String host,int port, int processId) {
		send(host, port, processId);
	}
	/*
	 * request process info on slave
	 */
	public void requestProessInfo(String host, int port) {
		send(host, port, "checkInfo");
	}


	/*
	 * get slave process info
	 */
	
	public void getSlaveProcessInfo() {
		for (Integer port : slaveProcessList.keySet()) {
			System.out.print(port + " ");
			for(Integer processId : slaveProcessList.get(port).keySet()) {
				System.out.print(processId + " ");
				System.out.print(slaveProcessList.get(port).get(processId).toString());
			}
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

	@Override
	public void run() {
		listen();
	}

}
