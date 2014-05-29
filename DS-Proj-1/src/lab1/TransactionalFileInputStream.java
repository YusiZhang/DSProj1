package lab1;

import java.io.*;

public class TransactionalFileInputStream extends InputStream implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8577982678271040984L;
	private String fileName;
	private long fileIndex;
	private transient RandomAccessFile fileStream;

	public TransactionalFileInputStream(String fileName) {
		this.fileName = fileName;
		this.fileIndex = 0;
		try {
			fileStream = new RandomAccessFile(fileName, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
	}
	
	@Override
	public int read() throws IOException {
		int res = fileStream.read();
		if (res != -1) fileIndex++;
		return res;
	}
	
}
