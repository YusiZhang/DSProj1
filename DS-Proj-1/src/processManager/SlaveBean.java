package processManager;

public class SlaveBean {
	private final int maxCapacity = 10;
	private String host;
	private int port;
	private int curCount;

	
	public SlaveBean(String host, int port){
		this.host = host;
		this.port = port;
		setCurCount(0);
	}
	
	
	/*
	 * return true is add successfully
	 */
	public boolean addProcess(){
		if(isAddable()) {
			curCount++;
			return true;
		}else {
			return false;
		}
	}
	
	public int getCurCount() {
		return curCount;
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setCurCount(int curCount) {
		this.curCount = curCount;
	}
	
	private boolean isAddable(){
		if(curCount > maxCapacity) {
			return false;
		} else {
			return true;
		}
	}

}
