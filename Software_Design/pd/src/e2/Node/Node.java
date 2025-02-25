package e2.Node;

import e2.World.Fleet;

public abstract class Node {

	private final String nodeName;
	private Fleet fleet;

	public Node(String nodeName){
		this.nodeName = nodeName;
	}

	public abstract void setChildNode(Node child);
	public abstract Node[] getChildNodes();
	public abstract Node executeNode();
	public void setFleet(Fleet fleet){this.fleet = fleet;}
	public Fleet getFleet() {return fleet;}
	public String getName() {return nodeName;}

	public abstract String getNodeType();
}
