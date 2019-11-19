# Fork-Join Parallelism
Example: Parallel Array Sum

---

This example primarily deals with using Java's `ForkJoin` framework. There is an alternative approach that works well for some situations and this is to use parallel streams. Here is an example:

```
		List<Integer> iList = Arrays.asList(arr);
		start = timingClock.millis();
		int sum2 = iList.parallelStream().reduce(0, (x, y) -> x + y);
		end = timingClock.millis();
		System.out.println("Parallel streams took us " + (end - start) + "ms");
```
