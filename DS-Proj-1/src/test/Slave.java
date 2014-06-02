package test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import process.GrepProcess;
import communication.MySocket;

public class Slave {
	public static void main(String[] args) {
		MySocket socket = new MySocket("127.0.0.1", 15640);
		String[] para1 = new String[3];
		para1[0] = "line";
		para1[1] = "testInput1.txt";
		para1[2] = "testOutput1.txt";
		
		GrepProcess g;
		try {
			g = new GrepProcess(para1);
			socket.send(g);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
