package workflow.steps;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Condition;

/**
 * Representation of a condition block :
 * If True then it will go to the ifStep
 * Else False then it will go to the other step
 * @author Vo Lam Nhat Khuong
 * @since 17/11/2019
 * @version 0.3
 *
 */
public class IfElseStep implements Step {
	
	private Condition condition;
	private Step ifStep;
	private Step elseStep;
	
	/**
	 * @param condition Condition to know which Workflow will be runned
	 * @param ifStep Workflow to run if condition is True
	 * @param elseStep Workflow to run if condition is False
	 * @throws TooManyStepsException if more than 2 steps is passed as parameters (in add function)
	 */
	public IfElseStep(Condition condition, Step ifStep, Step elseStep) throws TooManyStepsException {
		this.condition = condition;
		this.add(ifStep, elseStep);
	}

	@Override
	public void add(Step... steps) throws TooManyStepsException {
		if(steps.length == 2) {
			this.ifStep = steps[0];
			this.elseStep = steps[1];
		}
		else
			throw new TooManyStepsException();
	}

	/**
	 * Useless in this case
	 * Cannot addLast() to an IfElseStep
	 * Basically does the same thing as add() function
	 */
	@Override
	public void addLast(Step... steps) throws TooManyStepsException {
		this.add(steps);
	}

	@Override
	public boolean hasNext() {
		return this.ifStep != null || this.elseStep != null;
	}

	@Override
	public Object[] executeActions(Object... objects) {
		if(this.validateCondition(objects))
			return this.ifStep.activate(objects);
		return this.elseStep.activate(objects);
	}

	@Override
	public Object[] activate(Object... objects) {
		return this.executeActions(objects);
	}

	@Override
	public boolean validateCondition(Object... objects) {
		return condition.validate(objects);
	}

	/**
	 * Get the longest length from this block between the two Workflow
	 * @return int length
	 */
	@Override
	public int length() {
		int ifLength = 1;
		int elseLength = 1;
		if(this.ifStep != null)
			ifLength = 1 + this.ifStep.length();
		if(this.elseStep != null)
			elseLength = 1 + this.elseStep.length();
		
		return Math.max(ifLength, elseLength);
	}
	
	@Override
	public String toString() {
		StringBuilder ifString = new StringBuilder();
		StringBuilder elseString = new StringBuilder();
		StringBuilder result = new StringBuilder("+ IFELSESTEP \n");
		if(this.ifStep != null) {
			ifString.append("= IF " + this.ifStep.toString());
		}
		if(this.ifStep != null) {
			elseString.append("= ELSE " + this.ifStep.toString());
		}
		result.append(ifString);
		result.append("\n");
		result.append(elseString);
		return result.toString();
	}
}
