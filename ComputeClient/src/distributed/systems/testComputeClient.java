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
	public static void testSetup() throws InterruptedException, IOException
	{
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
		
		/* initialise task */
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
