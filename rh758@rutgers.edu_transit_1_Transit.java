import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 * @return The zero node in the train layer of the final layered linked list
	 */
	//Pseudocode:
	//make three new ArrayLists of TNodes for trains, busstops, an
	public static TNode makeList(int[] trainStations, int[] busStops, int[] locations) {

        //initializes arrayslists for nodes
        
        ArrayList<TNode> bus = new ArrayList<TNode>();
        bus.add(new TNode(0));
        ArrayList<TNode> train = new ArrayList<TNode>();
        train.add(new TNode(0));
		ArrayList<TNode> walk = new ArrayList<TNode>(); 
		//connect across
        for (int i = 0; i <= locations.length; i++){ 
            walk.add (new TNode(i, null, null));
        }
        for (int i = 0; i < busStops.length; i++){ 
            bus.add (new TNode(busStops[i], null, null));
        }
        for (int i = 0; i < trainStations.length; i++)
		{
            train.add (new TNode(trainStations[i], null, null));
        }
		//connect down
        for (int i = 0; i < walk.size()-1; i++)
		{
            walk.get(i).next = walk.get(i+1);
        }
        for (int i = 0; i < busStops.length; i++)
		{ 
            bus.get(i).next = bus.get(i+1);
        }
        for (int i = 0; i < trainStations.length; i++)
		{ 
            train.get(i).next = train.get(i+1);
        }
		
		
        for (int i = 0; i <= trainStations.length; i++){ //connects train nodes down
            TNode busPosition= pos(bus,train.get(i).location);
            train.get(i).down = busPosition; 
        }
        for(int i = 0; i <= busStops.length; i ++){ //connects bus nodes down
            bus.get(i).down = walk.get(bus.get(i).location);
        }
        return train.get(0);
    }
	private static TNode pos(ArrayList<TNode> buss, int position)
	{
        for (int i = 0; i < buss.size(); i ++)
		{
            if (buss.get(i).location == position)
			{
                return buss.get(i);
            }
        }
		return null;
	}

	/**
	 * Modifies the given layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param station The location of the train station to remove
	 */
	public static void removeTrainStation(TNode trainZero, int station) 
	{
		// WRITE YOUR CODE HERE
		
		TNode temp = trainZero;
		ArrayList<TNode> train = new ArrayList<TNode>();
		
		while (temp != null)
		{
			train.add(temp);
			temp = temp.next;
		}

		while (trainZero !=null)
		{
			if (trainZero.next != null && station == trainZero.next.location  )
			{
				trainZero.next = trainZero.next.next;
				
			}
			else if (trainZero.next == null)
			{
				trainZero = null;
				
			}
			if (trainZero!=null)
			{
				trainZero=trainZero.next;
			}
			
		}

	

	}

	/**
	 * Modifies the given layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param busStop The location of the bus stop to add
	 */
	public static void addBusStop(TNode trainZero, int busStop) {
		// WRITE YOUR CODE HERE
		TNode bus = trainZero.down;
		TNode walk = trainZero.down.down;
//arraylist created
		ArrayList<TNode> buss = new ArrayList<TNode>();
		ArrayList<TNode> walkk = new ArrayList<TNode>();
		while(bus!= null)
		{
			buss.add(bus);
			bus=bus.next;
		}
		while(walk!= null)
		{
			walkk.add(walk);
			walk=walk.next;
		}

		for (int i = 0; i<buss.size(); i++)
		{
			if (buss.get(i).location == busStop)
			{
				return;
			}
		}

		for(int i=0; i< buss.size(); i++)
		{
			if (buss.get(i).next==null)
			{
				for(int j = 0; j<walkk.size(); j++){
					if(walkk.get(j).location == busStop)
					{
						buss.get(i).next = new TNode(busStop, buss.get(i).next, walkk.get(j));
					}
				}
			}
			if(buss.get(i).next.location > busStop)
			{
				TNode check = null;
				for (TNode j=trainZero.down.down; j.next!=null; j=j.next)
				{
					if(j.location==busStop)
					{
						check = j;
					}
				}
				buss.get(i).next = new TNode(busStop, buss.get(i).next, check);
				return;
			}
		}



	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param destination An int representing the destination
	 * @return
	 */
	public static ArrayList<TNode> bestPath(TNode trainZero, int destination) {
		// WRITE YOUR CODE HERE
		ArrayList<TNode> route = new ArrayList<TNode>();
		//train route add
		while(trainZero.next!=null&&trainZero.next.location<=destination)
		{
			route.add(trainZero);
			trainZero = trainZero.next;
		}
		route.add(trainZero);
		trainZero = trainZero.down;
		route.add(trainZero);
		//bus route add
		while(trainZero.next!=null && trainZero.next.location<=destination)
		{
			trainZero = trainZero.next;
			route.add(trainZero);
		}
		trainZero = trainZero.down;

		//walk add
		while(trainZero!=null &&trainZero.location<=destination)
		{
			route.add(trainZero);
			trainZero = trainZero.next;
		}
		return route;



	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @return
	 */
	public static TNode duplicate(TNode trainZero) {
		// WRITE YOUR CODE HERE
		
		TNode train = new TNode(trainZero.location, trainZero.next, trainZero.down);
		TNode bus = new TNode(trainZero.down.location, trainZero.down.next, trainZero.down.down);
		TNode walk = (trainZero.down.down);

		ArrayList<TNode> walkk = new ArrayList<TNode>();

		ArrayList<TNode> buss = new ArrayList<TNode>();

		ArrayList<TNode> trainn = new ArrayList<TNode>();


		while (walk != null)
		{
			walkk.add(new TNode(walk.location, walk.next, null));
			walk = walk.next;
		}

		while (bus != null)
		{
			buss.add(new TNode(bus.location, bus.next, bus.down));
			bus = bus.next;
		}
		
		while (train != null)
		{
			trainn.add(new TNode(train.location, train.next, train.down));
			train = train.
			next;
		}
		ArrayList<TNode> dwalk = new ArrayList<TNode>();
		ArrayList<TNode> dbus = new ArrayList<TNode>();
		ArrayList<TNode> dtrain = new ArrayList<TNode>();

		for (int i = 0; i <= walkk.size()-1; i ++)
		{ 
			dwalk.add (new TNode(i, null, null));
		}
		for (int i = 0; i < dwalk.size()-1; i++)
		{ 
			dwalk.get(i).next = dwalk.get(i+1);
		}

		for (int i = 0; i < buss.size(); i ++){ 
			dbus.add (new TNode(buss.get(i).location, null, null));
		}
		for (int i = 0; i < dbus.size()-1; i++){ 
			dbus.get(i).next = dbus.get(i+1);
		}
		for (int i = 0; i < dbus.size(); i++){ 
			dbus.get(i).down = dbus.get(dbus.get(i).location);
		}

		for (int i = 0; i < trainn.size(); i ++){ 
			dtrain.add (new TNode(trainn.get(i).location, null, null));
		}
		for (int i = 0; i < dtrain.size()-1; i++){ 
			dtrain.get(i).next = dtrain.get(i+1);
		}
		for (int i = 0; i < dtrain.size(); i++){ 
			int temp = buuu(dbus, dtrain.get(i).location);
			dtrain.get(i).down = dbus.get(temp);
		}
		return dtrain.get(0);
	}
	private static int buuu(ArrayList<TNode> bus, int position)
	{
		for (int i = 0; i < bus.size(); i ++)
		{
			if (bus.get(i).location == position)
			{
				return i;
			}
		}
		return 0;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param trainZero The zero node in the train layer of the given layered list
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public static void addScooter(TNode trainZero, int[] scooterStops)
	{
		TNode bus = trainZero.down;
		TNode walk = bus.down;
		TNode first=new TNode(0);

		bus.down=first;
		first.down=walk;

		ArrayList<TNode> walkk = new ArrayList<TNode>();
		ArrayList<TNode> buss = new ArrayList<TNode>();

		while(walk != null)
		{
			walkk.add(walk);
			walk= walk.next;
		}
		while(bus!= null)
		{
			buss.add(bus);
			bus= bus.next;
		}

		ArrayList<TNode> sclist = new ArrayList<TNode>();
		sclist.add(first);

		for (int i = 0; i < scooterStops.length; i++)
		{
			sclist.add(new TNode (scooterStops[i]));
		}
		for (int i = 0; i <= scooterStops.length; i++)
		{
			sclist.get(i).down = walkk.get(sclist.get(i).location);
		}
		for (int i = 0; i < scooterStops.length; i++)
		{
			sclist.get(i).next = sclist.get(i+1);
		}

		for (int i = 0; i < buss.size(); i++){  //connects bus nodes down
			TNode scooterPosition = scooterhelper(sclist, buss.get(i).location);
			buss.get(i).down = scooterPosition;
		}
	}

	private static TNode scooterhelper(ArrayList<TNode> scooter, int position){
		for (int i = 0; i < scooter.size(); i ++)
		{
			if (scooter.get(i).location == position)
			{
				return scooter.get(i);
			}
		}
		return null;
	}
}