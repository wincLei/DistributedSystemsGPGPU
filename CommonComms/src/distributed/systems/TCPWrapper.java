package distributed.systems;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class TCPWrapper implements Runnable
{
	public static final int PACKETSIZE = 1024;
	
	private Thread _thread;
	private int listenPort;
	
	public TCPWrapper(int listenPort) 
	{
		this.listenPort = listenPort;
		recievedConnections = new LinkedList<Socket>();
		
	}
	
	//Actually start the listener
	public void Listen()
	{
		_thread = new Thread(this);
		_thread.start();
	}
	
	//Establish a connection to a client, pretty straight forward
	public Socket Connect(int targetPort, InetAddress targetAddress)
	{
		try 
		{
			return new Socket(targetAddress, targetPort);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	private Queue<Socket> recievedConnections;
		
	//Have any clients connected to us? returns a socket or null
	public Socket newConnections()
	{
		Socket returnSocket  = null;
		synchronized(recievedConnections)
		{
			returnSocket = recievedConnections.poll();
		}
		return returnSocket;
	}

	//Listener Thread, we listen to everything you say, just like the NSA :D
	@Override
	public void run()
	{
		//Open a socket listening at the specified port.
		ServerSocket listenSocket; 
		try
		{
		
			listenSocket = new ServerSocket(listenPort);
			//Wait for a new connection, add it to the queue, repeat ad nauseum.
			while(true)
	        {
				//This method BLOCKS, and is the entire reason for using a thread for this transaction.
				Socket connectionSocket = listenSocket.accept();
	            
				synchronized(recievedConnections)
				{
					System.out.println("New connection from: " + connectionSocket.getLocalSocketAddress()+":"+connectionSocket.getPort());
					recievedConnections.add(connectionSocket);
				}
			
	        }

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void sendPacket(Socket socket, byte[] data)
	{
		try 
		{
			socket.getOutputStream().write(data);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	//THIS BLOCKS, use at own peril, or perhaps use udp for control. whatever.
	public byte[] recievePacket(Socket socket)
	{
		byte[] retData = new byte[PACKETSIZE]; //Yea yea we read a fixed length packet size, just read more if thats a problem >_> blame the stupid socket api
		int read = -1;
		
		try 
		{
			 read = socket.getInputStream().read(retData);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
		if (read == -1)
			return null;
		return retData;
	}	
}
