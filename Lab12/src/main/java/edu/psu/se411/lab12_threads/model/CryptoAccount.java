package edu.psu.se411.lab12_threads.model;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

/**
 * Lab 11 solution: thread safety with "synchronized" (one intrinsic lock per account).
 */
public class CryptoAccount implements Account {
	private final String id;
	private Double balance;

	public CryptoAccount(String id, Double balance) {
		if (balance < 0) throw new IllegalArgumentException("Account balance cannot be negative");
		this.id = id;
		this.balance = balance;
	}

	public synchronized void addToBalance(Double amount) {
		if (amount < 0) throw new IllegalArgumentException("Amount added to balance cannot be negative");
		balance += amount;
	}

	public synchronized void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
		if (amount > balance) throw new InsufficientBalanceException(id);
		balance -= amount;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public synchronized Double getBalance() {
		return balance;
	}
}
