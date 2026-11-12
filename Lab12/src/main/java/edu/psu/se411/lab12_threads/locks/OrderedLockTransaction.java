package edu.psu.se411.lab12_threads.locks;

/**
 * Part d: resource ordering. Accounts have a total order (their numeric id), and
 * every transaction locks the LOWER account first, whatever the transfer direction.
 * A -> B and B -> A now both lock A then B, so a circular wait is impossible and
 * deadlock cannot happen.
 */
public class OrderedLockTransaction extends LockTransaction {

	public OrderedLockTransaction(String id, LockCryptoAccount sender, LockCryptoAccount receiver, Double amount) {
		super(id, sender, receiver, amount);
	}

	@Override
	protected LockCryptoAccount firstLock() {
		return sender.compareTo(receiver) < 0 ? sender : receiver;
	}
}
