package edu.psu.se411.lab12_threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.locks.LockCryptoAccount;
import edu.psu.se411.lab12_threads.locks.LockTransaction;

/**
 * Parts b and c: "synchronized" replaced by explicit locks; each transfer locks the
 * sender then the receiver. With many concurrent transfers in both directions this
 * DEADLOCKS. The program detects it with a timeout and the JVM's deadlock detector.
 *
 * Run: mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.AppLocks
 */
public class AppLocks {

	static Logger logger = LoggerFactory.getLogger(AppLocks.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");
		int nbAccounts = Simulation.intArg(args, 0, 10);
		int nbTransactions = Simulation.intArg(args, 1, 1_000_000);
		int poolSize = Math.max(4, Runtime.getRuntime().availableProcessors());

		ExecutorService executor = Executors.newFixedThreadPool(poolSize, Simulation.daemonThreads());
		List<LockCryptoAccount> accounts = Simulation.createAccounts(nbAccounts,
				i -> new LockCryptoAccount("acc_" + i, i, Simulation.INITIAL_BALANCE));

		Simulation.Result r = Simulation.run(executor, accounts, nbTransactions, LockTransaction::new, 10);
		Simulation.print(String.format("Locks WITHOUT ordering, %d accounts, %d transactions", nbAccounts, nbTransactions), r);

		if (!r.completed()) {
			System.out.println("  -> Deadlock: T1 holds A and waits for B while T2 holds B and waits for A.");
			logger.error("Deadlock detected: {} threads blocked forever", r.deadlockedThreads());
		}
		logger.info("Application is closing...");
	}
}
