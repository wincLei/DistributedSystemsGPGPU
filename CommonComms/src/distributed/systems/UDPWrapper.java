//Inspired by https://systembash.com/a-simple-java-udp-server-and-udp-client/
package distributed.systems;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;


public class UDPWrapper implements Runnable
{

	//Ya ya we work with packets of this specific size. THE END.
	public static final int PACKETSIZE = 1024;
	private Thread _thread;
	private int listenPort;
	
	//Construct an UDPListener, but do not listen yet 
	public UDPWrapper(int listenPort) 
	{
		recievedDatagrams = new LinkedList<DatagramPacket>();
		this.listenPort = listenPort;
	}
	
	//Actually start the listener
	public void Listen()
	{
		_thread = new Thread(this);
		_thread.start();
	}
	
	//Sends a UDP message then goes on its merry way.
	@SuppressWarnings("resource")
	public void sendDatagram(int targetPort, InetAddress targetAddress, byte[] data)
	{
		try 
		{
			DatagramSocket clientSocket = new DatagramSocket();
		    DatagramPacket sendPacket = new DatagramPacket(data, data.length, targetAddress, targetPort);
			clientSocket.send(sendPacket);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	private Queue<DatagramPacket> recievedDatagrams;
	public DatagramPacket getDatagram()//Get latest UDP message if any such exist, else return null
	{
		DatagramPacket returnPacket = null;
		//Acquire lock on datagram queue
		synchronized(recievedDatagrams)
		{
			//Take oldest message or null if no message exist.
			returnPacket = recievedDatagrams.poll();
		}
		return returnPacket;
	}
	
	
	
	//Listener Thread, we listen to everything you say, just like the NSA :D
	@Override
	public void run()
	{
		//Open a socket listening at the specified port.
		DatagramSocket listeningSocket;
		try 
		{
			listeningSocket = new DatagramSocket(listenPort);
		
	        
			//Wait for a datagram, add it to the queue, repeat ad nauseum.
			while(true)
			{

				byte[] receiveData = new byte[PACKETSIZE];
				
	        	//Object holding incoming message
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				//This method BLOCKS, and is the entire reason for using a thread for this transaction.
				listeningSocket.receive(receivePacket);
				
	
				//DEBUG LOGGING
				System.out.println("RECEIVED: " + new String(receivePacket.getData()) + " FROM: " + receivePacket.getAddress().getHostAddress() + ":" + receivePacket.getPort());
				
				//Add message to queue
				synchronized(recievedDatagrams)
				{
					recievedDatagrams.add(receivePacket);
					
					//DEBUG LOGGING
					System.out.println("Messages in queue: " + recievedDatagrams.size());
				}
	        }
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(-1);
		} 
		
		
	}

}
