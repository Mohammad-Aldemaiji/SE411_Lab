package edu.psu.se411.lab12_threads;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Part a: same program, but with a CACHED thread pool.
 *
 * Run: mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.AppCachedPool -Dexec.args="10 1000000"
 */
public class AppCachedPool {

	static Logger logger = LoggerFactory.getLogger(AppCachedPool.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");
		int nbAccounts = Simulation.intArg(args, 0, 10);
		int nbTransactions = Simulation.intArg(args, 1, 1_000_000);

		// a cached pool creates a new thread whenever no idle thread is available
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

		List<CryptoAccount> accounts = Simulation.createAccounts(nbAccounts,
				i -> new CryptoAccount("acc_" + i, Simulation.INITIAL_BALANCE));
		Simulation.Result r = Simulation.run(executor, accounts, nbTransactions, Transaction::new, 300);
		Simulation.print(String.format("Cached thread pool, %d accounts, %d transactions", nbAccounts, nbTransactions), r);
		System.out.printf("  Threads created by the cached pool (peak): %d%n", executor.getLargestPoolSize());

		logger.info("Cached pool: {} accounts, {} transactions, {} ms, {} threads", nbAccounts, nbTransactions,
				r.millis(), executor.getLargestPoolSize());
		logger.info("Application is closing...");
	}
}
