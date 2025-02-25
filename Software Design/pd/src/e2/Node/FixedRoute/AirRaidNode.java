package e2.Node.FixedRoute;

import e2.Node.Node;

public class AirRaidNode extends FixedRouteNode {

	private final int aircraft_power;

	public AirRaidNode(String nodeName, int aircraftPower) {
		super(nodeName);
		this.aircraft_power = aircraftPower;
	}

	@Override
	public Node executeNode() {
		this.getFleet().reduceHP(aircraft_power - (2 * this.getFleet().getAnti_air() + this.getFleet().getArmor()));

		if (!this.getFleet().isSunk()) {
			this.getChildNodes()[0].setFleet(this.getFleet());
			this.setFleet(null);
			return this.getChildNodes()[0];
		}

		return null;
	}

	@Override
	public String getNodeType() {
		return "AirRaid";
	}
}
