
public class TestEntry {
	public static void main(String[] args) {
		Thread t_main = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				MainMaster master = new MainMaster();
				try {
					master.main(null);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Thread t_s1 = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				MainSlave s1 = new MainSlave();
				s1.main(null);
			}
		};
		
		Thread t_s2 = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				MainSlave2 s2 = new MainSlave2();
				s2.main(null);
			}
		};
		
		Thread t_s3 = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				MainSlave3 s3 = new MainSlave3();
				s3.main(null);
			}
		};
		
		t_main.start();
		t_s1.start();
		t_s2.start();
		t_s3.start();
		
	}
}
