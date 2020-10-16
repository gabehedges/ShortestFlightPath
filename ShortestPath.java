import java.util.Map;
import java.io.*;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
public class ShortestPath {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.print("Enter a filename containing the paths: ");
		String myFile = kb.nextLine();
		while(myFile.isEmpty()) {
			System.out.print("Enter a filename containing the paths: ");
			myFile = kb.nextLine();
		}
		Map< String, List<Path> > adjList = readPaths(myFile); //loading file into adjacency list
		displayAdjacencyList(adjList);
		System.out.print("\nEnter a start city (empty line to quit): ");
		String startCity = kb.nextLine();
		
		while(startCity.length() > 0) { //will run until empty line is entered
		Map<String, Double> dist = findDistances(startCity,adjList);
		displayShortest(startCity,dist);
		System.out.print("\nEnter a start city (empty line to quit): ");
		startCity = kb.nextLine();
		} System.out.println("Peace out!");

	}

	
	
	public static Map< String, List<Path> > readPaths(String fname) {
	Map<String,List<Path>> aList = new 	HashMap<String,List<Path>>();
	try {
		/*
		 * Reading from first start city
		 */
	File inputFile = new File(fname);
	Scanner inFile = new Scanner(inputFile);
	while(inFile.hasNext()) {
	String inputLine = inFile.nextLine();	//Breaking down the elements of the line
	String[] elements = inputLine.split(",");
	String start = elements[0];
	String end = elements[1];
	Double cost = Double.parseDouble(elements[2]);
	if (aList.containsKey(start)) { //If the start city is already in the Map...
		List<Path> listOfPaths = aList.get(start); //getting list of paths from map
		Path adjCity = new Path(end,cost); //adding the adjacent city to the list of paths
		listOfPaths.add(adjCity);
	} else { //if start city isn't in map
		Path adjCity = new Path(end,cost);
		List<Path> listOfPaths = new LinkedList<Path>();
		listOfPaths.add(adjCity);
		aList.put(start, listOfPaths);		
	}	
	} inFile.close();
	/*
	 * Reading from first start city
	 */
		Scanner inFile2 = new Scanner(inputFile);
		while(inFile2.hasNext()) {
		String inputLine = inFile2.nextLine();	//Breaking down the elements of the line
		String[] elements = inputLine.split(",");
		String start = elements[1]; //Switched starting city
		String end = elements[0];
		Double cost = Double.parseDouble(elements[2]);
		if (aList.containsKey(start)) { //If the start city is already in the Map...
			List<Path> listOfPaths = aList.get(start); //getting list of paths from map
			Path adjCity = new Path(end,cost); //adding the adjacent city to the list of paths
			listOfPaths.add(adjCity);
		} else { //if start city isn't in map
			Path adjCity = new Path(end,cost);
			List<Path> listOfPaths = new LinkedList<Path>();
			listOfPaths.add(adjCity);
			aList.put(start, listOfPaths);		
		}	
		} inFile2.close();

	}
	
	catch(FileNotFoundException e) {
		System.out.println("Error loading file.");
	}
	return aList;
	}
/*
 * Display adjacency list neatly formatted
 */

	public static void displayAdjacencyList(Map< String,List<Path> > map) {
		System.out.println();
		System.out.println("Start City     Paths               \r\n" + 
				"-------------- ------------------------------");
		Set<String> keySet = map.keySet();
		for(String key : keySet) {
			System.out.printf("%-15s", key);
			List<Path> paths = map.get(key); //gets the list from current start city
			int i = 0;
			while (i < paths.size()) { //gets each element from list
				if (i == paths.size() - 1) {
					System.out.print("(" + paths.get(i).getEndpoint() + ": "+ paths.get(i).getCost() +")" );
				} else {
				System.out.print("(" + paths.get(i).getEndpoint() + ": "+ paths.get(i).getCost() +"), " );
				}
				i++;
			} System.out.println();
		}
	}
/*
 * Shortest path algorithm method
 */
	public static Map<String, Double> findDistances(String start, Map<String, List<Path>> adj_list) {
		Map<String, Double> shortestDistances = new HashMap<String, Double>();
		PriorityQueue<Path> pQ = new PriorityQueue<Path>();
		Path startPath = new Path(start,0.0);
		pQ.add(startPath);
		
		while(!pQ.isEmpty()) {
			Path current = pQ.remove();
			if (!shortestDistances.containsKey(current.getEndpoint())) { //if map DOES NOT contain the current city name
			Double d = current.getCost();
			String dest = current.getEndpoint();
			shortestDistances.put(dest, d); //shortest distances from initial node to this endpoint
			List<Path> adjacentPaths = adj_list.get(dest); //gets list from dest node
				for(Path p : adjacentPaths) {
					Path adjustedCost = new Path(p.getEndpoint(), p.getCost()+d);
					pQ.add(adjustedCost);
				}
			}
		}
		return shortestDistances;
	}
	public static void displayShortest(String start, Map<String, Double> shortest) {
		System.out.println("\nDistances from " + start + " to each city: ");
		System.out.println("Dest. City    Distance");
		System.out.println("-------------- --------");
		Set<String> cities = shortest.keySet();
		for (String city : cities) {
			System.out.printf("%-15.15s %5.2f %n",city, shortest.get(city));	
		}

	}
}
