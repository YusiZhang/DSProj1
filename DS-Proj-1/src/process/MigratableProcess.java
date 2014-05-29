/**
 * 
 */
package process;
import java.io.Serializable;

import lab1.TransactionalFileInputStream;
import lab1.TransactionalFileOutputStream;

/**
 * @author yusizhang
 *
 */
public interface MigratableProcess extends Runnable,Serializable{

	public void suspend();
	
	public void resume();
	
	public void terminate();
	
	public String toString();

	TransactionalFileInputStream getInput();

	TransactionalFileOutputStream getOutput(); 
}
