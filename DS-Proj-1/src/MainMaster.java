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
	
	
	public static void main(String[] args) {
		MasterProcessManager masterManager = new MasterProcessManager("127.0.0.1",15640);
		
		
		try {
//			String[] args1 = {"arg1", "arg2"};
			String[] para1 = new String[3];
			para1[0] = "line";
			para1[1] = "testInput1.txt";
			para1[2] = "testOutput1.txt";
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			masterManager.launchProcessConfig("process.GrepProcess", para1);
			
			masterManager.addSlave("127.0.0.1", 15644);
			masterManager.addSlave("127.0.0.1", 15645);
			masterManager.addSlave("127.0.0.1", 15646);
			
			
			
			masterManager.run();
			
			Thread.sleep(10000);
			
			masterManager.migrateProcessBest(masterManager.processList.get(0));
			Thread.sleep(8000);
//			masterManager.migrateProcessBest(masterManager.processList.get(1));
//			Thread.sleep(8000);
//			masterManager.migrateProcessBest(masterManager.processList.get(2));
//			Thread.sleep(8000);
			
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
