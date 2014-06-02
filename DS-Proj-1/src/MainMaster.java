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
//			masterManager.launchProcessConfig("process.TestProcess", "Yusi");
			masterManager.run();
//			masterManager.migrateProcess(slaveHost, slavePort, process);

			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
