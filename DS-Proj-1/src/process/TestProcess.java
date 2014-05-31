package process;

import io.TransactionalFileInputStream;
import io.TransactionalFileOutputStream;

public class TestProcess implements MigratableProcess{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Test Process is running!");
	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		return "this is a test process";
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TransactionalFileInputStream getInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionalFileOutputStream getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}
