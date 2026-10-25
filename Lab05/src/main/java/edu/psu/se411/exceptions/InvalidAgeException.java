package edu.psu.se411.exceptions;

/**
 * Thrown when an age does not meet the minimum required age.
 */
public class InvalidAgeException extends Exception {

    private final int age;

    public InvalidAgeException(int age, int minimumAge) {
        super("Invalid age: " + age + ". Age must be " + minimumAge + " or older.");
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
