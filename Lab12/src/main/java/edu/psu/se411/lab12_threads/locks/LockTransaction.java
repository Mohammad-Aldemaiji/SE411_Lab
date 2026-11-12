package edu.psu.se411.lab12_threads.locks;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

/**
 * Part b: transfer that locks the sender, then the receiver.
 *
 * Part c (what could go wrong): DEADLOCK. If thread T1 runs A -> B while thread T2
 * runs B -> A, T1 holds A's lock and waits for B, T2 holds B's lock and waits for A.
 * Neither can continue, and lock() waits forever, so the thread pool never finishes.
 * This is the classic circular-wait condition.
 */
public class LockTransaction implements Runnable {
	protected final String id;
	protected final LockCryptoAccount sender;
	protected final LockCryptoAccount receiver;
	protected final Double amount;

	public LockTransaction(String id, LockCryptoAccount sender, LockCryptoAccount receiver, Double amount) {
		if (id == null || sender == null || receiver == null || amount == null || amount < 0)
			throw new IllegalArgumentException("Invalid transaction");
		if (sender == receiver)
			throw new IllegalArgumentException("Sender and receiver must be different accounts");
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	@Override
	public void run() {
		LockCryptoAccount first = firstLock();
		LockCryptoAccount second = (first == sender) ? receiver : sender;
		first.getLock().lock();
		try {
			second.getLock().lock();
			try {
				transfer();
			} finally {
				second.getLock().unlock();
			}
		} finally {
			first.getLock().unlock();
		}
	}

	/** Naive order: whoever is the sender is locked first -> can deadlock. */
	protected LockCryptoAccount firstLock() {
		return sender;
	}

	private void transfer() {
		try {
			// both locks are held, so the check and the two updates happen atomically
			sender.retrieveFromBalance(amount);
			receiver.addToBalance(amount);
		} catch (InsufficientBalanceException e) {
			// rejected transaction: no money moves
		}
	}

	public String getId() {
		return id;
	}
}
