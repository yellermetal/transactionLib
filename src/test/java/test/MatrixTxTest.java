package test;

//import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import transactionLib.TXLibExceptions.AbortException;

public class MatrixTxTest {

	private static final int UPDATER_THREADS = 1;
	private static final int READER_THREADS = 10;
	private static final int UPDATER_ITERATIONS = 50;
	private static final int READER_ITERATIONS = 10;
	private static final int DIMENTION = 10;

	@Test
	public void testMatrixMultiThread() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		LL_Matrix matrix = new LL_Matrix(DIMENTION);
		List<Thread> threads = new ArrayList<>();

		for (int thread_id = 0; thread_id < UPDATER_THREADS; thread_id++)
			threads.add(new Thread
					   (new Updater
					   ("Updater" + thread_id, latch, UPDATER_ITERATIONS, matrix)));

		for (int thread_id = 0; thread_id < READER_THREADS; thread_id++)
			threads.add(new Thread
					   (new TxnReader
					   ("TxnReader" + thread_id, latch, READER_ITERATIONS, matrix)));
		
		for (int thread_id = 0; thread_id < READER_THREADS; thread_id++)
			threads.add(new Thread
					   (new SingletonReader
					   ("SingletonReader" + thread_id, latch, READER_ITERATIONS, matrix)));

		for (Thread thread : threads)
			thread.start();
		latch.countDown();
		for (Thread thread : threads)
			thread.join();
	}

	class Updater extends Transaction {
		
		LL_Matrix Matrix;

		Updater(String name, CountDownLatch l, int iterations, LL_Matrix matrix) {
			super(name, l, iterations);
			Matrix = matrix;

		}

		@Override
		public void execute(int iter) throws AbortException {

			for (int i = 0; i < Matrix.dim; i++) {
				//assertEquals(1, Matrix.get(i, i + iter % Matrix.dim));
				Matrix.set(i, (i + iter) % Matrix.dim, 0);
				Matrix.set(i, (i + iter + 1) % Matrix.dim, 1);
			}
		}
	}

	class TxnReader extends Transaction {

		LL_Matrix Matrix;

		TxnReader(String name, CountDownLatch l, int iterations, LL_Matrix matrix) {
			super(name, l, iterations);
			Matrix = matrix;
		}

		@Override
		public void execute(int iter) throws AbortException {
			System.out.println(threadName + " Matrix is 1-DS: " + Matrix.isKDoublyStochastic(1));

		}

	}
	
	class SingletonReader extends Singleton {
		
		LL_Matrix Matrix;

		SingletonReader(String name, CountDownLatch l, int iterations, LL_Matrix matrix) {
			super(name, l, iterations);
			Matrix = matrix;
		}

		@Override
		public void execute(int iter) {
			System.out.println(threadName + " Matrix is 1-DS: " + Matrix.isKDoublyStochastic(1));

			
		}
		
	}

}
