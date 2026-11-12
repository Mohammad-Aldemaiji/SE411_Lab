package edu.psu.se411.lab12_threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Lab 11 solution (double spending) using a FIXED thread pool.
 *
 * Run: mvn clean package exec:java -Dexec.args="nbAccounts nbTransactions"
 * e.g. -Dexec.args="1000 1000000"
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");
		int nbAccounts = Simulation.intArg(args, 0, 10);
		int nbTransactions = Simulation.intArg(args, 1, 1_000_000);

		// A fixed pool must be bounded: one thread per transaction (1,000,000 threads)
		// would exhaust memory. One thread per CPU core is the usual size.
		int poolSize = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);

		List<CryptoAccount> accounts = Simulation.createAccounts(nbAccounts,
				i -> new CryptoAccount("acc_" + i, Simulation.INITIAL_BALANCE));
		Simulation.Result r = Simulation.run(executor, accounts, nbTransactions, Transaction::new, 300);
		Simulation.print(String.format("Fixed thread pool (%d threads), %d accounts, %d transactions", poolSize,
				nbAccounts, nbTransactions), r);

		logger.info("Fixed pool: {} accounts, {} transactions, {} ms", nbAccounts, nbTransactions, r.millis());
		logger.info("Application is closing...");
	}
}
