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
public class MainSlave3 {
	
	
	public static void main(String[] args) {
		SlaveProcessManager slaveManager2 = new SlaveProcessManager("127.0.0.1", 15643,15443);
		
		slaveManager2.run();
			
	}
	
}
