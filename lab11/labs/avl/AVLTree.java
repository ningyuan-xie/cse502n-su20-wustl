package avl;

import java.util.LinkedList;

public class AVLTree<T extends Comparable<T>> {

	/* For the AVL Lab, find all 'FIXME's and
	 * supply the code asked for.
	 */

	private TreeNode<T> root;
	public int size;
	
	public AVLTree() {
	    this.root = null;
	    this.size = 0;
	}
	
	////////////////////////////////////////////////////////
	
	//
	// exists()
	// Check whether a specified value exists in the set
	//
	
	public boolean exists(T value) {
	    return existsHelper(value, this.root);
	}
	
	//
	// existsHelper()
	// (Optionally) recursive procedure to traverse a tree
	// rooted at "root" to find a specified value.  
	//
	// RETURNS: true if the value is found, else false
	//
	private boolean existsHelper(T value, TreeNode<T> root) {
		if (root == null) { // not found
			return false;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { // found
	    		return true;
	    	} else if (comparison < 0) { // still looking - go left
	    		return existsHelper(value, root.left);
	    	} else { // still looking - go right
	    		return existsHelper(value, root.right);
	    	}
	    }
	}
	
	////////////////////////////////////////////////////////
	
	//
	// min()
	// Return the minimum value in the set
	//
	// If the set is empty, result is undefined.
	//
	public T min() {
	    return minValueInSubtree(this.root);
	}
	
	//
	// minValueInSubTree()
	// Find the smallest value in the subtree rooted at
	// the specified node.
	//
	// ASSUMED: root is not null.
	//
	private T minValueInSubtree(TreeNode<T> root) {
	    while (root.left != null)
	    	root = root.left;
	    
	    return root.value;
	}

	//
	// max()
	// Return the maximum value in the set
	//
	// If the set is empty, result is undefined.
	//
	
	public T max() {
	    return maxValueInSubtree(this.root);
	}

	
	//
	// maxValueInSubTree()
	// Find the largest value in the subtree rooted at
	// the specified node.
	//
	// ASSUMED: root is not null.
	//
	private T maxValueInSubtree(TreeNode<T> root) {
	    while (root.right != null)
	    	root = root.right;
	    
	    return root.value;
	}
	
	////////////////////////////////////////////////////////
	
	//
	// insert()
	// Insert the specified value in the set if it does not
	// already exist.
	//
	// RETURNS: the size of the set after insertion.
	//
	public int insert(T value) 
	{
	    this.root = insertHelper(value, this.root);
	    return size;
	}
	
	//
	// insertHelper()
	// Recursive procedure to insert a value into the subtree
	// rooted at "root".  If value is already present in the
	// tree, nothing is inserted.
	//
	// RETURNS: root node of subtree after insertion
	//
	// FIXME: add the necessary code to this function
	// to maintain height and rebalance the tree when
	// a node is removed.
	//
	private TreeNode<T> insertHelper(T value,
					 TreeNode<T> root) {
		
		// 1. Case 1--Base case: successfully inserted a value into the subtree rooted at this empty "root"
		if (root == null) {
			// add new element as leaf of tree
			TreeNode<T> newNode = new TreeNode<T>(value); 
			
			size++;
			
			return newNode;
	    } 
		else {
	    	int comparison = value.compareTo(root.value);
	    	
	    	// 2. Case 2--Duplicate case: insertion failed
	    	if (comparison == 0) {
	    		// duplicate element -- return existing node
	    		return root;
	    	
	    	// 3. Case 3: value < existing root, so go left
	    	} else if (comparison < 0) {
	    		// still looking -- go left
	    		root.setLeft(insertHelper(value, root.left));
	    	
		    // 4. Case 4: value > existing root, so go right
	    	} else {
	    		// still looking -- go right
	    		root.setRight(insertHelper(value, root.right));
	    	}

	    	// 1. update height
	    	updateHeight(root);
	    	
			// 2. rebalance root
			root = rebalance(root);
			
	    	return root;
	    }
	}

	////////////////////////////////////////////////////////
	
	//
	// remove()
	// Remove a value from the set if it is present
	//
	public void remove(T value) {
	    this.root = removeHelper(value, this.root);
	}
	
	//
	// removeHelper()
	// Recursive procedure to remove a value from the
	// subtree rooted at "root", if it exists.
	//
	// RETURNS root node of subtree after insertion
	//
	// FIXME: add the necessary code to this function
	// to maintain height and rebalance the tree when
	// a node is removed.
	//
	private TreeNode<T> removeHelper(T value,
					 TreeNode<T> root) {
	    
	    if (root == null) { // did not find element
	    	return null;
	    } else {
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { // found element to remove
	    		if (root.left == null || root.right == null) {
	    			// base case -- root has at most one subtree,
	    			// so return whichever one is not null (or null
	    			// if both are)
	    			size--;
	    			return (root.left == null ? root.right : root.left);
	    		} else {
	    			// node with two subtrees -- replace key
	    			// with successor and recursively remove
	    			// the successor.
	    			T minValue = minValueInSubtree(root.right);
	    			root.value = minValue;
			
	    			root.setRight(removeHelper(minValue, root.right));
	    		}
	    	} else if (comparison < 0) {
	    		// still looking for element to remove -- go left
	    		root.setLeft(removeHelper(value, root.left));
	    	} else {
	    		// still looking for element to remove -- go right
	    		root.setRight(removeHelper(value, root.right));
	    	}
	    	
	    	// 1. update height
	    	updateHeight(root);
	    	
			// 2. rebalance root
			root = rebalance(root);
			
	    	return root;
	    }
	}


	
	////////////////////////////////////////////////////////
	//
	// INTERNAL METHODS FOR MAINTAINING BALANCE
	//
	
	// updateHeight()
	//
	// Recompute the height of the subtree rooted at "root",
	// assuming that its left and right children (if any)
	// have correct heights for their respective subtrees.
	//
	// EFFECT: Set the root's height field to the updated value
	//
	
	
	private void updateHeight(TreeNode<T> root) {
	    // FIXME: fill in the update code
		TreeNode<T> root_left = root.left;
		TreeNode<T> root_right = root.right;
		
		// 1. Case 1: root has no child node, so height = 0
		if (root_left == null && root_right == null) {
			root.height = 0;
		}
		
		// 2. Case 2: root only has left child node, so height = left child node's height + 1
		else if (root_left != null && root_right == null) {
			root.height = root_left.height + 1;
		}
		
		// 3. Case 3: root only has right child node, so height = right child node's height + 1
		else if (root_left == null && root_right != null) {
			root.height = root_right.height + 1;
		}
		
		// 4. Case 4: root has both child nodes, so height = max(children's node) + 1
		else {
			root.height = Math.max(root_left.height, root_right.height) + 1;
		}
	}
		
	////////////////////////////////////////////////////////////////////////////////////////////

	//
	// getBalance()
	// Return the balance factor of a subtree rooted at "root"
	// (right subtree height - left subtree height)
	//
	
	////////////////////////////////////////////////////////////////////////////////////////////	
	private int getBalance(TreeNode<T> root) {
	    // FIXME: fill in the balance computation
		
		TreeNode<T> root_left = root.left;
		TreeNode<T> root_right = root.right;
		
		// 1. Case 1: root has no child node, so balance factor = 0
		if (root_left == null && root_right == null ) {
			return 0;
		}
		
		// 2. Case 2: root only has left child node, so balance factor = (-1) - left child node's height
		if (root_left != null && root_right == null) {
			return (-1) - root_left.height;
		}

		// 3. Case 3: root only has right child node, so balance factor = right child node's height - (-1)
		if (root_left == null && root_right != null ) {
			return root_right.height - (-1);
		}
		
		// 4. Case 4: root has both child nodes, so height = right child node's height - left child node's height       
		return root_right.height - root_left.height;
		
	}
	////////////////////////////////////////////////////////////////////////////////////////////

	//
	// rebalance()
	//
	// Rebalance an AVL subtree, rooted at "root", that has possibly
	// been unbalanced by a single node's insertion or deletion.
	//
	// RETURNS: the root of the subtree after rebalancing
	//
	
	////////////////////////////////////////////////////////////////////////////////////////////
	private TreeNode<T> rebalance(TreeNode<T> root) {
	    // FIXME: fill in the rebalancing code
		
		TreeNode<T> root_left = root.left;
		TreeNode<T> root_right = root.right;
		
		
		if (getBalance(root) == -2) {
			
			if (getBalance(root_left) == 1) {   		// Case 2, needs 2 rotations: L + R
				root.setLeft(leftRotate(root_left));		// Rotate left on root_left. Return new subtree root.
			}
			
			return(rightRotate(root));					// Case 1, only 1 rotation: R. Return the new root.
		}
		
		if (getBalance(root) == 2) {
			
			if (getBalance(root_right) == -1) {			// Case 2 Opposite, needs 2 rotations: R + L
				root.setRight(rightRotate(root_right));		// Rotate right on root_right. Return new subtree root.
			}
			
			return(leftRotate(root));					// Case 1 Opposite, only 1 rotation: L. Return the new root.
		}
		
	    return root;
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	
	//
	// rightRotate()
	// Perform a right rotation on a tree rooted at "root"
	// The tree's root is assumed to have a left child.
	//
	// RETURNS: the new root after rotation.
	//
	private TreeNode<T> rightRotate(TreeNode<T> root) {
	    // FIXME: fill in the rotation code
	    
		// Define necessary nodes:
		TreeNode<T> root_left = root.left;
		TreeNode<T> root_left_right = root_left.right;
		
		// Rotate right:				// step 1: detach root_left's right subtree, which becomes orphan
		root_left.setRight(root); 		// step 2: make root_left the new root of the tree
		root.setLeft(root_left_right);  // step 3: reattach orphan as old root's left subtree
		
		// Update heights:
		updateHeight(root); 	 		// update old root first
		updateHeight(root_left); 		// update new root second
		
		// return the new root
		return root_left;
	}

	//
	// leftRotate()
	// Perform a left rotation on a tree rooted at "root"
	// The tree's root is assumed to have a right child.
	//
	// RETURNS: the new root after rotation.
	//
	private TreeNode<T> leftRotate(TreeNode<T> root) {
	    // FIXME: fill in the rotation code
	    
		// Define necessary nodes:
		TreeNode<T> root_right = root.right;
		TreeNode<T> root_right_left = root_right.left;
		
		// Rotate left:						// step 1: detach root_right's left subtree, which becomes orphan
		root_right.setLeft(root);			// step 2: make root_right the new root of the tree
		root.setRight(root_right_left); 	// step 3: reattach orphan as old root's right subtree
		
		// Update heights:
		updateHeight(root);		  			// update old root first
		updateHeight(root_right); 			// update new root second
		
		// return the new root
		return root_right;
		
	}
	
	/////////////////////////////////////////////////////////////
	//
	// METHODS USED TO VALIDATE CORRECTNESS OF TREE
	// (You should not have to touch these)
	//

	//
	// getRoot()
	// Return the root node of the tree (for validation only!)
	//
	public TreeNode<T> getRoot() {
	    return this.root;
	}
	
		
	//
	// enumerate()
	// Return the contents of the tree as an ordered list
	//
	public LinkedList<T> enumerate() {
	    return enumerateHelper(this.root);
	}
	
	//
	// enumerateHelper()
	// Enumerate the contents of the tree rooted at "root" in order
	// as a linked list
	//
	private LinkedList<T> enumerateHelper(TreeNode<T> root) {
	    if (root == null) 
		{
		    return new LinkedList<T>();
		}
	    else
		{
		    LinkedList<T> list = enumerateHelper(root.left);
		    list.addLast(root.value);
		    list.addAll(enumerateHelper(root.right));
		    
		    return list;
		}
	}
}
