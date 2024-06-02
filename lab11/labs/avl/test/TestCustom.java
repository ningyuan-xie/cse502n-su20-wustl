package avl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import avl.AVLTree;
import avl.TreeNode;

import org.junit.Rule;
import org.junit.Test;

import avl.util.TreeToStrings;
import avl.validate.BSTValidator;

public class TestCustom {

	@Test
	public void yourTest1() {
		// copy of testTinyInsert to give example of 
		// how to write test with given infrastructure

//		AVLTree<Integer> tree = genTree().tree;
//		
//		tree.insert(5);
//		assertNotNull("Inserted {5} into empty tree but tree root is still null.", tree.getRoot());
//
//		int leafHeight = tree.getRoot().height;
//		
//		tree.insert(1);
//		assertNotNull("Inserted {5, 1} into empty tree but root's left child null.", tree.getRoot().left);
//		
//		tree.insert(10);
//		assertNotNull("Inserted {5, 1, 10} into empty tree but root's right child null.", tree.getRoot().right);
//		
//		/*
//		 * Expected tree:
//		 * 			5
//		 * 		   / \
//		 * 		  1   10
//		 */
//		assertEquals("Height of node {1} does not match leaf height", leafHeight, tree.getRoot().left.height);
//		assertEquals("Height of node {10} does not match leaf height", leafHeight, tree.getRoot().right.height);
//		assertEquals("Height of node {5} is not correct based on your leaf height", leafHeight+1, tree.getRoot().height);

	}
	
	@Test
	public void yourTest2() {
//		fail("Not yet implemented");
	}
	
	@Test
	public void yourTest3() {
//		fail("Not yet implemented");
	}

	@Rule
	public FailReporter tvs = new FailReporter();

	private static BSTValidator<Integer> genTree() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		return new BSTValidator<Integer>(tree);
	}
	
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
