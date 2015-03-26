package distributed.systems;

import java.awt.image.BufferedImage;
import java.net.*;
import java.util.*;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ComputeClient extends CommonComm implements Runnable 
{
	private Thread t;
	private String threadName;
	private String serverAddress;
	private Integer localPort;
	
	private BufferedReader in;
    private PrintWriter out;
	
	private Map<String, InetAddress> net = new HashMap<String, InetAddress>();
	
	public ConcurrencyFramework concurrencyFramework;
	
	public List<Task> tasks = new ArrayList<Task>();
	
	
	private Map<String, Integer> status = new HashMap<String, Integer>();
	
	
	/**
	 * ComputeClient(Integer capability_mean, Integer capability_var)
	 */
	public ComputeClient(String serverAddress, Integer localPort, String threadName)
	{
		this.serverAddress = serverAddress;
		this.localPort 		= localPort;
		this.threadName 	= threadName;
		
		net.put("self_ipv4", null);
		net.put("relay_ipv4", null);
		net.put("central_ipv4", null);
		
	    status.put("compute load", null);
		status.put("compute capasity", null);
		
	}
	
	public static void main(String [] args)
	{
		System.out.println("ComputeClient");
		
	}
	
	public void set_status(Map<String, Integer> map)
	{
		status = map;
	}
	
	public Map<String, Integer> get_status()
	{
		return status;
	}
	
	public void connectToServer() throws IOException
	{
        Socket socket = new Socket(this.serverAddress, this.localPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

	@Override
	public void run() {
		/*
		try {
			this.connectToServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    }
    
    public void start ()
	{
		System.out.println("Starting client: " +  threadName);
		if (t == null){
			t = new Thread (this, threadName);
			t.start ();
		}
	}

	public void addTask(String cameraAddress, Integer cameraLocalPort, Integer cameraUdpPort) {
		// TODO Auto-generated method stub
		UUID id = UUID.randomUUID();
		System.out.println("Starting task  : " +  id + " on client " + threadName);
		tasks.add(new Task(cameraAddress, cameraLocalPort, cameraUdpPort, id));
	}

	public void initConcurrencyFramework() {
		this.concurrencyFramework = new ConcurrencyFramework();
	}
	
	
	
}