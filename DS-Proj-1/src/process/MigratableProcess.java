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
	
	public void resume();
	
	public void terminate();
	
	public String toString();

	TransactionalFileInputStream getInput();

	TransactionalFileOutputStream getOutput(); 
	
}
