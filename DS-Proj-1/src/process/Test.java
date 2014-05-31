package process;

/*Michelle
 */
public class Test {
	public static void main(String[] args) {
		String[] para1 = new String[3];
		para1[0] = "code";
		para1[1] = "testInput1.txt";
		para1[2] = "testOutput1.txt";

		String[] para2 = new String[3];
		para2[0] = "run";
		para2[1] = "testInput2.txt";
		para2[2] = "testOutput2.txt";
		GrepProcess g1, g2;
		try {
			 g1 = new GrepProcess(para1);
			 g2 = new GrepProcess(para2);

//			 g1 = new Thread(new GrepProcess(para1));
//			 g2 = new Thread(new GrepProcess(para2));
			g1.start();
			g2.start();

			Thread.sleep(1000);
			g1.suspend();
			System.out.println("Suspending First Thread");
			Thread.sleep(1000);
			g1.resume();
			System.out.println("Resuming First Thread");
			g2.suspend();
			System.out.println("Suspending thread Two");
			Thread.sleep(1000);
			g2.resume();
			System.out.println("Resuming thread Two");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//	         System.out.println("Waiting for threads to finish.");
//	         g1.t.join();
//	         g2.t.join();
//	      } catch (InterruptedException e) {
//	         System.out.println("Main thread Interrupted");
//	      }

	}
}
