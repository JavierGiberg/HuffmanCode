package assign1;
/**
 * Assignment 1 Submitted by: Javier Giberg. ID# 302280383 Netanel Bitton. ID#
 * 305484651
 */
public class Node implements Comparable<Node> {
	private Byte data;
	private int frequency;
	private Node left, right;

	public Node(Byte data, int frequency) {
		this.data = data;
		this.frequency = frequency;
	}

	public Byte getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Node [data=" + data + ", frequency=" + frequency + ", left=" + left + ", right=" + right + "]";
	}

	public int getFrequency() {
		return frequency;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	@Override
	public int compareTo(Node that) {
		return this.frequency - that.frequency;
	}

	public void Order() {
		if (this.left != null) {
			this.left.Order();
		}
		if (this.right != null) {
			this.right.Order();
		}
	}
}
