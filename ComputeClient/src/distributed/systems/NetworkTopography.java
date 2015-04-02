package distributed.systems;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkTopography {
	
	public int n_centrals 		= 0;
	public int n_relaynodes		= 0;
	public int n_computeclients 	= 0;
	public int n_cameras			= 0;
	
	public static int listenport 		= 0;
	
	public List<CentralNode> centrals 				= new ArrayList<CentralNode>();
	public List<RelayNode> relaynodes 				= new ArrayList<RelayNode>();
	public List<ComputeClient> computeclients 		= new ArrayList<ComputeClient>();
	public List<IPCamera> cameras 					= new ArrayList<IPCamera>();
	public List<NetworkCluster> clusters 			= new ArrayList<NetworkCluster>();
	
	public Map<Object, Map<Object, Float>> delays	= new HashMap<Object,Map<Object, Float>>();
	public Map<Object, Float> delayToObject			= new HashMap<Object, Float>();
	
	public static Random rand 		= new Random();
	public static int delay_mean 	= 100; // ms
	public static int delay_var 	= 40;
	public static int delay_min 	= 10;
	
	public NetworkTopography(int n_centrals, int n_relaynodes, int n_computeclients, int n_cameras) {
		// TODO Auto-generated constructor stub
		this.n_centrals = n_centrals;
		this.n_relaynodes = n_relaynodes;
		this.n_computeclients = n_computeclients;
		this.n_cameras = n_cameras;
	}

	public void initCentrals() {
		// TODO Auto-generated method stub
		for(int i=0; i<n_centrals; i++){
			centrals.add(new CentralNode());
		}
	}

	public void initRelayNodes() {
		// TODO Auto-generated method stub
		for(int i=0; i<n_relaynodes; i++){
			relaynodes.add(new RelayNode(listenport));
		}
	}

	public void initComputeClients() {
		// TODO Auto-generated method stub
		for(int i=0; i<n_computeclients; i++){
			computeclients.add(new ComputeClient(listenport));
		}
	}

	public void initCameras() {
		// TODO Auto-generated method stub
		for(int i=0; i<n_cameras; i++){
			cameras.add(new IPCamera(listenport));
		}
	}

	public void setListenPort(int port) {
		this.listenport = port;
	}

	public void ping(Object obj) {
		// TODO Auto-generated method stub
		Map<Object, Float> delayToObject = new HashMap<Object, Float>();
		Float delay = 0.0f;
		for(Object objx: getAllElements()){
			if(objx!=obj){
				delay = getGaussian(delay_mean, delay_var);
				if(delays.containsKey(objx)){
					if(delays.get(objx).get(obj)!=null){
						delayToObject.put(objx, delays.get(objx).get(obj));
					}
				}else{
					delayToObject.put(objx, delay);
				}
			}
		}
		if(delayToObject!=null){
			delays.put(obj, delayToObject);
		}
	}
	
	
	public static Float getGaussian(int mean, int var)
	{
		Float val = (float) (mean + rand.nextGaussian() * var);
		if (val<delay_min){
			val = (float) delay_min;
		}
	    return val;
	}

	public List<Object> getAllElements() {
		// TODO Auto-generated method stub
		List<Object> objects = new ArrayList<Object>();
		for(Object obj: centrals){
			objects.add(obj);
		}
		for(Object obj: relaynodes){
			objects.add(obj);
		}
		for(Object obj: computeclients){
			objects.add(obj);
		}
		for(Object obj: cameras){
			objects.add(obj);
		}
		return objects;
	}

	public void generateRelayNodeEdges(Object parent_object) {
		// TODO Auto-generated method stub
		
	}
	
	
}
