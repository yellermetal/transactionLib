package test;

import java.util.Iterator;

import transactionLib.LinkedList;

public class LL_Matrix {
	
	protected LinkedList[] matrix;
	protected int dim;
	
	public LL_Matrix(int dim) {
		
		matrix = new LinkedList[dim];
		for (int i = 0; i < dim; i++) {
			matrix[i] = new LinkedList();
		}
		
		this.dim = dim;
		
		for (int i = 0; i < dim; i++)
			for (int j = 0; j < dim; j++) {
				matrix[i].put(j, 0);
		}
		
		for (int i = 0; i < dim; i++) {
			matrix[i].put(i, 1);
		}
	}
	
	public int get(int row, int col) {
		return (int) matrix[row].get(col);
		
	}
	
	public void set(int row, int col, int val) {
		matrix[row].put(col, val);
	}
	
	public boolean isKDoublyStochastic(int K) {

                    
        int[] row_sums = new int[dim];
        int[] col_sums = new int[dim];
        
        for (int i = 0; i < dim; i++) { 
        	int j = 0;
        	Iterator<Object> it = matrix[i].iterator();
        	while(it.hasNext()) {
        		int val = (int) it.next();
        		row_sums[i] += val;
        		col_sums[j++] += val;
        	}
        }
        
        for (int i = 0; i < dim; i++)
        	if (row_sums[i] != K || col_sums[i] != K) 
        		return false;
                            
        return true;
    } 

}
