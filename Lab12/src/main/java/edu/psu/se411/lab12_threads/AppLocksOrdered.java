package edu.psu.se411.lab12_threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.locks.LockCryptoAccount;
import edu.psu.se411.lab12_threads.locks.OrderedLockTransaction;

/**
 * Part d: same as AppLocks, but locks are always taken in ascending account order,
 * so no deadlock can occur and the total money is preserved.
 *
 * Run: mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.AppLocksOrdered
 */
public class AppLocksOrdered {

	static Logger logger = LoggerFactory.getLogger(AppLocksOrdered.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");
		int nbAccounts = Simulation.intArg(args, 0, 10);
		int nbTransactions = Simulation.intArg(args, 1, 1_000_000);
		int poolSize = Math.max(4, Runtime.getRuntime().availableProcessors());

		ExecutorService executor = Executors.newFixedThreadPool(poolSize, Simulation.daemonThreads());
		List<LockCryptoAccount> accounts = Simulation.createAccounts(nbAccounts,
				i -> new LockCryptoAccount("acc_" + i, i, Simulation.INITIAL_BALANCE));

		Simulation.Result r = Simulation.run(executor, accounts, nbTransactions, OrderedLockTransaction::new, 300);
		Simulation.print(String.format("Locks WITH resource ordering, %d accounts, %d transactions", nbAccounts, nbTransactions), r);

		if (r.completed()) {
			logger.info("Completed without deadlock in {} ms", r.millis());
		} else {
			logger.error("Did not complete: {} deadlocked threads", r.deadlockedThreads());
		}
		logger.info("Application is closing...");
	}
}
