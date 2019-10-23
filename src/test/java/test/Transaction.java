package test;

import java.util.concurrent.CountDownLatch;

import transactionLib.TX;
import transactionLib.TXLibExceptions;

abstract class Transaction implements Runnable {

    String threadName;
    CountDownLatch latch;
    int Iterations;

    Transaction(String name, CountDownLatch l, int iterations) {
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
        int iter = 0;
        while (iter < Iterations) {
            try {
                try {
                    TX.TXbegin();
                    System.out.println(threadName + " finished TX-Begin with readVersion: " + 
         				   TX.lStorage.get().readVersion);
                    
                    execute(iter);
                    
                } 
                
                finally {
                	System.out.println(threadName + " Starting TX-End... ");
                    TX.TXend();
                }
            } 
            catch (TXLibExceptions.AbortException exp) {
            	System.out.println(threadName + " Aborted... ");
                continue;
            }
            
            System.out.println(threadName + " finished succesfully. ");
            iter++;
        }
        
              
   }
    
    public abstract void execute(int iter) throws TXLibExceptions.AbortException;
}