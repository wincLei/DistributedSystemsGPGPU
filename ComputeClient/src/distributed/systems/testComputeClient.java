/**
 * 
 */
package distributed.systems;

import static org.junit.Assert.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.Test;

/**
 * @author emilnielsen
 *
 */
public class testComputeClient implements Runnable {
	
	private static Random rand = new Random(System.currentTimeMillis());
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		testNetworkTopography();
		
//		int[] tcp_free_ports = getFreePorts(1);
//		Integer tcp_port = tcp_free_ports[0];
		
		/* format : Socket[addr=/192.168.1.64,port=53036,localport=9898]
		host: 324-040.local/192.168.1.64
		The capitalization server is running.
		New connection with client# 0 at Socket[addr=/192.168.1.64,port=53036,localport=9898]
		Connection with client# 0 closed
		*/
		
		
		// TODO Auto-generated method stub
		try {
			testSetup();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	
	public static void testNetworkTopography()
	{
		boolean print 			= false;
		int n_centrals 			= 0;	// the number of central nodes in the network at initialisation
		int n_relaynodes 		= 3;	// the number of relay nodes in the network at initialisation
		int n_computeclients 	= 8;	// the number of compute clients in the network at initialisation
		int n_cameras 			= 8; 	// the number of IP cameras in the network at initialisation
		
		NetworkTopography topography = new NetworkTopography(n_centrals, n_relaynodes, n_computeclients, n_cameras);
		
		// initialise network elements
		topography.initCentrals();
		topography.initRelayNodes();
		topography.initComputeClients();
		topography.initCameras();
		
		// set the listen port
		int listenport = 9898;
		topography.setListenPort(listenport);
		
		// get all network elements
		List<Object> objects = topography.getAllElements();
		for(Object object: objects){
			topography.ping(object);
		}
		
		// set print boolean
		print = false;
		
		// iterator for all network delays
		Iterator iterator_objs = topography.delays.keySet().iterator();// Iterate on keys
		
		while(iterator_objs.hasNext()){
			Object head_object = (Object) iterator_objs.next();
			Map<Object, Float> delayToObject = topography.delays.get(head_object);
			Iterator iterator_delays = delayToObject.keySet().iterator();// Iterate on keys
			// print ping if object is a relay node
			if(print){
				if(head_object.getClass().equals(RelayNode.class)){
					System.out.println(head_object);
				}
			}
			
			while (iterator_delays.hasNext()){
				Object got_object = (Object) iterator_delays.next();
				Float got_delay = (Float) delayToObject.get(got_object);
				// print ping if object is a relay node
				if(print){
					if(head_object.getClass().equals(RelayNode.class)){
						System.out.println("\t-> " + got_object + "\n\tping: " + got_delay + "[ms]");
					}
				}
			}
		}
		
		// set print boolean
		print = true;
		// create network element clusters, i.e. cameras and compute clients singularly attached to relay nodes
		List<NetworkCluster> clusters = new ArrayList<NetworkCluster>();
		
//		Map<Object, Float> min_computeclient = new HashMap<Object, Float>();
		ComputeClient min_computeclient;
		IPCamera min_camera;
		Float min_delay;
		
		for(RelayNode relay: topography.relaynodes){ // go through relay nodes
			clusters.add(new NetworkCluster(relay));
			
			// add nearest compute clients
			for(ComputeClient computeclient: topography.computeclients){ // go through compute clients
				min_computeclient = computeclient;
				min_delay = topography.delays.get(relay).get(computeclient);
				
				boolean isMin = true;
				for(RelayNode relay_nested: topography.relaynodes){ // go through relay nodes
					if(!relay_nested.equals(relay)){ // exclude current relay node
						if(topography.delays.get(relay_nested).get(computeclient)<min_delay){
							isMin = false;
						}
					}
				}
				if(isMin){
					for(NetworkCluster cluster: clusters){
						if(cluster.relay.equals(relay)){
							cluster.addComputeClientWithDelay(min_computeclient, min_delay);
						}
					}
				}
			}
			// add nearest camera
			for(IPCamera camera: topography.cameras){ // go through cameras
				min_camera = camera;
				min_delay = topography.delays.get(relay).get(camera);
				
				boolean isMin = true;
				for(RelayNode relay_nested: topography.relaynodes){ // go through relay nodes
					if(!relay_nested.equals(relay)){ // exclude current relay node
						if(topography.delays.get(relay_nested).get(camera)<min_delay){
							isMin = false;
						}
					}
				}
				if(isMin){
					for(NetworkCluster cluster: clusters){
						if(cluster.relay.equals(relay)){
							cluster.addCameraWithDelay(min_camera, min_delay);
						}
					}
				}
			}
		}
		
		for(NetworkCluster cluster: clusters){
			System.out.println("cluster @ relay: " + cluster.relay);
			for(ComputeClient computeclient: cluster.computeclients.keySet()){
				System.out.println("\t contains: " + computeclient + " at " + cluster.computeclients.get(computeclient) + " [ms]");
			}
			for(IPCamera camera: cluster.cameras.keySet()){
				System.out.println("\t contains: " + camera + " at " + cluster.cameras.get(camera) + " [ms]");
			}
		}
	}
	
	public static void testSetup() throws InterruptedException, IOException
	{
		/*
		String serverAddress = InetAddress.getLocalHost().getHostAddress();
		Integer localPort = 9898;
		System.out.println("Server address : " + serverAddress);
		System.out.println("    Local port : " + localPort);
		
		ComputeClient client = new ComputeClient(serverAddress, localPort, "thread_client_0");
		client.start();
		
		// create compute device
		Integer deviceCapasity = 5;
		ComputeDevice device = new ComputeDevice(deviceCapasity);
		
		// initialise concurrency framework
		client.initConcurrencyFramework();
		
		// add compute device to concurrency framework recources
		client.concurrencyFramework.addDevice(device);
		
		// initialise task
		String cameraAddress 	= InetAddress.getLocalHost().getHostAddress();
		Integer cameraLocalPort = 8890;
		Integer cameraUdpPort 	= 8891;
		client.addTask(cameraAddress, cameraLocalPort, cameraUdpPort);
		
		// assert that it is false that the list of tasks is empty
		assertFalse(client.tasks.isEmpty());
		
		// get a task object
		Task task = client.tasks.get(0);
		
		// add compute device to the tasks resource list
		task.useDevice(client.concurrencyFramework.devices.get(0));
		
		// assert that it is false that the list of devices is empty
		assertFalse(task.devices.isEmpty());
		
		// start running the task
		task.start();
		
		// enqueue data
		byte[] data = {0,1,2,3}; // received through the UDP socket
		task.addToDeviceQueue(data);
		
		// dequeue data
		assertEquals(task.takeFromDeviceQueue(), data);
		byte[] dataFromQueue = task.takeFromDeviceQueue();
		
		// process data
		Integer deviceAvailableThreads = 0;
		deviceAvailableThreads = task.devices.get(0).availableThreads();
		System.out.println("deviceAvailableThreads: " + deviceAvailableThreads);
		System.out.println("task.devices.get(0).availableThreads.size(): " + task.devices.get(0).availableThreads.size());
		client.concurrencyFramework.executeOnDevice(task.takeFromDeviceQueue(), task.devices.get(0), Task.capasityRate.fullrate);
		*/
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 *  non-body method
	 *  Integer getUniform(Integer min, Integer max)
	 */
	public static Integer getUniform(Integer min, Integer max)
	{
		Integer val = rand.nextInt((max - min) + 1) + min;
		if (val<1){
			val = 1;
		}
	    return val;
	}
	
	/**
	 *  non-body method
	 *  Integer getGaussian(Integer mean, Integer var)
	 */
	public static Integer getGaussian(Integer mean, Integer var)
	{
		Integer val = (int) (mean + rand.nextGaussian() * var);
		if (val<1){
			val = 1;
		}
	    return val;
	}
	
	/**
	 *  non-body method
	 *  getFreePorts(int portNumber) throws IOException {
	 */
	public static int[] getFreePorts(int portNumber) throws IOException {
	    int[] result = new int[portNumber];
	    List<ServerSocket> servers = new ArrayList<ServerSocket>(portNumber);
	    ServerSocket tempServer = null;

	    for (int i=0; i<portNumber; i++) {
	        try {
	            tempServer = new ServerSocket(0);
	            servers.add(tempServer);
	            result[i] = tempServer.getLocalPort();
	        } finally {
	            for (ServerSocket server : servers) {
	                try {
	                    server.close();
	                } catch (IOException e) {
	                    // Continue closing servers.
	                }
	            }
	        }
	    }
	    return result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
