package backend.structures;

/**
 * Узел красно-чёрного дерева.
 */
public class RedBlackTreeNode {
	public static final boolean RED = true;
	public static final boolean BLACK = false;

	public int key;
	public boolean color;
	public RedBlackTreeNode left;
	public RedBlackTreeNode right;
	public RedBlackTreeNode parent;

	public RedBlackTreeNode(int key) {
		this.key = key;
		this.color = RED; // по умолчанию — красный при вставке
		this.left = null;
		this.right = null;
		this.parent = null;
	}

	@Override
	public String toString() {
		return (this.color == RED ? "R:" : "B:") + key;
	}
}
