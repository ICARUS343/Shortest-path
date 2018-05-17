PROJECT 4
Praphul Shivakoti
pshivako@u.rochester.edu

Project partner : 
Aman Shrestha
ashrest2


Running the code:
1.Compile all the java files in the src folder.
2.Run Graph.java with the appropriate arguments.

Classes:
1.Node: A basic node class which has field variables for ID, longitude, latitude, and a adjacency linked list. Nodes doesn't implement comparable but we have a separate node like class in graph that does; the idea being that we only wanted to compare all the vertex which are connected.

2.Edge: A basic edge class which has field variables for ID, Intersection id1,Intersection id2, and weight.
 
3.Graph: 
Functions: 
1.readtext(String): reads in all the lines from the text files and stores them as a list of Nodes or Edges depending on the line.
2.distance(double,double,double,double): calculates distance between any two points. 
3.Dijkstra(String,String): implements the disjkstra's algorithm using a priority queue.
4.loadimage(String): loads the marker image file.
5.paintComponent: Scales everything to fit the screen.
6.setDistance: Calculates the new distance


Extra Credit:
1.The marker image: Just changing the color of the track wasn't always enough to see the track, so we added a marker at the start and end point.
2.Distance and time: a message box displays the distance, and time required to reach the destination.

Dijkstra's Algorithm implemented by Aman Shrestha.
Debugged by Praphul Shivakoti.
Graphics done by Praphul Shivakoti.
Debugged by Aman Shrestha.

For the Dijkstra's algorithm, a new class info has been created. 
This class has the properties required to implement Dijkstra's algorithm such as traversed, Node, distance and parent.
These weren't included in the Node class to save space.
For each node, these values are only required when finding the shortest path.
So, storing them otherwise would just take up space.

The dijkstras method uses a hashmap of nodes and their info
The priority queue has instances of this info class. The info class compares its instances by the distance.
Priority queue has been updated after changing the priority of the nodes.

To make the algorithm faster, we add just the children in the priorityqueue and not all the nodes.

The run time of the algorithm is Elog(V)
Expected run time of showing the UR map <1 second
Expected run time of showing the monroe map <2 seconds
Expected run time of showing the NYS map <10 seconds

Expected run time of displaying direction in UR map <1 second
Expected run time of displaying direction in monroe map <10 seconds
Expected run time of displaying direction in nys map <50 seconds(for very long path)


https://github.com/jasonwinn/haversine/blob/master/Haversine.java
reffered to for calculation of distance

For calcuating the speed average for extra credit, the following sites were referred to
https://www.usatoday.com/story/money/cars/2014/08/21/fastest-slowest-states/14386951/
https://www.westernite.org/datacollectionfund/2005/psu_ped_summary.pdf


