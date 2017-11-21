package parallelsum;

import java.time.Clock;

/**
 * This class allows us to compute the sum of an array using multiple threads.
 * 
 * @author Sathish Gopalakrishnan
 *
 */
public class SumThread extends Thread {
	// we want to compute ans as sum[lo]+sum[lo+1]...+sum[hi-1]
	private int lo;
	private int hi;
	private long ans = 0;
	private int[] arr;

	/**
	 * 
	 * @param a
	 *            array to sum
	 * @param lo
	 *            is the index to start summing from
	 * @param hi
	 *            is the index to stop computing the sum at (a[hi] is not part
	 *            of the sum)
	 */
	SumThread(int[] a, int lo, int hi) {
		this.arr = a;
		this.lo = lo;
		this.hi = hi;
		this.ans = 0;
	}

	/**
	 * Actually compute the sum
	 */
	public void run() {
		for (int idx = lo; idx < hi; idx++) {
			ans += arr[idx];
		}
		// System.out.println(ans);
	}

	/**
	 * Compute the sum of an array
	 * 
	 * @param arr
	 *            array to sum
	 * @return the sum of the elements in arr
	 * @throws InterruptedException
	 */
	public static long sum(int[] arr) throws InterruptedException {
		int len = arr.length;
		long ans = 0;
		int numThreads = 4;
		SumThread[] ts = new SumThread[numThreads];
		for (int i = 0; i < numThreads; i++) {// do parallel computations
			ts[i] = new SumThread(arr, i * len / numThreads, (i + 1) * len / numThreads);
			ts[i].start(); // start not run
		}
		for (int i = 0; i < numThreads; i++) { // combine results
			ts[i].join(); // wait for helper to finish!
			ans += ts[i].ans;
		}
		return ans;
	}

	public static void main(String[] args) {
		Clock timingClock = Clock.systemUTC();
		int len = 1024000;
		long ans = 0;
		int[] arr = new int[len];
		for (int i = 0; i < len; i++)
			arr[i] = i + 1;
		long start = timingClock.millis();
		try {
			ans = sum(arr);
		} catch (InterruptedException e) {
			System.out.println("Hello! We were interrupted.");
		}
		long end = timingClock.millis();
		System.out.println(ans + " computed in " + (end - start) + "ms");
	}

}