package io;
/*
import java.io.*;

public class TransactionalFileOutputStream extends OutputStream implements Serializable{

	private static final long serialVersionUID = -7131932025690436925L;
	
	private String fileName;
	private long fileIndex;
	private transient RandomAccessFile rf;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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
*/

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;


public class TransactionalFileOutputStream extends OutputStream
    implements Serializable {

    private static final long serialVersionUID = 1L;
 
    //fields
    private long fileIndex; // file offset
    private String fileName;
    

    /**
     * constructor with append flag
     * @param outFile
     * @param apflag, append write:true, else false
     */
    public TransactionalFileOutputStream(String outFile, boolean apflag) {
        fileName = outFile; 
        try {
        	RandomAccessFile file;
            file = new RandomAccessFile(fileName, "rw");
            fileIndex = apflag ? file.length() : 0;
            file.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * transaction write: open, seek, perform, close
     */
    @Override
    public void write(int content) throws IOException {
    	RandomAccessFile file;
        file = new RandomAccessFile(fileName, "rw");
        if (file == null) {
            throw new IOException("This file has not been opened");
        }

        file.seek(fileIndex++);
        file.write(content);
        file.close();
    }

    @Override
    public void write(byte[] content) throws IOException {
    	RandomAccessFile file;
        file = new RandomAccessFile(fileName, "rw");
        if (file == null) {
            throw new IOException("This file has not been opened");
        }

        file.seek(fileIndex);
        file.write(content);
        file.close();
        fileIndex += content.length;
    }

    @Override
    public void write(byte[] content, int off, int len) throws IOException {
    	RandomAccessFile file;
        file = new RandomAccessFile(fileName, "rw");
        if (file == null) {
            throw new IOException("This file has not been opened");
        }

        file.seek(fileIndex);
        file.write(content, off, len);
        file.close();
        fileIndex += len;
    }

    public String getFileName() {
        return fileName;
    }
}