package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import process.MigratableProcess;
import communication.MySocketServer;

public class Master {
	public static void main(String[] args) throws InterruptedException {
		final MySocketServer server = new MySocketServer(15640);

		Thread t = new Thread(){
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
				while(true) {
					if (server.getObject() != null && server.getObject().toString().equals("grep")) {
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