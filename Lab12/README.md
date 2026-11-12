# Lab 12 – Threads and locks

Starting point: the Lab 11 solution (the double-spending demo).

## How to run

```
mvn clean package exec:java                                                       # fixed pool (App)
mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.AppCachedPool
mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.Benchmark
mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.AppLocks
mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.AppLocksOrdered
```

`App` and `AppCachedPool` take optional arguments: `-Dexec.args="nbAccounts nbTransactions"`, for example `-Dexec.args="1000 1000000"`.

| Class | Lab part |
|---|---|
| `App` | Lab 11 program, fixed thread pool |
| `AppCachedPool` | a. second program using a cached thread pool |
| `Benchmark` | a. runs both pools on both scenarios, averages 3 runs |
| `locks/LockCryptoAccount`, `locks/LockTransaction`, `AppLocks` | b. and c. `synchronized` replaced by `ReentrantLock` (deadlocks) |
| `locks/OrderedLockTransaction`, `AppLocksOrdered` | d. resource ordering (no deadlock) |
| `Simulation` | shared code reused by all the programs |

Note: the starter created `Executors.newFixedThreadPool(nbTransactions)`. That is one thread per transaction, which is 1,000,000 threads for this lab and runs out of memory. The fixed pool is now sized to the number of CPU cores.

## a. Fixed vs cached thread pool (1,000,000 transactions)

Measured on a 2-core machine, averaged over 3 runs:

| Accounts | Fixed pool | Cached pool |
|---|---|---|
| 1000 | 525 ms | 3648 ms (65 threads created) |
| 10 | 378 ms | 3502 ms (71 threads created) |

Your times will be different on your PC. Rerun `Benchmark` to get your own numbers.

**What do you observe?**
- The fixed pool is about 7 to 9 times faster. Each transaction is tiny, so the time goes into managing threads, not into doing the work.
- A fixed pool uses a few threads (one per core) that take tasks from a queue one after another, with no extra cost.
- A cached pool has no queue. Each task is handed directly to an idle thread, and if none is free at that moment, a new thread is created. Here that meant dozens of threads on 2 cores, so the CPU spends its time on handoffs and context switches.
- With 10 accounts, the threads compete for the same 10 locks (high contention). With 1000 accounts, contention is low. Contention causes more blocking, and with a cached pool a blocked thread means more new threads get created. With more cores, the 10-account case gets slower than the 1000-account case.
- Conclusion: for many short CPU-bound tasks, use a bounded fixed pool. A cached pool is meant for many short-lived tasks that mostly wait (for example I/O) and arrive at a moderate rate.

## b. Replacing `synchronized` with locks

`LockCryptoAccount` uses a `ReentrantLock`, and every method does `lock(); try { ... } finally { unlock(); }`. To make the whole transfer atomic (check the balance, debit, credit), `LockTransaction` takes the sender's lock and then the receiver's lock.

## c. What could go wrong?

**Deadlock.** Transaction T1 transfers A → B and transaction T2 transfers B → A at the same time:

- T1 locks A and waits for B.
- T2 locks B and waits for A.

Each one waits for a lock the other holds (a circular wait), and `lock()` waits forever, so the pool never finishes. `AppLocks` shows this: after a 10 s timeout, the JVM's deadlock detector (`ThreadMXBean.findDeadlockedThreads`) reports the blocked threads. Other risks with explicit locks: forgetting `unlock()` (always call it in `finally`), and starvation.

## d. Solution: resource ordering

Accounts get a total order (their numeric id). Every transaction locks the lower account first, whichever direction the money moves. Both A → B and B → A now lock A and then B, so a circular wait cannot form and deadlock is impossible. `AppLocksOrdered` finishes 1,000,000 transactions on 10 accounts in under a second, and the total money in the system stays the same (100000.00 before and after).
