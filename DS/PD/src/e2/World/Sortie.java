package e2.World;

import e2.Node.*;
import e2.Node.BranchRoute.BranchNode;
import e2.Node.FixedRoute.FixedRouteNode;

public class Sortie {
	private final Fleet fleet;
	private final Node start_node;
	private Node current_node;
	private Node last_node;

	public Sortie(Node start_node, Fleet fleet){
		this.start_node = start_node;
		this.current_node = start_node;
		this.fleet = fleet;
	}

	public String simulate() {
		while (this.current_node != null && !this.fleet.isSunk() && !(this.last_node instanceof EndNode)) {
			this.last_node = this.current_node;
			this.current_node.setFleet(this.fleet);
			this.current_node = this.current_node.executeNode();
		}

		return "Sortie Result:\n\t" +
				(!fleet.isSunk()? "SUCCESS":"FAIL") +
				"\n\tLast Visited Node: " + this.last_node.getName() +
				"\n\tFinal HP: " + this.fleet.getHP();
	}

	private int getMinNecessaryNodesAuxiliar(Node node, int cnt) {
		if (node == null)
			return cnt - 1;
		else if (node instanceof BranchNode)
			return Math.min(getMinNecessaryNodesAuxiliar(node.getChildNodes()[0], cnt+1), getMinNecessaryNodesAuxiliar(node.getChildNodes()[1], cnt+1));
		else if (node instanceof FixedRouteNode)
			return getMinNecessaryNodesAuxiliar(node.getChildNodes()[0], cnt+1);
		else //endNode
			return cnt;
	}

	public String getMinNecessaryNodes() {
		return "Smallest Node Count to End: " + getMinNecessaryNodesAuxiliar(this.start_node, 1);
	}

	private String toStringAux(Node node) {
        if (node instanceof EndNode)
			return "(" + node.getName() + " " + node.getNodeType() + ")";
        else if (node instanceof FixedRouteNode)
			return "(" + node.getName() + " " + node.getNodeType() + ", " + toStringAux(node.getChildNodes()[0]) + ")";
        else if (node instanceof BranchNode)
			return "(" + node.getName() + " " + node.getNodeType() + ", " + toStringAux(node.getChildNodes()[0]) + ", " + toStringAux(node.getChildNodes()[1]) + ")";
        else throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		return toStringAux(this.start_node);
	}
}
