package io;

import java.io.*;

public class TransactionalFileOutputStream extends OutputStream implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7131932025690436925L;
	
	private String fileName;
	private long fileIndex;
	private transient RandomAccessFile rf;
	
	public TransactionalFileOutputStream(String fileName, boolean b) {
		this.fileName = fileName;
		this.fileIndex = 0;
		
		try {
			rf = new RandomAccessFile(fileName, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void write(int b) throws IOException {
		rf = new RandomAccessFile(fileName, "rw");

		rf.write(b);
		rf.close();
		fileIndex++;
	}

}
