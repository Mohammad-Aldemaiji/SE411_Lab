package edu.psu.se411.lab12_threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Part a: runs both pools on both scenarios and prints a comparison table.
 *
 * Run: mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.Benchmark
 */
public class Benchmark {

	static Logger logger = LoggerFactory.getLogger(Benchmark.class);

	private static final int NB_TRANSACTIONS = 1_000_000;
	private static final int RUNS = 3;

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.printf("CPU cores: %d, transactions per run: %,d, runs averaged: %d%n%n", cores, NB_TRANSACTIONS, RUNS);

		// warm-up so JIT compilation does not penalise the first measurement
		measure(false, 100, 100_000);
		measure(true, 100, 100_000);

		System.out.printf("%-10s | %-22s | %-30s%n", "Accounts", "Fixed pool (avg ms)", "Cached pool (avg ms, peak threads)");
		System.out.println("-".repeat(70));
		for (int nbAccounts : new int[] { 1000, 10 }) {
			long fixed = 0, cached = 0;
			int peak = 0;
			for (int i = 0; i < RUNS; i++) {
				fixed += measure(false, nbAccounts, NB_TRANSACTIONS)[0];
				long[] c = measure(true, nbAccounts, NB_TRANSACTIONS);
				cached += c[0];
				peak = Math.max(peak, (int) c[1]);
			}
			System.out.printf("%-10d | %-22d | %d ms, %d threads%n", nbAccounts, fixed / RUNS, cached / RUNS, peak);
			logger.info("{} accounts: fixed={} ms, cached={} ms ({} threads)", nbAccounts, fixed / RUNS, cached / RUNS, peak);
		}
		logger.info("Application is closing...");
	}

	/** @return {elapsed ms, peak number of threads} */
	private static long[] measure(boolean cachedPool, int nbAccounts, int nbTransactions) throws InterruptedException {
		ExecutorService executor = cachedPool ? Executors.newCachedThreadPool()
				: Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<CryptoAccount> accounts = Simulation.createAccounts(nbAccounts,
				i -> new CryptoAccount("acc_" + i, Simulation.INITIAL_BALANCE));
		Simulation.Result r = Simulation.run(executor, accounts, nbTransactions, Transaction::new, 300);
		if (Math.abs(r.finalTotal() - r.initialTotal()) > 1e-3) {
			System.out.println("WARNING: money was created or lost!");
		}
		return new long[] { r.millis(), ((ThreadPoolExecutor) executor).getLargestPoolSize() };
	}
}
