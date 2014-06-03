package process;
import io.TransactionalFileInputStream;
import io.TransactionalFileOutputStream;

import java.io.PrintStream;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;

public class ReplaceProcess implements MigratableProcess
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6457065956449962288L;
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	private String src;
	private String des;
	volatile boolean suspending;
	volatile boolean terminated;

	public ReplaceProcess(String args[]) throws Exception
	{
		if (args.length != 4) {
			System.out.println("usage: GrepProcess <sourceString> <destinationString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		//replace the source word with the destination word
		src = args[0]; 
		des = args[1];
		inFile = new TransactionalFileInputStream(args[2]);
		outFile = new TransactionalFileOutputStream(args[3], false);
	}

	
//	public void runProcess(){
//		//if it has never been started 
//		if (inFile.getFileIndex() == 0){
//			Thread t = new Thread(this);
//			t.start();
//		}
//		else {
//			this.resume();
//		}
//	}
	
	public void run()
	{
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);
		
		try {
			while (!suspending) {
				String line = in.readLine();

				if (line == null) {
					terminated = true;
					break;

				}
				
				if (line.contains(src)) {
					System.out.println(line);
					line.replace(src, des);
					outFile.write(line.getBytes());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// ignore it
				}
			}
		} catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("Replace Process: Error: " + e);
		}

 
		suspending = false;
	}

	public void suspend()
	{
		System.out.println("executing suspending");

		suspending = true;
		while (suspending);
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
	public synchronized void resume()
	{
		suspending = false;
		this.run();
	}

//	@Override
//	public void terminate() {
//		terminated  = true;		
//	}
	
	@Override
    public String toString() {
//        StringBuilder showstring = new StringBuilder("GrepProcess ");
//        showstring.append(this.query);
//        showstring.append(" ");
//        showstring.append(this.inFile.getFileName());
//        showstring.append(" ");
//        showstring.append(this.outFile.getFileName());
//        return showstring.toString();
		return "Replace Process";
    }


	@Override
	public void runProcess() {
		//if it has never been started 
		if (inFile.getFileIndex() == 0){
			Thread t = new Thread(this);
			t.start();
		}
		else {
			this.resume();
		}
	}


	@Override
	public boolean isTerminated() {
		return terminated;
	}


}