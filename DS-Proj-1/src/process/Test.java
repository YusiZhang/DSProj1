package process;

/*Michelle
 */
public class Test {
	public static void main(String[] args) {
		String[] para1 = new String[3];
		para1[0] = "line";
		para1[1] = "testInput1.txt";
		para1[2] = "testOutput1.txt";

		String[] para2 = new String[4];
		para2[0] = "line";
		para2[1] = "newline";
		para2[2] = "testInput2.txt";
		para2[3] = "testOutput2.txt";
		GrepProcess g1, g2;
		Thread t1, t2;
		try {
			g1 = new GrepProcess(para1);
			// g2 = new GrepProcess(para2);

			t1 = new Thread(g1);
			// t2 = new Thread(g2);
			t1.start();
			// t2.start();

			Thread.sleep(1000);
			Thread.sleep(3000);

			g1.suspend();
			System.out.println("Suspending First Thread");
			Thread.sleep(3000);

			g1.resume();
			System.out.println("Resuming First Thread");
			g1.run();
			// g2.suspend();
			// System.out.println("Suspending thread Two");
			// Thread.sleep(1000);
			// g2.resume1();
			// System.out.println(g2.suspending);
			// g2.run();
			// System.out.println("Resuming thread Two");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
