package heaps.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Rule;

import heaps.MinHeap;
import heaps.util.HeapToStrings;
import heaps.validate.MinHeapValidator;
import timing.Ticker;

public class TestUtil {
	
	Random r = new Random();
	
	//
	// Fancy way to report problems, so our diagnostic output is included
	//
	@Rule
	public FailReporter tvs = new FailReporter();
	
	static MinHeapValidator<Integer> genHeap(int size) {
		MinHeap<Integer> heap = new MinHeap<Integer>(size, new Ticker());
		return new MinHeapValidator<Integer>(heap);
	}
	
	static Iterable<Integer> genUniqueInts(int num) {
		ArrayList<Integer> ans = new ArrayList<Integer>(num);
		for (int i=0; i < num; ++i) {
			ans.add(i,i);
		}
		Collections.shuffle(ans);
		return ans;
	}

	static Iterable<Integer> genDupInts(int num) {
		ArrayList<Integer> ans = new ArrayList<Integer>(num);
		for (int i=0; i < num; ++i) {
			ans.add(i,i);
			ans.add(i,i);
		}
		Collections.shuffle(ans);
		return ans;
	}
	
	protected void verifySize(String event, MinHeap<?> pq, int expectedSize) {
		assertEquals("Expect queue " + event + " to have size " + expectedSize + " but it did not:  check your size() method", expectedSize, pq.size());
		
	}
	
	protected void verifySizeCapacity(MinHeap<?> pq) {
		assertTrue("A heap's capacity must be at least its size. You have "
				+ " capacity: " + pq.capacity() + " size " + pq.size() + ".  Check your size() and capacity() methods",
				pq.capacity() >= pq.size());
	}

	protected void sortTest(Iterable<Integer> iter, boolean show) {
		MinHeapValidator<Integer> mhv = genHeap(100);
		MinHeap<Integer> pq = mhv.pq;		
		int num = 0;
		assertEquals("Wrong result returned by your size() method:", 0, pq.size());
		for (Integer n : iter) {
			mhv.check();
			pq.insert(n);
			mhv.check();
			++num;
		}
		assertEquals("Wrong result returned by your size() method:", num, pq.size());
		if (show) {
			System.out.println(HeapToStrings.toTree(pq));
		}

		int[] copy = new int[num];
		int size = num;
		for (Integer n : iter) {
			copy[--num] = n;
		}
		Arrays.sort(copy);
		for (int i=0; i < size; ++i) {
			assertEquals("Wrong result returned by your size() method: ", (size-i), pq.size());
			mhv.check();
			int val = pq.extractMin();
			assertEquals("Extraction from queue violated sorted order ", copy[i], val);
			mhv.check();
		}

	}
}
