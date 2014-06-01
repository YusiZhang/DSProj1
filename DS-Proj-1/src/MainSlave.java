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
public class MainSlave {
	
	
	public static void main(String[] args) {
		SlaveProcessManager slaveManager = new SlaveProcessManager("127.0.0.1", 15644);
		SlaveProcessManager slaveManager2 = new SlaveProcessManager("127.0.0.1", 15645);
		
		
		try {
			slaveManager.run();
			slaveManager2.run();
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
