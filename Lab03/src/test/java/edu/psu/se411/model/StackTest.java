package edu.psu.se411.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StackTest {

    private Stack<String> stringStack;

    @BeforeEach
    public void setUp() {
        stringStack = new Stack<>();
    }

    // Step 2c: push -> push -> pop returns the latest pushed element
    @Test
    public void push_push_pop() {
        stringStack.push("Z");
        stringStack.push("A");
        assertEquals("A", stringStack.pop());
        // Step 2e: to force a failure, temporarily change "A" above to "Z"
    }

    // Step 2f-i: popping an empty stack throws
    @Test
    public void pop_empty_stack() {
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> stringStack.pop(),
                "Expected pop from empty Stack to throw, but it didn't");
        assertTrue(thrown.getMessage().equals("Stack is empty, cannot pop"));
    }

    // Step 2f-ii: elements come out in reverse order of insertion
    @Test
    public void pop_returns_reverse_order() {
        stringStack.push("A");
        stringStack.push("B");
        stringStack.push("C");
        assertEquals("C", stringStack.pop());
        assertEquals("B", stringStack.pop());
        assertEquals("A", stringStack.pop());
    }
}
