package process;

import io.TransactionalFileInputStream;
import io.TransactionalFileOutputStream;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;

public class WordCountProcess implements MigratableProcess{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6690831713903505496L;
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	private String query;
	private long count;
	private volatile boolean suspending;
	private volatile boolean terminated;

	public WordCountProcess(String[] args) throws Exception{
		if (args.length != 3) {
			System.out.println("usage: WordCountProcess <queryString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		query = args[0];
		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2], false);
		
		
	}
	
	public void run(){
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);
		try{
			while (!suspending) {
				String line = in.readLine();
				if (line == null) break;
				if (line.contains(query)) {
					count++;
				}
				
				try{
					Thread.sleep(10000);
				}catch(InterruptedException e){
					//do nothing
				}
			}
			
			out.println(count);
		}catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("WordCountProcess: Error: " + e);
		}
		
		
		suspending = false;
	}

	@Override
	public void suspend() {
		suspending = true;
		while (suspending);		
	}

	@Override
	public TransactionalFileInputStream getInput() {
		return this.inFile;
	}

	@Override
	public TransactionalFileOutputStream getOutput() {
		return this.outFile;
	}

	@Override
	public synchronized void resume() {
		suspending = false;
		this.notify();
	}

//	@Override
//	public void terminate() {
//		terminated  = true;
//	}

	@Override
	public void runProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return terminated;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "grep";
	}
	
}
