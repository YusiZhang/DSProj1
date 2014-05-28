/**
 * 
 */
package lab1;
import java.io.Serializable;

/**
 * @author yusizhang
 *
 */
public interface MigratableProcess extends Runnable,Serializable{

	public void suspend();
	public String toString();
	
}
