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

public class WordCountProcess implements MigratableProcess
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6457065956449962288L;
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	private String query;
	private long count;

	volatile boolean suspending;
	volatile boolean terminated;

	public WordCountProcess(String args[]) throws Exception
	{
		if (args.length != 3) {
			System.out.println("usage: WordCountProcess <queryString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		query = args[0];
		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2], false);
	}

	
	public void run()
	{
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);
		
		try {
			while (!suspending) {
				String line = in.readLine();

				if (line == null) {
					System.out.println("there are "+count+" "+this.query+" in the file "+ inFile.getFileName());
					outFile.write((int)count);
					terminated = true;
					break;

				}
				
				if (line.contains(query)) {
					count++;

				}
				// Make grep take longer so that we don't require extremely large files for interesting results
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// ignore it
				}
			}
		} catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("WordCountProcess: Error: " + e);
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
	
//	@Override
//    public String toString() {
////        StringBuilder showstring = new StringBuilder("GrepProcess ");
////        showstring.append(this.query);
////        showstring.append(" ");
////        showstring.append(this.inFile.getFileName());
////        showstring.append(" ");
////        showstring.append(this.outFile.getFileName());
////        return showstring.toString();
//		return "Word Count Process";
//    }


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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "count";
	}
	
}




