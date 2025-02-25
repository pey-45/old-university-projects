package e2.Node.BranchRoute;

import e2.Node.Node;

public abstract class BranchNode extends Node {

	private Node left_child;
	private Node right_child;

	public BranchNode(String nodeName) {super(nodeName);}

	@Override
	public void setChildNode(Node child) {
		if (this.left_child == null)
			this.left_child = child;
		else if (this.right_child == null)
			this.right_child = child;
		else
			throw new IllegalArgumentException();
	}

	@Override
	public Node[] getChildNodes() {
		return new Node[]{this.left_child, this.right_child};
	}
}
