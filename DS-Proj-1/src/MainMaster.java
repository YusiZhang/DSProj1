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
//		SlaveProcessManager slaveManager = new SlaveProcessManager("127.0.0.1", 15644);
		
		
		try {
			masterManager.launchProcessConfig(null, null);
//			slaveManager.run();
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
