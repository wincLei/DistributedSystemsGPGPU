package distributed.systems;

public class ComputeDeviceThread extends Thread{

	public ComputeDeviceThread() {
		
	}
	
	public void execute(Integer time){
		// run for some time
		try {
			sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
