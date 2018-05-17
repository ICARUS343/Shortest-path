import java.util.LinkedList;



public class Node {
	
	String ID;
	double longitude;
	double latitude;
	LinkedList<Node> connected=new LinkedList<Node>();
	
	public Node(String ID, double longitude,double latitude) {
		this.ID=ID;
		this.longitude=longitude;
		this.latitude=latitude;
	}

	

}
