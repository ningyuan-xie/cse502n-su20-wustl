package avl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import avl.AVLTree;
import org.junit.Rule;
import org.junit.Test;

import avl.util.TreeToStrings;
import avl.validate.BSTValidationError;
import avl.validate.BSTValidator;

/**
 *
 * @author Fernando Rojo, Leah Rivkin, Steve Cole
 *
 */
public class TestFull {

	@Rule
	public FailReporter tvs = new FailReporter();

	private static BSTValidator<Integer> genTree() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		return new BSTValidator<Integer>(tree);
	}

	/***** SMALL TESTS *****/

	@Test
	public void testEmptyTree() {
		AVLTree<Integer> bst = genTree().tree;
		assertTrue("The tree after creation should be empty, but your size is > 0.", bst.size == 0);
	}

	@Test
	public void testTinyInsert() {
		AVLTree<Integer> tree = genTree().tree;
		
		tree.insert(5);
		assertNotNull("Inserted {5} into empty tree but tree root is still null.", tree.getRoot());

		int leafHeight = tree.getRoot().height;
		
		tree.insert(1);
		assertNotNull("Inserted {5, 1} into empty tree but root's left child null.", tree.getRoot().left);
		
		tree.insert(10);
		assertNotNull("Inserted {5, 1, 10} into empty tree but root's right child null.", tree.getRoot().right);
		
		/*
		 * Expected tree:
		 * 			5
		 * 		   / \
		 * 		  1   10
		 */
		assertEquals("Height of node {1} does not match leaf height", leafHeight, tree.getRoot().left.height);
		assertEquals("Height of node {10} does not match leaf height", leafHeight, tree.getRoot().right.height);
		assertEquals("Height of node {5} is not correct based on your leaf height", leafHeight+1, tree.getRoot().height);
	}

	@Test
	public void testTinyInsertRebalance() {
		AVLTree<Integer> tree = new AVLTree<>();
		
		tree.insert(5);
		assertNotNull("Inserted {5} into empty tree but tree root is still null.", tree.getRoot());
		
		int leafHeight = tree.getRoot().height;
		
		tree.insert(4);
		assertNotNull("Inserted {5, 4} into empty tree but root's left child null.", tree.getRoot().left);
		
		tree.insert(3);
		/*
		 * Expected tree:
		 * 			5 						
		 * 		   /
		 * 		  4		-- rotates -->		4
		 * 		 /						   / \
		 * 		3					   	  3	  5
		 */
		assertNotNull("Inserted three values into empty tree but root is null.", tree.getRoot());
		assertNotNull("Inserted three values into empty tree but root's left child is null.", tree.getRoot().left);
		assertNotNull("Inserted three values into empty tree but root's right child is null.", tree.getRoot().right);
		
		assertEquals("Height of node {3} does not match leaf height", leafHeight, tree.getRoot().left.height);
		assertEquals("Height of node {5} does not match leaf height", leafHeight, tree.getRoot().right.height);
		assertEquals("Height of node {4} is not correct based on your leaf height", leafHeight+1, tree.getRoot().height);
	}

	@Test
	public void testSmallInsert() {
		// small insert test, no rebalancing necessary

		BSTValidator<Integer> bst = genTree();
		AVLTree<Integer> tree = bst.tree;

		Integer[] vals = {7, 6, 10, 5, 15};
		Set<Integer> valsInserted = new HashSet<>();

			for(int i=0; i<vals.length; i++) {
			bst.check();
			String before = TreeToStrings.toTree(tree);
			tree.insert(vals[i]);
			valsInserted.add(vals[i]);
			bst.check();
			verifyContents("after inserting "+ vals[i], tree, valsInserted, before);
		}

		/* tree after insertions:
		 * 
		 *   	7
		 *	   /  \
		 *	  6   10
		 *   /      \
		 *  5	    15
		 */
	}
	
	@Test
	public void testSmallInsertRebalance() {
		// tests all 4 rebalance cases 
		
		// left-heavy tree
		BSTValidator<Integer> bstvL = genTree();
		AVLTree<Integer> treeL = bstvL.tree;

		Integer[] left = {15, 10, 5, 7, 6};
		Set<Integer> leftInserted = new HashSet<>();

		/* test left-left, inserts 15, 10, 5
		 *			15
		 *		   /
		 *		  10		-- rotate -->   10
		 *		 /						   /  \
		 *		5						  5	   15
		 */
		for(int i=0; i<3; i++) {
			bstvL.check();
			String before = TreeToStrings.toTree(treeL);
			treeL.insert(left[i]);
			leftInserted.add(left[i]);
			bstvL.check();
			verifyContents("after inserting "+ left[i], treeL, leftInserted, before);
		}

		/* test right-left, inserts 7, 6
		 * 
		 *		  10		         	10			     	10
		 *		 /  \		         	/ \		      	   /  \
		 *		5	 15		--r1-->    5  15   --r2-->	  6   15
		 *		 \			            \			     / \
		 *		  7			             6          	5	7
		 *		 /						  \
		 *		6 						   7
		 */
		for(int i=3; i<5; i++) {
			bstvL.check();
			String before = TreeToStrings.toTree(treeL);
			treeL.insert(left[i]);
			leftInserted.add(left[i]);
			bstvL.check();
			verifyContents("after inserting "+ left[i], treeL, leftInserted, before);
		}

		BSTValidator<Integer> bstvR = genTree();
		AVLTree<Integer> treeR = bstvR.tree;

		Integer[] right = {5, 10, 15, 13, 14};
		Set<Integer> rightInserted = new HashSet<>();

		/* test right-right, inserts 5, 10, 15
		 * 
		 * 		5
		 * 		 \
		 * 		  10	-- rotate -->     10
		 * 			\					 /  \
		 * 			15					5	15
		 */
		for(int i=0; i<3; i++) {
			bstvR.check();
			String before = TreeToStrings.toTree(treeR);
			treeR.insert(right[i]);
			rightInserted.add(right[i]);
			bstvR.check();
			verifyContents("after inserting "+ right[i], treeR, rightInserted, before);
		}

		// test left-right, inserts 13, 14
		/*
		 * 		10					10
		 * 	   /  \					/ \		
		 *    5	   15	--r1->	   5   15	  --r2-->    10
		 *    	  /					   /	   			/  \
		 *    	 13					  14		  	   5 	14
		 *    	  \					 /			 		   /  \
		 *    	  14				13		 			  13   15
		 */
		for(int i=3; i<5; i++) {
			bstvR.check();
			String before = TreeToStrings.toTree(treeR);
			treeR.insert(right[i]);
			rightInserted.add(right[i]);
			bstvR.check();
			verifyContents("after inserting "+ right[i], treeR, rightInserted, before);
		}
	}
	
	@Test
	public void testSmallRemove() {
		// small delete test, no rebalancing necessary

		BSTValidator<Integer> bst = genTree();
		AVLTree<Integer> tree = bst.tree;

		Integer[] vals = {7, 6, 10, 5, 15};
		Set<Integer> valsInserted = new HashSet<>();

			for(int i=0; i<vals.length; i++) {
			bst.check();
			String before = TreeToStrings.toTree(tree);
			tree.insert(vals[i]);
			valsInserted.add(vals[i]);
			bst.check();
			verifyContents("after inserting "+ vals[i], tree, valsInserted, before);
		}

		/* tree after insertions:
		 * 
		 *   	7
		 *	   /  \
		 *	  6   10
		 *   /      \
		 *  5	    15
		 */
			
		// delete elements in order; should exercise
		//  all 3 delete cases with no rebalancing necessary
		for(int i=0; i < vals.length; i++)
		{
			bst.check();
			String before = TreeToStrings.toTree(tree);
			tree.remove(vals[i]);
			valsInserted.remove(vals[i]);
			bst.check();
			verifyContents("after deleting "+ vals[i], tree, valsInserted, before);
		}
	}

	@Test
	public void testSmallRemoveRebalance() {
		// small delete test, rebalancing necessary

		// left-heavy tree
		BSTValidator<Integer> bst = genTree();
		AVLTree<Integer> tree = bst.tree;

		Integer[] vals = {15, 10, 5, 7, 6};
		Set<Integer> valsInserted = new HashSet<>();

			for(int i=0; i<vals.length; i++) {
			bst.check();
			String before = TreeToStrings.toTree(tree);
			tree.insert(vals[i]);
			valsInserted.add(vals[i]);
			bst.check();
			verifyContents("after inserting "+ vals[i], tree, valsInserted, before);
		}

		/* tree after insertions:
		 * 
		 *   	10
		 *	   /  \
		 *	  6   15
		 *   / \     
		 *  5	7    
		 */
			
		// delete elements in reverse order; should exercise
		//  all 3 delete cases with rebalancing
		assert(vals.length >= 2);
		for(int i=1; i >= 0; i--)
		{
			bst.check();
			String before = TreeToStrings.toTree(tree);
			tree.remove(vals[i]);
			valsInserted.remove(vals[i]);
			bst.check();
			verifyContents("after deleting "+ vals[i], tree, valsInserted, before);					
		}
		for(int i=vals.length-1; i > 1; i--)
		{
			bst.check();
			String before = TreeToStrings.toTree(tree);
			tree.remove(vals[i]);
			valsInserted.remove(vals[i]);
			bst.check();
			verifyContents("after deleting "+ vals[i], tree, valsInserted, before);
		}
	}

	@Test
	public void testRemove() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;
		for(int i=0; i<num; i++) {
			verifySize("before Insert", tree, i);
			bstv.check();
			tree.insert(i);
			verifySize("after Insert", tree, i+1);
			bstv.check();
		}
		 Set<Integer> removed = new HashSet<>();
		for (int i=0; i < num/2; ++i) {
			verifySize("before Remove", tree, num - i);
			bstv.check();
			String before = TreeToStrings.toTree(tree);
			tree.remove((i + 7) % num);
			removed.add((i+7)%num);
			List<Integer> missing = new ArrayList<>();
			for(int j=0; j<num; ++j) {
				if(!removed.contains(j)) {
					// this element should still be in the tree
					if(!tree.exists(j)) 
						missing.add(j);
				}
			}
			try {
				if(missing.size()>0) {
					throw new BSTValidationError("\nAfter removing "+ (i+7)%num + " your tree is missing " + Arrays.toString(missing.toArray()));
				}
			} catch (Throwable t) {
				String oops = "\nTree before the problem occurred:\n";
				oops += before + "\n";
				oops += "Tree that triggered this problem:" + "\n";
				oops += TreeToStrings.toTree(tree);
				t.printStackTrace();
				throw new BSTValidationError(t + "" + oops);
			}
			bstv.check();
			verifySize("after Remove", tree, num - i - 1);
		}
	}

	@Test
	public void testSmallInsertAscending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;
		Set<Integer> inserted = new HashSet<>();
		for (int i=0; i < num; ++i) {
			bstv.check();
			String before = TreeToStrings.toTree(tree);
			verifySize("before inserting " + i, tree, i);

			tree.insert(i);
			inserted.add(i);

			bstv.check();
			verifyContents("after inserting "+ i, tree, inserted, before);
			verifySize("after inserting "+ i, tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}


	@Test
	public void testSmallInsertDescending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		Set<Integer> inserted = new HashSet<>();

		int num = 15;
		for (int i= 0; i < num; ++i) {
			String before = TreeToStrings.toTree(tree);
			bstv.check();
			verifySize("before inserting "+ i, tree, i);

			tree.insert(num - i - 1);
			inserted.add(num - i - 1);

			verifyContents("after inserting "+ i, tree, inserted, before);
			bstv.check();
			verifySize("after inserting "+ i, tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}


	@Test
	public void testInsertSmallRandom() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		Set<Integer> inserted = new HashSet<>();

		int num = 15;

		ArrayList<Integer> ints = genUniqueInts(num);
		for (int i= 0; i < num; ++i) {
			bstv.check();
			String before = TreeToStrings.toTree(tree);
			verifySize("before inserting " + ints.get(i), tree, i);

			tree.insert(ints.get(i));
			inserted.add(ints.get(i));

			bstv.check();
			verifyContents("after inserting "+ ints.get(i), tree, inserted, before);
			verifySize("after inserting "+ ints.get(i), tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	@Test
	public void testInsertDuplicates() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 15;

		ArrayList<Integer> ints = genUniqueInts(num);
		Set<Integer> contents = new HashSet<>();
		for (int i= 0; i < num; ++i) {
			bstv.check();
			String before = TreeToStrings.toTree(tree);
			verifySize("before inserting "+ ints.get(i), tree, i);

			tree.insert(ints.get(i));
			contents.add(ints.get(i));

			bstv.check();
			verifyContents("after inserting "+ ints.get(i), tree, contents, before);
			verifySize("after inserting "+ ints.get(i), tree, i+1);

			tree.insert(ints.get(i));

			bstv.check();
			verifyContents("after inserting "+ ints.get(i) + " as a duplicate", tree, contents, before);
			verifySize("after inserting "+ ints.get(i) + " as a duplicate", tree, i+1);
		}
		// uncomment following line to print tree
		// System.out.println(TreeToStrings.toTree(tree));
	}

	/***** END SMALL TESTS *****/
	
	/***** LARGE TESTS *****/
	
	@Test
	public void testLargeInsertAscending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 200000;
		int smallNum = 10;
		Set<Integer> inserted = new HashSet<>();
		
		for(int i=0; i<smallNum; ++i) {
			bstv.check();
			String before = TreeToStrings.toTree(tree);
			verifySize("before inserting " + i, tree, i);
			tree.insert(i);
			inserted.add(i);
			bstv.check();
			verifyContents("after inserting "+ i, tree, inserted, before);
			verifySize("after inserting "+ i, tree, i+1);
		}
		bstv.check();
		verifySize("after inserting "+ smallNum + " nodes", tree, smallNum);

		for (int i=smallNum; i < num; ++i) {
			tree.insert(i);
		}
		bstv.check();
		verifySize("after inserting "+ num + " nodes", tree, num);
	}

	@Test
	public void testLargeInsertDescending() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		Set<Integer> inserted = new HashSet<>();

		int num = 200000;
		int smallNum = 10;
		
		for(int i=0; i<smallNum; ++i) {
			String before = TreeToStrings.toTree(tree);
			bstv.check();
			verifySize("before inserting "+ i, tree, i);
			tree.insert(num - i - 1);
			inserted.add(num - i - 1);
			verifyContents("after inserting "+ i, tree, inserted, before);
			bstv.check();
			verifySize("after inserting "+ i, tree, i+1);
		}

		for (int i=smallNum; i < num; ++i) {
			tree.insert(num - i - 1);
		}
		bstv.check();
		verifySize("after inserting "+ num + " nodes", tree, num);
	}

	@Test
	public void testLargeInsertRandom() {
		BSTValidator<Integer> bstv = genTree();
		AVLTree<Integer> tree = bstv.tree;
		int num = 200000;
		int smallNum = 10;

		ArrayList<Integer> ints = genUniqueInts(num);
		Set<Integer> contents = new HashSet<>();

		for(int i=0; i<smallNum; ++i) {
			bstv.check();
			verifySize("before inserting "+ ints.get(i), tree, i);
			String before = TreeToStrings.toTree(tree);
			tree.insert(ints.get(i));
			contents.add(ints.get(i));
			bstv.check();
			verifyContents("after inserting "+ ints.get(i), tree, contents, before);
			verifySize("after inserting "+ ints.get(i), tree, i+1);
		}

		for (int i=smallNum; i < num; ++i) {
			tree.insert(ints.get(i));
		}
		bstv.check();
		verifySize("after Insert", tree, num);
	}

	/***** END LARGE TESTS *****/

	private <T extends Comparable<T>> void verifyContents(String event, AVLTree<T> tree, Set<T> expected, String before) {
		if(expected.isEmpty()) {
			assertEquals("Your empty tree did not have size 0", 0, tree.size);
			return;
		}
		Set<T> missing = new HashSet<>();
		for(T thing : expected) {
			if(!tree.exists(thing)) {
				missing.add(thing);
			}
		}
		assertTrue("Your tree " + event + " was missing node(s): " + Arrays.toString(missing.toArray())
					+ "\n\nTree before:\n" + before +"\n"+"\nTree after:\n" + TreeToStrings.toTree(tree), 
					missing.isEmpty());
	}

	private void verifySize(String event, AVLTree<?> tree, int expectedSize) {
		assertEquals("Expect tree " + event + " to have size " + 
				expectedSize + " but it did not", expectedSize, tree.size);
	}

	private ArrayList<Integer> genUniqueInts(int num) {
		ArrayList<Integer> ans = new ArrayList<Integer>(num);
		for (int i=0; i < num; ++i) {
			ans.add(i,i);
		}
		Collections.shuffle(ans);
		return ans;
	}

}
