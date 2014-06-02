/*
 * author : yusi
 * version 2
 */
package processManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.table.TableColumn;

import communication.MySocket;
import communication.MySocketServer;
import process.MigratableProcess;

public class SlaveProcessManager implements Runnable {
	private String host;
	private int port;
	private SlaveBean slaveBean;
	public HashMap<Integer,MigratableProcess>processList;
	public MySocketServer server;
	public MySocket socekt;
	public int processId;

	public SlaveProcessManager(String host, int port) {
		this.host = host;
		this.port = port;
		slaveBean = new SlaveBean(host, port);
		processList = new HashMap<Integer,MigratableProcess>();
		server = new MySocketServer(this.port);
		processId = 0;
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
			@Override
			public void run() {
				super.run();
				while(true) {
					if(server.getObject()!=null && server.getObject().toString().equals("grep")){
						//get process
						//add it to hashmap and run it
						System.out.println("running process");
						MigratableProcess process = (MigratableProcess)server.getObject();
						processList.put(processId, process);
						processId++;
						process.runProcess();
						server.setObject(null);
					} else if (server.getObject()!=null && server.getObject().getClass().isInstance("str")){
						
						if(server.getObject().equals("checkAval")) {
							// get slave bean
							//update slave bean
							slaveBean.setCurCount(checkList());
							System.out.println("to send slaveBean " + slaveBean.getCurCount());
							send("127.0.0.1" , 15640, slaveBean);
							server.setObject(null);
						} else if(server.getObject().equals("checkInfo")){
							//get processList
							//send back to master
							send("127.0.0.1",15640,processList);
							server.setObject(null);
						}
						
						
					} else if (server.getObject()!=null && server.getObject().getClass().isInstance(processId)) {
						//get processId and mig process back to master
						Integer processId = (Integer)server.getObject();
						if(processList.get(processId).isTerminated()) {
							System.out.println("Process " + processId + "is finished(terminated)");
							server.setObject(null);
						} else {
							processList.get(processId).suspend();
							send("127.0.0.1" , 15640, processList.get(processId));
							server.setObject(null);
						}
						
					}
					
					
					
					try {
						Thread.sleep(500);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		t.start();
		System.out.println("Herer!!!!!!!!");
	}


	/*
	 * send
	 */
	public void send(String host, int port, Object obj) {
		socekt = new MySocket(host, port);
		socekt.send(obj);		
	}

	
	/*
	 * check availability of slave
	 */
	public int checkList() {
		for (Integer processId : processList.keySet()) {
			if(processList.get(processId).isTerminated()) {
				processList.remove(processId);
			}
		}
		System.out.println("processList size is " + processList.size());
		return processList.size();
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
	public void run()  {
		listen();
	}



}
