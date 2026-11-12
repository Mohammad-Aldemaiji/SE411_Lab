package edu.psu.se411.lab12_threads.model;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

/**
 * Lab 11 solution: a money transfer between two synchronized accounts.
 */
public class Transaction implements Runnable {
	private final String id;
	private final CryptoAccount sender;
	private final CryptoAccount receiver;
	private final Double amount;

	public Transaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
		if (id == null || sender == null || receiver == null || amount == null || amount < 0)
			throw new IllegalArgumentException("Invalid transaction");
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (InsufficientBalanceException e) {
			// rejected transaction: no money moves
		}
	}

	public void execute() throws InsufficientBalanceException {
		if (sender == receiver) throw new IllegalArgumentException("Cannot make a transaction where the sender and the receiver are the same account");
		// retrieveFromBalance re-checks the balance while holding the sender's lock
		sender.retrieveFromBalance(amount);
		receiver.addToBalance(amount);
	}

	public String getId() {
		return id;
	}
}
