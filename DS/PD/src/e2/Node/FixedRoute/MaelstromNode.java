package e2.Node.FixedRoute;

import e2.Node.Node;

public class MaelstromNode extends FixedRouteNode{

	private final int strength;

	public MaelstromNode(String nodeName, int strength) {
		super(nodeName);
		this.strength = strength;
	}


	@Override
	public Node executeNode() {
		this.getFleet().reduceHP(this.getFleet().getLine_of_sight() > strength ? 0 : 10);

		if (!this.getFleet().isSunk()) {
			this.getChildNodes()[0].setFleet(this.getFleet());
			this.setFleet(null);
			return this.getChildNodes()[0];
		}

		return null;
	}

	@Override
	public String getNodeType() {
		return "Maelstrom";
	}
}
