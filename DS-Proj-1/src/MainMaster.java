import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import process.GrepProcess;
import processManager.MasterProcessManager;
import processManager.SlaveProcessManager;

/*
 * author : yusiz
 */


/*
 * Main entry 
 */
public class MainMaster {
	
	
	public static void main(String[] args) throws InterruptedException {
		MasterProcessManager masterManager = new MasterProcessManager("127.0.0.1",15640);
		
		String[] para1 = new String[3];
		para1[0] = "line";
		para1[1] = "testInput1.txt";
		para1[2] = "testOutput1.txt";
		
		String[] para2 = new String[4];
		para2[0] = "line";
		para2[1] = "newline";
		para2[2] = "testInput2.txt";
		para2[3] = "testOutput2.txt";
		
		String[] para3 = {"line", "testInput3.txt", "testOutput3.txt"};
		
		try {
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.ReplaceProcess", para2);
			masterManager.launchProcessConfig("process.WordCountProcess", para3);
			
			
			masterManager.addSlave("127.0.0.1", 15641);
			masterManager.addSlave("127.0.0.1", 15642);
			masterManager.addSlave("127.0.0.1", 15643);
			
			Thread.sleep(4000);
			
			masterManager.initMigrate();//send process to slave 1 (15641)
			masterManager.run();//run listen 
			
			
			Thread.sleep(3000);
			masterManager.requestProessInfo("127.0.0.1", 15641);//request process info from slave1
			masterManager.getSlaveProcessInfo(); //console output info of slave 

			Thread.sleep(2000);
			masterManager.requestMig("127.0.0.1", 15641, 0); //request process 0 on slave1 back to master, and send it to slave2
			Thread.sleep(2000);
			masterManager.requestMig("127.0.0.1", 15641, 1);//request process 1 on slave1 back to master, and send it to slave3
			Thread.sleep(2000);
			masterManager.requestMig("127.0.0.1", 15641, 2);//request process 2 on slave1 back to master, and send it to slave3
			

			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
