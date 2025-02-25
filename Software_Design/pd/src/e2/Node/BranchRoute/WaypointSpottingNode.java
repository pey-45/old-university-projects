package e2.Node.BranchRoute;

import e2.Node.Node;

public class WaypointSpottingNode extends BranchNode {

	private final int distance;
	public WaypointSpottingNode(String nodeName, int distance) {
		super(nodeName);
		this.distance = distance;
	}

	@Override
	public Node executeNode() {
		if (this.getFleet() == null) return null;

		int i = this.getFleet().getLine_of_sight() >= this.distance? 0:1;

		this.getChildNodes()[i].setFleet(this.getFleet());
		this.setFleet(null);
		return this.getChildNodes()[i];
	}

	@Override
	public String getNodeType() {
		return "WaypointSpotting";
	}
}
