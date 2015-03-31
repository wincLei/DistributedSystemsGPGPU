package distributed.systems;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkCluster {
	
	public RelayNode relay;
	public Map<ComputeClient, Float> computeclients	= new HashMap<ComputeClient, Float>();
	public Map<IPCamera, Float> cameras				= new HashMap<IPCamera, Float>();

	public NetworkCluster(RelayNode relay) {
		// TODO Auto-generated constructor stub
		this.relay = relay;
	}

	public void addComputeClientWithDelay(ComputeClient computeclient, Float delay) {
		// TODO Auto-generated method stub
		computeclients.put(computeclient, delay);
	}

	public void addCameraWithDelay(IPCamera camera, Float delay) {
		// TODO Auto-generated method stub
		cameras.put(camera, delay);
	}

}
