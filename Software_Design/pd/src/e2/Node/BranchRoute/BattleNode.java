package e2.Node.BranchRoute;

import e2.World.Fleet;
import e2.Node.Node;

public class BattleNode extends BranchNode {

	Fleet enemyFleet;
	public BattleNode(String nodeName, Fleet enemyFleet) {
		super(nodeName);
		this.enemyFleet = enemyFleet;
	}

	@Override
	public Node executeNode() {

		enemyFleet.reduceHP(this.getFleet().getFire_power() - enemyFleet.getArmor());

		if(enemyFleet.isSunk()) {
			this.getChildNodes()[0].setFleet(this.getFleet());
			this.setFleet(null);
			return this.getChildNodes()[0];
		}

		this.getFleet().reduceHP(enemyFleet.getFire_power() - this.getFleet().getArmor());

		if(!this.getFleet().isSunk()){
			this.getChildNodes()[1].setFleet(this.getFleet());
			this.setFleet(null);
			return this.getChildNodes()[1];
		}

		return null;
	}

	@Override
	public String getNodeType() {
		return "Battle";
	}
}
