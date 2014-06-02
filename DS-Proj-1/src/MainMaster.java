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
		
		try {
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.addSlave("127.0.0.1", 15641);
			masterManager.addSlave("127.0.0.1", 15642);
			masterManager.addSlave("127.0.0.1", 15643);
			
			Thread.sleep(10000);
			
			masterManager.initMigrate();
			masterManager.run();

			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
