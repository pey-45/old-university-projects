package e2.Node.FixedRoute;

import e2.Node.Node;

public abstract class FixedRouteNode extends Node {

	private Node child;

	public FixedRouteNode(String nodeName) {super(nodeName);}

	@Override
	public void setChildNode(Node child){
		if (this.child == null)
			this.child = child;
		else throw new IllegalArgumentException();
	}

	@Override
	public Node[] getChildNodes() {
		return new Node[]{this.child};
	}
}
