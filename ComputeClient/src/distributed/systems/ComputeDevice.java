package distributed.systems;

import java.util.*;

public class ComputeDevice extends Thread {
	
	private Integer deviceCapasity = 0;
	private static Integer capasityToThreadMultiplier = 100;
	
	Map<ComputeDeviceThread, Boolean> threads = new HashMap<ComputeDeviceThread, Boolean>(); // <thread object, in-use flag>
	List<ComputeDeviceThread> availableThreads = new ArrayList<ComputeDeviceThread>();
	
	public ComputeDevice(Integer deviceCapasity) {
		// TODO Auto-generated constructor stub
		for(Integer i=0; i<capasityToThreadMultiplier*deviceCapasity; i++){
			threads.put(new ComputeDeviceThread(), false);
		}
	}

	public Integer availableThreads() {
		Integer num_availableThreads = 0;
		availableThreads.clear();
		Iterator<Map.Entry<ComputeDeviceThread, Boolean>> iterator = threads.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<ComputeDeviceThread, Boolean> entry = iterator.next();
			if(entry.getValue()==false){
				num_availableThreads++;
				availableThreads.add(entry.getKey());
			}
		}
		return num_availableThreads;
	}

	public void compute(byte[] data, Integer requiredThreads) {
		// TODO Auto-generated method stub
		if(availableThreads()>=requiredThreads){
			// availableThreads. mark threads with a TRUE to reserve
		}else{
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
