

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Graph extends JPanel{
	//option pane
	 static JOptionPane panel1;
	
	
	//for checking the arguments
	boolean showmap=false;
	boolean direction=false;
	
	 double distanceValue=0;

	//for scaling
	double minLat=Double.MAX_VALUE;
	double minLong=Double.MAX_VALUE;
	double maxLat=Double.MIN_VALUE;
	double maxLong=-Double.MAX_VALUE;

	String filename="";
	String FID="";
	String SID="";
	ArrayList<Node> entirePath;


	@Override
	public void paintComponent(Graphics g) {
		//Graphics2D g = (Graphics2D)g2d;
		//g.setStroke(new BasicStroke(1));
		int w=getWidth();
		int h=getHeight()+50;
		g.setColor(new Color(0,102,51));
		g.fillRect(0, 0, w, h);
		g.setColor(Color.WHITE);

		//finds the difference between min and max Longitude and Latitude
		double diffX=Math.abs(maxLat-minLat);
		double diffY=Math.abs(maxLong-minLong);

		for(Edge edge:edges) {

			double latitudeFirst=nodes.get(edge.Intersection1ID).latitude;
			double longitudeFirst=nodes.get(edge.Intersection1ID).longitude;

			double latitudeSecond=nodes.get(edge.Intersection2ID).latitude;
			double longitudeSecond=nodes.get(edge.Intersection2ID).longitude;

			//scaling algorithm
			int x1Scale=(int)(((longitudeFirst-minLong)/(diffY)*h));
			int x2Scale=(int)(((longitudeSecond-minLong)/(diffY)*h));

			double y1Scale=(((latitudeFirst-minLat)/(diffX)*w));
			double y2Scale=(((latitudeSecond-minLat)/(diffX)*w));

			g.drawLine((int)x1Scale, h-(int)y1Scale,(int) x2Scale,h-(int) y2Scale);
		}

		g.setColor(Color.RED);
		//if we are getting shortest path
		if(this.direction) {
			ArrayList<Node>Nodes= this.entirePath;//gets the list of nodes

			
			for(int i=0;i<Nodes.size()-1;i++) {
				double latitudeFirst=Nodes.get(i).latitude;
				double longitudeFirst=Nodes.get(i).longitude ;

				double latitudeSecond=Nodes.get(i+1).latitude;
				double longitudeSecond=Nodes.get(i+1).longitude;

				//adds the distance
				this.distanceValue=this.distanceValue+ this.distance(latitudeFirst, longitudeFirst, latitudeSecond, longitudeSecond);
				int x1Scale=(int)(((longitudeFirst-minLong)/(diffY)*h));
				int x2Scale=(int)(((longitudeSecond-minLong)/(diffY)*h));

				double y1Scale=(((latitudeFirst-minLat)/(diffX)*w));
				double y2Scale=(((latitudeSecond-minLat)/(diffX)*w));

				//draws the line on the edge
				g.drawLine((int)x1Scale, h-(int)y1Scale,(int) x2Scale,h-(int) y2Scale);

				//puts markers on start and end point
				if(i==0) {
					g.drawImage(loadImage("point1.png"),(int)x1Scale,h-(int)y1Scale,null);
				}
				if(i==Nodes.size()-2) {
					g.drawImage(loadImage("point1.png"),(int)x2Scale,h-(int)y2Scale,null);
				}
			}			
		}

	}
	
	public void setDistance() {
		//ArrayList<Node>Nodes= this.entirePath;//gets the list of nodes
		for(Edge edge:this.edges) {
			double latitudeFirst=nodes.get(edge.Intersection1ID).latitude;
			double longitudeFirst=nodes.get(edge.Intersection1ID).longitude;

			double latitudeSecond=nodes.get(edge.Intersection2ID).latitude;
			double longitudeSecond=nodes.get(edge.Intersection2ID).longitude;
			this.distanceValue=this.distanceValue+ this.distance(latitudeFirst, longitudeFirst, latitudeSecond, longitudeSecond);	
		}
	}

	public BufferedImage loadImage(String filename) {
		Class myClass = getClass();
		ClassLoader loader = myClass.getClassLoader();//getting the ClassLoader
		String path;
		try {
			URL myURL = loader.getResource(filename);//gets the URL of the file
			path = myURL.getPath();//gets the path from the URL
			path=path.replaceAll("%20", " ");//fixes the path with space instead of %20
		}catch(NullPointerException e) {//if file does not exist
			System.out.println("No file found");
			return null;
		}
		try {
			return ImageIO.read(new File(path));
		}catch(Exception e) {
			System.out.println("Failed");
			return null;
		}
	}

	HashMap<String,Node>nodes=new HashMap<String,Node>();
	ArrayList<Edge>edges=new ArrayList<Edge>();

	public void readText(String filename) throws FileNotFoundException {
		Class myClass = getClass();
		ClassLoader loader = myClass.getClassLoader();//getting the ClassLoader
		String path;

		URL myURL = loader.getResource(filename);//gets the URL of the file

		path = myURL.getPath();//gets the path from the URL
		path=path.replaceAll("%20", " ");//fixes the path with space instead of %
		Scanner sc=new Scanner (new File(path));//scans the file

		while (sc.hasNextLine()) {
			StringTokenizer st=new StringTokenizer(sc.nextLine()," \t");//tokenizes based on tab
			while (st.hasMoreTokens()) {
				String identifier=st.nextToken();
				if(identifier.equals("i")) {//intersection

					//sets relevant information
					String intersectionID=(st.nextToken());
					double latitude=Double.parseDouble(st.nextToken());
					double longitude=Double.parseDouble(st.nextToken());

					minLat=Math.min(minLat, latitude);
					minLong=Math.min(minLong, longitude);
					maxLat=Math.max(maxLat, latitude);
					maxLong=Math.max(maxLong, longitude);

					Node intersection=new Node(intersectionID,longitude,latitude);
					this.nodes.put(intersectionID, intersection);

				}else if(identifier.equals("r")) {//edge
					//sets relevant information
					String roadID=(st.nextToken());
					String intersection1ID=(st.nextToken());
					String intersection2ID=(st.nextToken());
					Edge edge=new Edge(roadID,intersection1ID,intersection2ID);

					edge.weight=distance(nodes.get(intersection1ID).latitude, nodes.get(intersection1ID).longitude, nodes.get(intersection2ID).latitude, nodes.get(intersection2ID).longitude);

					nodes.get(intersection1ID).connected.add(nodes.get(intersection2ID));
					nodes.get(intersection2ID).connected.add(nodes.get(intersection1ID));
					edges.add(edge);
				}

			}

		}


	}


	public class info implements Comparable<info>{//nested class for dijkstra's algorithm

		Node node;
		double distance;
		Node parent;
		boolean traversed;

		info(Node node,boolean traversed, double distance, Node parent){
			this.node=node;
			this.traversed=traversed;
			this.distance=distance;
			this.parent=parent;
		}

		@Override
		public int compareTo(info o) {//for priority queue
			// TODO Auto-generated method stub
			//checking done based on distance
			if(this.distance>o.distance)return 1;
			else if(this.distance<o.distance)return -1;
			else return 0;
		}

	}


	public ArrayList<Node> dijkstra(String node1,String node2) {

		PriorityQueue<info> queue=new PriorityQueue<info>();//priority queue of info
		Node temporary=nodes.get(node1);//gets the first of the two nodes
		HashMap<Node,info> information=new HashMap<Node,info>();//stores information of the nodes


		for(String node:nodes.keySet()) {//stores information of each node in the hashmap
			//each info has node, traversed as false, infity as distance and null as parent
			information.put(nodes.get(node), new info(nodes.get(node),false,Double.MAX_VALUE,null));
		}

		//starts dijkstra's algorithm
		information.get(temporary).traversed=true;
		information.get(temporary).distance=0;
		queue.add(information.get(nodes.get(temporary.ID)));

		double distance=0;
		while(!queue.isEmpty()) {
			Node temp=queue.poll().node;//polls the one with the smallest distance

			distance=information.get(temp).distance;//gets distance
			information.get(temp).traversed=true;//sets as traversed
			double remember=distance;//distance remembered to add later
			for (Node connectedOnes:temp.connected) {
				distance=remember;//for each of the connected ones
				double added=distance(temp.latitude, temp.longitude, connectedOnes.latitude, connectedOnes.longitude);

				distance=distance+added;//changes the distance
				if(distance<information.get(connectedOnes).distance && !information.get(connectedOnes).traversed) {
					//if the distance is less than what it previously had, it updates the info
					information.get(connectedOnes).distance=distance;
					information.get(connectedOnes).parent=temp;
					queue.add(information.get(connectedOnes));
				}
				//this is to make sure the priority queue is updated properly
				PriorityQueue<info> newqueue=new PriorityQueue<info>();
				while(!queue.isEmpty()) {
					newqueue.add(queue.poll());
				}
				queue=newqueue;

			}
		}
		//the algorithm is complete. Now we find the shortest path

		Node temp=nodes.get(node2);
		ArrayList<Node> path=new ArrayList<Node>();//This is store the nodes, the shortest path

		while(temp!=nodes.get(node1) ) {

			System.out.println(temp.ID);
			if(information.get(temp).parent==null) {
				System.out.println("No connection");//no connection if parent is null.i.e. the node was never reached
				this.direction=false;
				break;
			}
			else {
				path.add(temp);//adds the node
				temp=information.get(temp).parent;//goes to the parent
			}

		}

		return path;//returns the list

	}




	//https://github.com/jasonwinn/haversine/blob/master/Haversine.java
	//was refered to for he following code.
	private static final int EARTH_RADIUS = 3959; // Approx Earth radius in KM

	public static double distance(double startLat, double startLong,double endLat, double endLong) {

		double dLat  = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));

		startLat = Math.toRadians(startLat);
		endLat   = Math.toRadians(endLat);

		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c; // <-- d
	}

	public static double haversin(double val) {
		return Math.pow(Math.sin(val / 2), 2);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Graph test=new Graph();
		try {



			JFrame frame=new JFrame("Map");
			frame.setSize(1000, 1000);
			frame.add(test);


			int c=args.length;
			test.filename=args[0];
			//checks the arguments
			if(c==2) {
				if (args[1].equals("--show")) {
					test.direction=false;
				}else {
					throw new IOException("Invalid entry");
				}
			}
			if(c==4) {
				if(args[1].equals("--directions")) {
					test.direction=true;
					test.FID=args[2];
					test.SID=args[3];
				}else {
					throw new IOException("Invalid entry");
				}
			}
			if(c==5) {
				if(args[1].equals("--show") && args[2].equals("--directions") || args[2].equals("--show") && args[1].equals("--directions")) {
					test.direction=true;
					test.FID=args[3];
					test.SID=args[4];
				}
				else {
					throw new IOException("Invalid entry");
				}
			}



			test.readText(test.filename);
			//runs dijkstra's algorithm
			if(test.direction) {
				test.entirePath= test.dijkstra(test.FID	,test.SID);
				test.repaint();
				
			}


			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			
			if(test.direction) {
				test.setDistance();
				 String message1 = "Distance = "+test.distanceValue +"miles"; 
				 String message2 = "Walking will take:"+ test.distanceValue/3.1+"hour";
				 String message3 = "Driving will take:"+ test.distanceValue/50+"hour";
				JOptionPane.showMessageDialog(panel1, message1+"\n"+message2+"\n"+message3);
			}
		}
		catch(Exception e){
			test.edges.clear();

			System.out.println("Input Error!!! Try again.");
		}
	}

}


