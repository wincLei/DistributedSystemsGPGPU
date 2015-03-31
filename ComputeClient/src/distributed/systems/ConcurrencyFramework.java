package distributed.systems;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConcurrencyFramework extends Thread
{
	private static Integer capasityToThreadMultiplier = 100;
	
	public List<ComputeDevice> devices = new ArrayList<ComputeDevice>();
	
	public ConcurrencyFramework() {
		// TODO Auto-generated constructor stub
	}

	public void addDevice(ComputeDevice device) {
		this.devices.add(device);
	}

	public void executeOnDevice(byte[] data, ComputeDevice device, Task.capasityRate rate) {
		Integer requiredThreads;
		if(rate==Task.capasityRate.fullrate){
			// device.availableThreads
			requiredThreads = 100;
		}else if(rate==Task.capasityRate.halfrate){
			requiredThreads = 50;
		}else if(rate==Task.capasityRate.thirdrate){
			requiredThreads = 33;
		}
		// device.compute(data, requiredThreads);
	}
	
	

}
