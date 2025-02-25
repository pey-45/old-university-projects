package e2.Node;

public class EndNode extends Node {

	public EndNode(String nodeName) {
		super(nodeName);
	}

	@Override
	public Node executeNode() {
		return null;
	}

	@Override
	public void setChildNode(Node childNode) {
		throw new UnsupportedOperationException("EndNodes can't have child nodes");
	}

	@Override
	public Node[] getChildNodes() {
		throw new UnsupportedOperationException("EndNodes can't have child nodes");
	}

	@Override
	public String getNodeType() {
		return "End";
	}
}
