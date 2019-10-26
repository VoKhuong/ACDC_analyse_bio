package workflow.steps;

import java.util.Arrays;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Action;
import workflow.parameters.Condition;

public class SimpleStep implements Step {
	
	private Step nextStep;
	private Condition condition;
	private Action action;
	
	public SimpleStep(Condition condition, Action action, Step... steps) throws TooManyStepsException {
		this.condition = condition;
		this.action = action;
		this.add(steps);
	}

	@Override
	public void add(Step... steps) throws TooManyStepsException {
		for(Step step: steps) {
			this.setNextStep(step);
			if(steps.length > 1) {
				Step[] newSteps = Arrays.copyOfRange(steps, 1, steps.length);
				this.getNextStep().add(newSteps);
			}
			break;
		}
	}

	@Override
	public void addLast(Step... steps) throws TooManyStepsException {
		if(this.hasNext())
			this.getNextStep().addLast(steps);
		else {
			this.add(steps);
		}
	}

	@Override
	public boolean hasNext() {
		return this.nextStep != null;
	}

	@Override
	public Object[] executeActions(Object... objects) {
		return this.action.execute(objects);
	}

	@Override
	public Object[] activate(Object... objects) {
		if(this.validateCondition(objects)) {
			objects = this.executeActions(objects);
			if(this.hasNext())
				return this.getNextStep().activate(objects);
			return objects;
		}
		return null;
	}

	@Override
	public boolean validateCondition(Object... objects) {
		return this.condition.validate(objects);
	}
	
	@Override
	public String toString() {
		if(this.hasNext())
			return "+ SimpleStep " + this.getNextStep().toString();
		return "+ SimpleStep ";
	}
	
	@Override
	public int length() {
		if(this.hasNext())
			return 1 + this.getNextStep().length();
		return 1;
	}

	private Step getNextStep() {
		return nextStep;
	}

	private void setNextStep(Step nextStep) {
		this.nextStep = nextStep;
	}

}
