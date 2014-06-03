/**
 * 
 */
package process;
import io.TransactionalFileInputStream;
import io.TransactionalFileOutputStream;

import java.io.Serializable;

/**
 * @author yusizhang
 *
 */
public interface MigratableProcess extends Runnable,Serializable{

	public void suspend();
	
	public void runProcess();
	
	public void resume();
	
	public boolean isTerminated();
	
	public String toString();//required implementation to return string .

	TransactionalFileInputStream getInput();

	TransactionalFileOutputStream getOutput(); 
	
}
