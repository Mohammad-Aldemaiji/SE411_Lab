package edu.psu.se411.model;

import edu.psu.se411.exceptions.InsufficientFundsException;

/**
 * An online wallet that lets its owner withdraw money to a bank account.
 */
public class Wallet {

    private final String owner;
    private double balance;

    public Wallet(String owner, double initialBalance) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner name must not be empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.owner = owner;
        this.balance = initialBalance;
    }

    /**
     * Withdraws money from this wallet into the given bank account.
     *
     * @param account the destination bank account
     * @param amount  the amount to withdraw (must be positive)
     * @throws InsufficientFundsException if amount is greater than the wallet balance
     * @throws IllegalArgumentException   if account is null or amount is not positive
     */
    public void withdrawToBank(BankAccount account, double amount) throws InsufficientFundsException {
        if (account == null) {
            throw new IllegalArgumentException("Bank account must not be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(balance, amount);
        }
        balance -= amount;
        account.deposit(amount);
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }
}
