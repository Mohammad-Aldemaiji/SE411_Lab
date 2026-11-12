package edu.psu.se411.lab12_threads.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;
import edu.psu.se411.lab12_threads.model.Account;

/**
 * Part b: same account, but "synchronized" is replaced by an explicit ReentrantLock.
 * The lock is exposed so a transaction can hold BOTH accounts' locks for the whole
 * transfer (check balance + debit + credit as one atomic step).
 * Because the lock is reentrant, the methods below can still be called by a thread
 * that already holds it.
 */
public class LockCryptoAccount implements Account, Comparable<LockCryptoAccount> {
	private final String id;
	private final int order; // numeric key used for resource ordering (part d)
	private Double balance;
	private final Lock lock = new ReentrantLock();

	public LockCryptoAccount(String id, int order, Double balance) {
		if (balance < 0) throw new IllegalArgumentException("Account balance cannot be negative");
		this.id = id;
		this.order = order;
		this.balance = balance;
	}

	public void addToBalance(Double amount) {
		if (amount < 0) throw new IllegalArgumentException("Amount added to balance cannot be negative");
		lock.lock();
		try {
			balance += amount;
		} finally {
			lock.unlock(); // always release, even if an exception is thrown
		}
	}

	public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
		lock.lock();
		try {
			if (amount > balance) throw new InsufficientBalanceException(id);
			balance -= amount;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Double getBalance() {
		lock.lock();
		try {
			return balance;
		} finally {
			lock.unlock();
		}
	}

	public Lock getLock() {
		return lock;
	}

	@Override
	public String getId() {
		return id;
	}

	public int getOrder() {
		return order;
	}

	/** Total order on accounts, used to always lock in ascending order. */
	@Override
	public int compareTo(LockCryptoAccount other) {
		return Integer.compare(order, other.order);
	}
}
