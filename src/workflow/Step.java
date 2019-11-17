package workflow;

import workflow.exceptions.TooManyStepsException;

/**
 * high-level basic representation of a workflow
 * @author Vo Lam Nhat Khuong
 * @implNote to be used with a workflow interpreter
 * @version 0.2
 * @since 11/10/2019
 *
 */
public interface Step {
	
	/**
	 * add next steps to the current step
	 * @param steps
	 * @since 11/10/2019
	 */
	public void add(Step... steps) throws TooManyStepsException;
	
	/**
	 * add new steps to the last step
	 * add at the last position of the list if possible
	 * @param steps
	 * @since 11/10/2019
	 */
	public void addLast(Step... steps) throws TooManyStepsException;
	
	/**
	 * verify if the current step is not the last
	 * @return true if there is a next step else false
	 * @since 11/10/2019
	 */
	public boolean hasNext();
	
	/**
	 * execute the operations of the current step
	 * @param objects data
	 * @since 11/10/2019
	 */
	public Object[] executeActions(Object... objects);
	
	/**
	 * activate the step
	 * Look if condition is true and execute actions
	 * @param objects data
	 * @see workflow interpreter
	 * @since 11/10/2019
	 * 
	 */
	public Object[] activate(Object... objects);
	
	/**
	 * verify if the condition is met before running the node
	 * @param objects data
	 * @return true if activation condition is met
	 * @since 11/10/2019
	 */
	public boolean validateCondition(Object... objects);
	
	/**
	 * @return string representation of the current step
	 * @since 11/10/2019
	 */
	public String toString();
	
	
	
	/**
	 * @return int length of the workflow from this block
	 * @since 17/11/2019
	 */
	public int length();
}
