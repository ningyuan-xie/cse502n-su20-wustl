package heaps.test;

import timing.ExecuteAlgorithm;
import timing.utils.GenSizes;
import timing.utils.IntArrayGenerator;

public class HeapTimer {


	public HeapTimer() {
	}



	public static void main(String[] args) {
		runExperiment(30000);   
	}

	private static void runExperiment(int factor) {
		int start = 5;
		int end   = 50;
		GenSizes sizes = GenSizes.arithmetic(start*factor, end*factor, factor);
		ExecuteAlgorithm.timeAlgorithm(
				"heapsort", 
				"heaps.HeapSort", 
				new IntArrayGenerator(), 
				sizes
				);
	}

}
