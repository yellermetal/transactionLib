package test;

import java.util.concurrent.CountDownLatch;

abstract class Singleton implements Runnable {
	
	String threadName;
    CountDownLatch latch;
    int Iterations;

    Singleton(String name, CountDownLatch l, int iterations) {
        threadName = name;
        Iterations = iterations;
        latch = l;
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException exp) {
            System.out.println(threadName + ": InterruptedException");
        }
        System.out.println(threadName + " started."); 
        for (int iter = 0; iter < Iterations; iter++) 
        	execute(iter);
        System.out.println(threadName + " ended successfully."); 
        
    }
    
    public abstract void execute(int iter);

}
