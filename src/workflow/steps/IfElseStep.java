package workflow.steps;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Condition;

public class IfElseStep implements Step {
	
	private Condition condition;
	private Step ifStep;
	private Step elseStep;
	
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

	@Override
	public void addLast(Step... steps) throws TooManyStepsException {
		this.add(steps);
	}

	@Override
	public boolean hasNext() {
		return this.ifStep != null && this.elseStep != null;
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
	
	public String toString() {
		String ifString = new String();
		String elseString = new String();
		if(this.ifStep != null) {
			ifString = "+ IfElseStep IF " + this.ifStep.toString();
		}
		if(this.ifStep != null) {
			elseString = "+ IfElseStep ELSE " + this.elseStep.toString();
		}
		return ifString + '\n' + elseString;
	}
}
