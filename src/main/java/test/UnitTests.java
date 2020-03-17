package test;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitTests {

    @Test
    public void whenThis_thenThat() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(true);
    }

    @Test
    public void whenSomething_thenSomething() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(true);
    }

    @Test
    public void whenSomethingElse_thenSomethingElse() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(false);
        assertTrue(true);
    }

    @Test
    public void whenSomething_thenSomething2() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(true);
    }

    @Test
    public void whensomethingElse_thenSomethingElse() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(true);
    }
}