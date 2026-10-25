package edu.psu.se411.exceptions;

/**
 * Thrown when a withdrawal amount exceeds the available wallet balance.
 */
public class InsufficientFundsException extends Exception {

    private final double balance;
    private final double requestedAmount;

    public InsufficientFundsException(double balance, double requestedAmount) {
        super(String.format("Insufficient funds: requested %.2f SAR but wallet balance is %.2f SAR (short by %.2f SAR).",
                requestedAmount, balance, requestedAmount - balance));
        this.balance = balance;
        this.requestedAmount = requestedAmount;
    }

    public double getBalance() {
        return balance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getShortfall() {
        return requestedAmount - balance;
    }
}
