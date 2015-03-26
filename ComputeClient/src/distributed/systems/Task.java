/**
 * 
 */
package distributed.systems;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author emilnielsen
 *
 */
public class Task extends Thread
{
	
	public enum capasityRate{
		fullrate,
		halfrate,
		thirdrate;
	}
	
	private Socket tcp_socket; // two-way with IP Camera
	private DatagramSocket udp_socket; // one way from IP Camera
	public UUID id;
    public ConcurrentLinkedQueue<byte[]> queue = new ConcurrentLinkedQueue<byte[]>();
    public List<ComputeDevice> devices = new ArrayList<ComputeDevice>();

    public Task(String cameraAddress, Integer cameraLocalPort, Integer cameraUdpPort, UUID id){
    	/*
        try {
			this.tcp_socket = new Socket(cameraAddress, cameraLocalPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			this.udp_socket = new DatagramSocket(cameraUdpPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        this.id = id;
    }
    
    public void run() {
    	System.out.println("Running task   : " +  id);
//    	TcpConnection tcp = new TcpConnection(this.tcp_socket);
//    	UdpConnection udp = new UdpConnection(this.udp_socket);
    }
    
    private class TcpConnection extends Thread
    {

    	public TcpConnection(Socket tcp_socket)
    	{
    		try {
    			BufferedReader tcp_in = new BufferedReader(new InputStreamReader(tcp_socket.getInputStream()));
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            try {
    			PrintWriter tcp_out = new PrintWriter(tcp_socket.getOutputStream(), true);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
    }
    
    private class UdpConnection extends Thread
    {
    	public UdpConnection(DatagramSocket udp_socket)
    	{
    		byte[] buffer = new byte[65507];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
            	udp_socket.receive(dp);
            	// enqueue data
            	addToDeviceQueue(dp.getData());
            	Thread.yield();
            } catch (IOException ex) {
            	System.err.println(ex);
            }
    	}
    }
    
    /**
     * Logs a simple message.  In this case we just write the
     * message to the server applications standard output.
     */
    private void log(String message) {
        System.out.println(message);
    }

	public void addToDeviceQueue(byte[] data) {
		queue.offer(data);
	}

	public byte[] takeFromDeviceQueue() {
		return queue.poll();
	}

	public void useDevice(ComputeDevice device) {
		// TODO Auto-generated method stub
		this.devices.add(device);
	}

	

}
