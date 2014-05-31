package process;

import java.io.PrintStream;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;
import io.TransactionalFileInputStream;
import io.TransactionalFileOutputStream;

public class GrepProcess implements MigratableProcess {
	public Thread t;
	private TransactionalFileInputStream inFile;
	private TransactionalFileOutputStream outFile;
	private String query;

	private static volatile boolean suspending;
	private volatile boolean terminated;

	public GrepProcess(String args[]) throws Exception {
		if (args.length != 3) {
			System.out
					.println("usage: GrepProcess <queryString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}

		query = args[0];
		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2], false);
	}

	public void run() {
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

		try {
			while (!suspending) {
				String line = in.readLine();

				if (line == null)
					break;

				if (line.contains(query)) {
					System.out.println(line);
					out.println("here haha");
				}

				// Make grep take longer so that we don't require extremely
				// large files for interesting results
				try {
					Thread.sleep(1000);
					System.out.println("run here sleep 1000");
				} catch (InterruptedException e) {
					// ignore it
				}
			}
		} catch (EOFException e) {
			// End of File
		} catch (IOException e) {
			System.out.println("GrepProcess: Error: " + e);
		}

		suspending = false;
	}

	public void start() {
		System.out.println("Starting " + "grep");
		if (t == null) {
			t = new Thread(this, "grep");
			t.start();
		}
	}

	@Override
	public TransactionalFileInputStream getInput() {
		return inFile;
	}

	@Override
	public TransactionalFileOutputStream getOutput() {
		return outFile;
	}

	@Override
	public void suspend() {
		System.out.println("Suspend!!");
		suspending = true;
		while (suspending)
			;
	}

	public String toString() {
		return this.getClass().getName();
	}

	@Override
	public void resume() {
		suspending = false;
		notify();
	}

	@Override
	public void terminate() {
		terminated = true;
	}

	// @SuppressWarnings("deprecation")
	// public static void main(String[] args) {
	// String[] para = new String[3];
	// para[0] = "code";
	// para[1] = "testInput.txt";
	// para[2] = "testOutput.txt";
	// try {
	// GrepProcess g = new GrepProcess(para);
	// GrepProcess g2 = new GrepProcess(para);
	// // g.start();
	// // Thread t = new Thread(new GrepProcess(para));
	// g.run();
	// g2.suspend();
	//
	// // suspending = true;
	// System.out.println("here start");
	//			
	// System.out.println("here suspend");
	//			
	// Thread.sleep(500);
	// System.out.println("here sleep 500");
	// Thread.sleep(500);
	// System.out.println("here sleep 500");
	//
	// g.resume();
	// System.out.println("here resume");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//		
	// }

}