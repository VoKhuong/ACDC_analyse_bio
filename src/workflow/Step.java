package workflow;

import workflow.exceptions.TooManyStepsException;

/**
 * high-level basic representation of a workflow
 * @author Vo Lam Nhat Khuong
 * @implNote to be used with a workflow interpreter
 * @version 0.1
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
	 * add at the last position of the list
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
	 * @since 11/10/2019
	 */
	public void executeActions();
	
	/**
	 * activate the step
	 * @see workflow interpreter
	 * @since 11/10/2019
	 * 
	 */
	public void activate();
	
	/**
	 * verify if the condition is met before running the node
	 * @return true if activation condition is met
	 * @since 11/10/2019
	 */
	public boolean validateCondition();
	
	/**
	 * @return string representation of the current step
	 * @since 11/10/2019
	 */
	public String toString();
}
