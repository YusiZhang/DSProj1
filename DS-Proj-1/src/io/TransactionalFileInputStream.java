package io;
/*
import java.io.*;

public class TransactionalFileInputStream extends InputStream implements Serializable{
	/**
	 * 
	 
	private static final long serialVersionUID = -8577982678271040984L;
	private String fileName;
	private long fileIndex;
	private transient RandomAccessFile rf;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TransactionalFileInputStream(String fileName) {
		this.fileName = fileName;
		this.fileIndex = 0;
		try {
			rf = new RandomAccessFile(fileName, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
	}
	
	@Override
	public int read() throws IOException {
		//RandomAccessFile rf;
		rf = new RandomAccessFile(fileName, "r");
		rf.seek(fileIndex);
		int res = rf.read();
		if (res != -1) fileIndex++;
		return res;
	}
	
	@Override
	public void close() {
		try {
			rf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
*/

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;


public class TransactionalFileInputStream extends InputStream
    implements Serializable {

    private static final long serialVersionUID = 1L;

    //fields
    private long fileIndex; // file offset
    private String fileName;
    
    private int lastContent;

    public TransactionalFileInputStream(String inFile) {
        fileName = inFile;
        fileIndex = 0;
        lastContent = 0;
    }

    /**
     * transaction read: open, seek, perform, close
     */
    @Override
    public int read() throws IOException {
        //not reach the end already
        if (lastContent != -1) {
        	RandomAccessFile file;
            file = new RandomAccessFile(fileName, "r");
            if (file == null) {
                throw new IOException("This file has not been opened");
            }

            file.seek(fileIndex++);
            lastContent = file.read();
            file.close();
        }
        return lastContent;
    }

    @Override
    public int read(byte[] b) throws IOException {
        //not reach the end already
        if (lastContent != -1) {
        	RandomAccessFile file;
            file = new RandomAccessFile(fileName, "r");
            if (file == null) {
                throw new IOException("This file has not been opened");
            }

            file.seek(fileIndex);
            lastContent = file.read(b);
            file.close();
            fileIndex += b.length;
        }
        return lastContent;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        //not reach the end already
        if (lastContent != -1) {
        	RandomAccessFile file;
            file = new RandomAccessFile(fileName, "r");
            if (file == null) {
                throw new IOException("This file has not been opened");
            }

            file.seek(fileIndex);
            lastContent = file.read(b, off, len);
            file.close();
            fileIndex += len;
        }
        return lastContent;
    }

    public String getFileName() {
        return fileName;
    }
}
