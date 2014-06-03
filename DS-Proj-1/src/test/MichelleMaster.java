package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import process.MigratableProcess;
import process.ReplaceProcess;
import process.WordCountProcess;
import communication.MySocketServer;

public class MichelleMaster {
	public static void main(String[] args) throws InterruptedException {
		final MySocketServer server = new MySocketServer(15640);

		Thread t = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				server.listen();
			}
		};

		Thread t2 = new Thread() {
			MigratableProcess process = null;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while (true) {

//					System.out.println("Please enter your task:");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					String request = null;
					try {
						request = br.readLine();
						if (request == null) 
							Thread.sleep(1000);
						if (request.equals("grep")) {
							//
							if (server.getObject() != null&& server.getObject().toString().equals("grep")) {

								System.out.println("Coming process");
								process = (MigratableProcess) server
										.getObject();
								process.runProcess();
								server.setObject(null);
							}
						}
						else if (request.equals("replace")) {
							String[] para = {"line", "newLine", "testInput2.txt", "testOutput2.txt"};
							process = new ReplaceProcess(para);
							process.runProcess();
						}
						else if (request.equals("word count")) {
							String[] para = {"line", "testInput3.txt", "testOutput3.txt"};
							process = new WordCountProcess(para);
							process.runProcess();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (server.getObject() != null
							&& server.getObject().toString().equals("grep")) {
						System.out.println("Coming process");
						process = (MigratableProcess) server.getObject();
						process.runProcess();
						server.setObject(null);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		t.start();

		t2.start();

	}
}

/*
 * public class MichelleMaster { public static void main(String[] args) throws
 * InterruptedException { final MySocketServer server = new
 * MySocketServer(15640);
 * 
 * Thread t = new Thread(){
 * 
 * @Override public void run() { // TODO Auto-generated method stub super.run();
 * server.listen(); } };
 * 
 * Thread t2 = new Thread() { MigratableProcess process = null;
 * 
 * @Override public void run() { // TODO Auto-generated method stub super.run();
 * while(true) { // if (server.getObject() != null &&
 * server.getObject().toString().equals("grep")) { //
 * System.out.println("Coming process"); // process = (MigratableProcess)
 * server.getObject(); // process.runProcess(); // server.setObject(null); // }
 * // try { // Thread.sleep(1000); // } catch (InterruptedException e) { // //
 * TODO Auto-generated catch block // e.printStackTrace(); // }
 * 
 * System.out.println("Please enter your task:"); BufferedReader br = new
 * BufferedReader(new InputStreamReader(System.in)); String request = null; try
 * { request = br.readLine(); } catch (IOException e) { // TODO Auto-generated
 * catch block e.printStackTrace(); } if (request == null) continue; else if
 * (request.equals("grep")){ String[] para = new String[3]; //
 * System.out.println("what query string: "); br = new BufferedReader(new
 * InputStreamReader(System.in)); try { para[0] = br.readLine(); } catch
 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
 * 
 * System.out.println("read from what file: "); br = new BufferedReader(new
 * InputStreamReader(System.in)); try { para[1] = br.readLine(); } catch
 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
 * 
 * System.out.println("write to what file: "); br = new BufferedReader(new
 * InputStreamReader(System.in)); try { para[2] = br.readLine(); } catch
 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
 * 
 * //run the grepprocess with parameters
 * 
 * } // if (server.getObject() != null &&
 * server.getObject().toString().equals("grep")) { //
 * System.out.println("Coming process"); // process = (MigratableProcess)
 * server.getObject(); // process.runProcess(); // server.setObject(null); // }
 * // String[] para = new String[3]; // //
 * System.out.println("what query string: "); // br = new BufferedReader(new
 * InputStreamReader(System.in)); // para[0] = br.readLine(); // //
 * System.out.println("read from what file: "); // br = new BufferedReader(new
 * InputStreamReader(System.in)); // para[1] = br.readLine(); // //
 * System.out.println("write to what file: "); // br = new BufferedReader(new
 * InputStreamReader(System.in)); // para[2] = br.readLine();
 * 
 * 
 * process = (MigratableProcess) server.getObject(); process.runProcess();
 * server.setObject(null); } } };
 * 
 * t.start();
 * 
 * t2.start();
 * 
 * 
 * } }
 * 
 * 
 * /*
 */
