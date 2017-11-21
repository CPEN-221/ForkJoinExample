package parallelsum;

import java.time.Clock;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSum extends RecursiveTask<Long> {

	static private int SEQUENTIAL_CUTOFF = 128000;
	static private Integer numThreads = 0;
	private int lo;
	private int hi;
	private int[] arr; // arguments

	ForkJoinSum(int[] a, int l, int h) {
		this.arr = a;
		this.hi = h;
		this.lo = l;
		synchronized (numThreads) {
			numThreads++;
		}
	}

	protected Long compute() {// return answer
		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
			Long ans = (long)0;
			for (int i = lo; i < hi; i++)
				ans += arr[i];
			return ans;
		} else {
			ForkJoinSum left = new ForkJoinSum(arr, lo, (hi + lo) / 2);
			ForkJoinSum right = new ForkJoinSum(arr, (hi + lo) / 2, hi);
			left.fork();
			Long rightAns = right.compute();
			Long leftAns = left.join();
			return leftAns + rightAns;
		}
	}

	static final ForkJoinPool fjPool = new ForkJoinPool();

	public static Long sum(int[] arr) {
		return fjPool.invoke(new ForkJoinSum(arr, 0, arr.length));
	}

	public static void main(String[] args) {
		System.out.println("cores available: "+Runtime.getRuntime().availableProcessors());
		Clock timingClock = Clock.systemUTC();
		int len = 1024000;
		long ans = 0;
		int[] arr = new int[len];
		for (int i = 0; i < len; i++)
			arr[i] = i + 1;
		long start = timingClock.millis();
		ans = sum(arr);
		long end = timingClock.millis();
		System.out.println(ans);
		System.out.println("We used " + ForkJoinSum.numThreads + " threads and " + (end - start) + "ms");
	}

}