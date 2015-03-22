/**
 * 
 */
package distributed.systems;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

/**
 * @author emilnielsen
 *
 */
public class testComputeClient {
	
	private static Random rand = new Random(System.currentTimeMillis());
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			testSetup();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Mock
	//ComputeClient client;
	
	@Test
	public static void testSetup() throws UnknownHostException, InterruptedException {
		// TODO Auto-generated constructor stub
		System.out.println("Testing: testSetup");
		
		Integer num_clients = 3;
		Integer capability_mean = 5;
		Integer capability_var = 2;
		
//		CentralNode central = new CentralNode();
//		List <RelayNode> relays = new ArrayList<RelayNode>();
//		List <IPCamera> cameras = new ArrayList<IPCamera>();
		
		List <ComputeClient> clients = new ArrayList<ComputeClient>();
		ComputeClient client;
		Map<String, Integer> status;
		String str_ipv4;
		
		for (Integer i=0; i<num_clients; i++){
			client = new ComputeClient();
			status = new HashMap<String, Integer>(); 
			
		    status.put("compute load", 0);
			status.put("compute capasity", getGaussian(capability_mean, capability_var));
			client.set_status(status);
			
			str_ipv4 = "0.0.0." + i;
			client.set_self_ip(InetAddress.getByName(str_ipv4));
			
			clients.add(client);
			
		}
		
		// assert and print
		for(ComputeClient obj : clients) {
			assertNotEquals(obj.get_self_ip().getHostName(), "0.0.0.");
			assertEquals(obj.get_status().get("compute load"), (Integer) 0);
			assertTrue(obj.get_status().get("compute capasity") > (Integer) 0);
			System.out.println(obj.get_self_ip().getHostName());
			System.out.println(obj.get_status());
			System.out.println();
		}
		
		
		
		/* MOCK
		ComputeClient client = Mockito.mock(ComputeClient.class);
		String str_ipv4 = "255.255.255.255";
		InetAddress ipv4 = InetAddress.getByName(str_ipv4);
		//client.set_self_ip(ipv4);
		Mockito.when(client.get_self_ip()).thenReturn(ipv4);
		assertEquals(client.get_self_ip(), ipv4);
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

}
