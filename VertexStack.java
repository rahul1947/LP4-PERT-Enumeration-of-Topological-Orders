package rsn170330.lp4;

/**
 * CS 5V81: Implementation of Data Structures and Algorithms
 * Extension of Long Project LP4: PERT, Enumeration of topological orders
 * Team: LP101
 * @author Rahul Nalawade (rsn170330)
 * @author Prateek Sarna (pxs180012)
 * @author Bhavish Khanna Narayanan (bxn170002)
 * 
 */

/**
 * Stack Implementation for Enumeration of paths, avoiding built-in stack to 
 * save from running out of space because of dynamic object creation.
 */
public class VertexStack<T> {
	private int capacity;
	private int top;
	
	private T[] stack;
	
	// Default Constructor
	public VertexStack() {
		capacity = 64; // default size = 64*
		top = -1;
		stack = (T[]) new Object[capacity]; 
		// To avoid "generic array cannot be created" error, declare the array
		// to be Object[] and type-cast where needed to avoid type warnings.
	}
	
	// Constructor for stack size
	public VertexStack(int c) {
		capacity = c;
		top = -1;
		stack = (T[]) new Object[capacity];
	}
	
	/**
	 * Add a new element at top of the stack.
	 * @param item the element to be pushed onto this stack.
	 * @return the pushed element
	 */
	public T push(T item) {
		
		if (top < (capacity - 1)) {
			stack[++top] = item;
			return item;
		}
		return null; // Stack overflow
	}
	
	/**
	 * Removes the element at the top of this stack.
	 * @return removed element 
	 */
	public T pop() {
		
		if (-1 < top) {
			T e = stack[top--];
			return e;
		}
		return null; // Empty stack
	}
	
	/**
	 * Looks at the element at the top of this stack 
	 * without removing it from the stack.
	 * @return top element
	 */
	public T peek() {
		
		if (top == -1) {
			return null;
		}
		return stack[top];
	}
	
	/**
	 * Tests if this stack is empty.
	 * @return true iff this stack contains no items, false otherwise.
	 */
	public boolean empty() {
		return top == -1;
	}
	
	/**
	 * Returns the number of elements in the stack.
	 * @return the number of elements in the stack
	 */
	public int size() {
		return top + 1;
	}
	
	/**
	 * Fills user supplied array with the elements of the stack, in same order
	 * @param a the supplied array reference
	 */
	public void toArray(T[] a) {
		int l = size();
		
		for (int i=0; i<l; i++) {
			a[i] = (T) stack[i];
		}
	}
	
	public static void main(String args[]) {
		
	}
}
