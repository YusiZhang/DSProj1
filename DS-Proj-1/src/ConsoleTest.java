import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import process.MigratableProcess;
import processManager.MasterProcessManager;
import processManager.SlaveBean;


public class ConsoleTest {
	
	public static void main(String[] args) {
		MasterProcessManager masterManager = new MasterProcessManager("127.0.0.1",15640);
		
		
		try {
			String[] para1 = new String[3];
			para1[0] = "line";
			para1[1] = "testInput1.txt";
			para1[2] = "testOutput1.txt";
			String cmd; 
			while (true) {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//				System.out.println(System.in);
				cmd = in.readLine();
				System.out.println("here " + cmd);
				if (cmd.equals("grep")){
					masterManager.launchProcessConfig("process.GrepProcess", para1);
					masterManager.run();
				}
				else if (cmd.equals("migrate")){
					String p, srcSlave, desSlave;
					System.out.println("Which Process:");
					in = new BufferedReader(new InputStreamReader(System.in));
					p = in.readLine();
					
					System.out.println("from which slave port:");
					in = new BufferedReader(new InputStreamReader(System.in));
					srcSlave = in.readLine();
					System.out.println("to which slave port:");
					in = new BufferedReader(new InputStreamReader(System.in));
					desSlave = in.readLine();
					
					//all the current slaves with current processes
//					System.out.println(masterManager.slaveList);
					System.out.println("what do I need to do");
				}
//				if (cmd.equals("a")) System.out.println("here I get a");
			}
//			masterManager.run();

			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
