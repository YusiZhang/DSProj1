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
	private TransactionalFileInputStream  inFile;
	private TransactionalFileOutputStream outFile;
	private String query;
	private String replaceStr;


	volatile boolean suspending;
	volatile boolean terminated;

	public ReplaceProcess(String args[]) throws Exception
	{
		if (args.length != 4) {
			System.out.println("usage: ReplaceProcess <queryString> <replaceString> <inputFile> <outputFile>");
			throw new Exception("Invalid Arguments");
		}
		
		query = args[0];
		replaceStr = args[1];
		inFile = new TransactionalFileInputStream(args[2]);
		outFile = new TransactionalFileOutputStream(args[3], false);
	}

	public void run()
	{
		PrintStream out = new PrintStream(outFile);
		DataInputStream in = new DataInputStream(inFile);

		try {
			while (!suspending) {
				String line = in.readLine();

				if (line == null) break;
				
				if (line.contains(query)) {
					line.replace(query, replaceStr);
					out.println(line);
				}
				// Make grep take longer so that we don't require extremely large files for interesting results
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// ignore it
				}
			}
		} catch (EOFException e) {
			//End of File
		} catch (IOException e) {
			System.out.println ("GrepProcess: Error: " + e);
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
		this.notify();
	}

//	@Override
//	public void terminate() {
//		terminated  = true;		
//	}
	
	@Override
    public String toString() {
        StringBuilder showstring = new StringBuilder("GrepProcess ");
        showstring.append(this.query);
        showstring.append(" ");
        showstring.append(this.inFile.getFileName());
        showstring.append(" ");
        showstring.append(this.outFile.getFileName());
        return showstring.toString();
    }

	@Override
	public void runProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return terminated;
	}


}