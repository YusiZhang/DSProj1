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
		
		try {
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.ReplaceProcess", para2);
			
			masterManager.addSlave("127.0.0.1", 15641);
			masterManager.addSlave("127.0.0.1", 15642);
			masterManager.addSlave("127.0.0.1", 15643);
			
			Thread.sleep(4000);
			
			masterManager.initMigrate();
			masterManager.run();
			Thread.sleep(2000);
			masterManager.requestMig("127.0.0.1", 15641, 0);
			Thread.sleep(4000);
			masterManager.requestMig("127.0.0.1", 15641, 1);
			Thread.sleep(3000);
			masterManager.requestProessInfo("127.0.0.1", 15641);
			masterManager.getSlaveProcessInfo();


			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
