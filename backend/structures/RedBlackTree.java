package backend.structures;

public class RedBlackTree {
	private RedBlackTreeNode root;

	public void insert(int key) {
		RedBlackTreeNode inserted = bstInsertReturnInserted(key);
		fixViolation(inserted);
	}

	private RedBlackTreeNode bstInsertReturnInserted(int key) {
		RedBlackTreeNode node = new RedBlackTreeNode(key);
		RedBlackTreeNode current = root;
		RedBlackTreeNode parent = null;

		while (current != null) {
			parent = current;
			if (key < current.key) {
				current = current.left;
			} else {
				current = current.right;
			}
		}

		node.parent = parent;

		if (parent == null) {
			root = node;
		} else if (key < parent.key) {
			parent.left = node;
		} else {
			parent.right = node;
		}

		return node;
	}

	private void fixViolation(RedBlackTreeNode z) {
		while (z != root && z.parent.color == RedBlackTreeNode.RED) {
			if (z.parent == z.parent.parent.left) {
				RedBlackTreeNode y = z.parent.parent.right;
				if (y != null && y.color == RedBlackTreeNode.RED) {
					z.parent.color = RedBlackTreeNode.BLACK;
					y.color = RedBlackTreeNode.BLACK;
					z.parent.parent.color = RedBlackTreeNode.RED;
					z = z.parent.parent;
				} else {
					if (z == z.parent.right) {
						z = z.parent;
						leftRotate(z);
					}
					z.parent.color = RedBlackTreeNode.BLACK;
					z.parent.parent.color = RedBlackTreeNode.RED;
					rightRotate(z.parent.parent);
				}
			} else {
				RedBlackTreeNode y = z.parent.parent.left;
				if (y != null && y.color == RedBlackTreeNode.RED) {
					z.parent.color = RedBlackTreeNode.BLACK;
					y.color = RedBlackTreeNode.BLACK;
					z.parent.parent.color = RedBlackTreeNode.RED;
					z = z.parent.parent;
				} else {
					if (z == z.parent.left) {
						z = z.parent;
						rightRotate(z);
					}
					z.parent.color = RedBlackTreeNode.BLACK;
					z.parent.parent.color = RedBlackTreeNode.RED;
					leftRotate(z.parent.parent);
				}
			}
		}
		root.color = RedBlackTreeNode.BLACK;
	}

	private void leftRotate(RedBlackTreeNode x) {
		RedBlackTreeNode y = x.right;
		x.right = y.left;
		if (y.left != null)
			y.left.parent = x;
		y.parent = x.parent;
		if (x.parent == null)
			root = y;
		else if (x == x.parent.left)
			x.parent.left = y;
		else
			x.parent.right = y;
		y.left = x;
		x.parent = y;
	}

	private void rightRotate(RedBlackTreeNode y) {
		RedBlackTreeNode x = y.left;
		y.left = x.right;
		if (x.right != null)
			x.right.parent = y;
		x.parent = y.parent;
		if (y.parent == null)
			root = x;
		else if (y == y.parent.left)
			y.parent.left = x;
		else
			y.parent.right = x;
		x.right = y;
		y.parent = x;
	}

	public String getTreeString() {
		StringBuilder sb = new StringBuilder();
		buildTreeString(root, 0, sb);
		return sb.toString();
	}

	private void buildTreeString(RedBlackTreeNode node, int depth, StringBuilder sb) {
		if (node != null) {
			buildTreeString(node.right, depth + 1, sb);
			sb.append("  ".repeat(depth)).append(node.toString()).append("\n");
			buildTreeString(node.left, depth + 1, sb);
		}
	}

	public void printInOrder() {
		System.out.println("RB-Tree in-order:");
		printInOrder(root, 0);
	}

	private void printInOrder(RedBlackTreeNode node, int depth) {
		if (node != null) {
			printInOrder(node.right, depth + 1);
			System.out.printf("%s%s%n", "  ".repeat(depth), node.toString());
			printInOrder(node.left, depth + 1);
		}
	}

	// 🔧 Для тестов
	public static void main(String[] args) {
		RedBlackTree tree = new RedBlackTree();
		tree.insert(10);
		tree.insert(7);
		tree.insert(3);
		tree.insert(11);
		tree.printInOrder();
	}
}
