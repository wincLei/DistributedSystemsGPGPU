package distributed.systems;

import java.awt.image.BufferedImage;
import java.net.*;
import java.util.*;

public class ComputeClient extends CommonComm 
{
	private SocketAddress listening_port;
	private Map<String, InetAddress> net;
	
	private Map<String, Integer> status;
	
	private BufferedImage img;
	
	/**
	 * ComputeClient(Integer capability_mean, Integer capability_var)
	 * @param capability_mean
	 * @param capability_var
	 */
	public ComputeClient()
	{
		net = new HashMap<String, InetAddress>();
		net.put("self_ipv4", null);
		net.put("relay_ipv4", null);
		net.put("central_ipv4", null);
		
	    status = new HashMap<String, Integer>(); 
	    status.put("compute load", null);
		status.put("compute capasity", null);
	}
	
	public static void main(String [] args)
	{
		System.out.println("ComputeClient");
		
	}
	
	public void set_self_ip(InetAddress adr)
	{
		net.put("self_ipv4", adr);
	}
	
	public InetAddress get_self_ip()
	{
		return net.get("self_ipv4");
	}
	
	public void set_relay_ip(InetAddress adr)
	{
		
	}
	
	public void set_central_ip(InetAddress adr)
	{
		
	}
	
	public void set_status(Map<String, Integer> map)
	{
		status = map;
	}
	
	public Map<String, Integer> get_status()
	{
		return status;
	}
	
	
	
	
}