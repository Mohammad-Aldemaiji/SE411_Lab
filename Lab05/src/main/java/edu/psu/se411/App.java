package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;
import edu.psu.se411.exceptions.InvalidAgeException;
import edu.psu.se411.model.BankAccount;
import edu.psu.se411.model.Wallet;

public class App {

    private static final int MINIMUM_AGE = 18;

    public static void main(String[] args) {
        System.out.println("===== Exercise 1: Custom Exceptions =====");
        int[] ages = {25, 18, 15};
        for (int age : ages) {
            try {
                validateAge(age);
            } catch (InvalidAgeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println();
        System.out.println("===== Exercise 2: Online Wallet =====");
        Wallet wallet = new Wallet("Mohammad", 500.00);
        BankAccount account = new BankAccount("SA-DEMO-0001", 1000.00);
        printBalances(wallet, account);

        double[] withdrawals = {200.00, 400.00, -50.00, 300.00};
        for (double amount : withdrawals) {
            System.out.printf("%nWithdrawing %.2f SAR...%n", amount);
            try {
                wallet.withdrawToBank(account, amount);
                System.out.println("Withdrawal successful.");
            } catch (InsufficientFundsException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                printBalances(wallet, account);
            }
        }
    }

    /**
     * Validates that the given age is at least 18.
     *
     * @throws InvalidAgeException if age is less than 18
     */
    public static void validateAge(int age) throws InvalidAgeException {
        if (age < MINIMUM_AGE) {
            throw new InvalidAgeException(age, MINIMUM_AGE);
        }
        System.out.println("Age valid: " + age);
    }

    private static void printBalances(Wallet wallet, BankAccount account) {
        System.out.printf("Wallet balance: %.2f SAR | Bank balance: %.2f SAR%n",
                wallet.getBalance(), account.getBalance());
    }
}
