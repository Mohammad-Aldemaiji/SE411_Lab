package edu.psu.se411.lab12_threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import edu.psu.se411.lab12_threads.model.Account;

/**
 * Shared simulation code reused by every main program in this lab.
 */
public final class Simulation {

	public static final double INITIAL_BALANCE = 10000.0;

	/** Builds the Runnable for one transfer between two accounts. */
	@FunctionalInterface
	public interface TransactionFactory<A extends Account> {
		Runnable create(String id, A sender, A receiver, Double amount);
	}

	/** Result of one run. */
	public record Result(long millis, double initialTotal, double finalTotal, boolean completed, int deadlockedThreads) {
	}

	private Simulation() {
	}

	public static <A extends Account> List<A> createAccounts(int nbAccounts, IntFunction<A> factory) {
		List<A> accounts = new ArrayList<>(nbAccounts);
		for (int i = 0; i < nbAccounts; i++) {
			accounts.add(factory.apply(i));
		}
		return accounts;
	}

	/**
	 * Submits nbTransactions random transfers to the executor and waits for them.
	 * If they are not done after timeoutSeconds, the run is reported as not completed
	 * and the number of deadlocked threads (found by the JVM) is returned.
	 */
	public static <A extends Account> Result run(ExecutorService executor, List<A> accounts, int nbTransactions,
			TransactionFactory<A> factory, long timeoutSeconds) throws InterruptedException {
		int nbAccounts = accounts.size();
		double initialTotal = total(accounts);
		long start = System.currentTimeMillis();

		for (int i = 0; i < nbTransactions; i++) {
			int senderIndex = (int) (Math.random() * nbAccounts);
			int receiverIndex = (int) (Math.random() * nbAccounts);
			if (senderIndex == receiverIndex) {
				receiverIndex = (receiverIndex + 1) % nbAccounts; // make sure they are different
			}
			Double amount = Math.random() * 100;
			executor.execute(factory.create("tx_" + i, accounts.get(senderIndex), accounts.get(receiverIndex), amount));
		}

		executor.shutdown();
		boolean completed = executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
		long millis = System.currentTimeMillis() - start;

		int deadlocked = 0;
		if (!completed) {
			ThreadMXBean mx = ManagementFactory.getThreadMXBean();
			long[] ids = mx.findDeadlockedThreads();
			deadlocked = ids == null ? 0 : ids.length;
			executor.shutdownNow();
			// money is only meaningful once every transfer has finished
			return new Result(millis, initialTotal, Double.NaN, false, deadlocked);
		}
		return new Result(millis, initialTotal, total(accounts), true, 0);
	}

	public static double total(List<? extends Account> accounts) {
		double total = 0;
		for (Account a : accounts) {
			total += a.getBalance();
		}
		return total;
	}

	/** Daemon threads so a deadlocked pool cannot keep the JVM alive forever. */
	public static ThreadFactory daemonThreads() {
		return r -> {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		};
	}

	public static int intArg(String[] args, int index, int defaultValue) {
		return args.length > index ? Integer.parseInt(args[index].replace("_", "")) : defaultValue;
	}

	public static void print(String title, Result r) {
		System.out.printf("%s%n", title);
		System.out.printf("  Initial money volume in system: %.2f%n", r.initialTotal());
		if (r.completed()) {
			System.out.printf("  Final money volume in system:   %.2f%n", r.finalTotal());
			System.out.printf("  Total time: %d ms%n", r.millis());
		} else {
			System.out.printf("  NOT FINISHED after %d ms -> %d deadlocked threads detected by the JVM%n",
					r.millis(), r.deadlockedThreads());
		}
	}
}
