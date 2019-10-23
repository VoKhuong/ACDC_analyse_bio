package workflow.steps;

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
		for(Step step: steps) {
			this.add(step);
		}
	}

	@Override
	public void add(Step... steps) throws TooManyStepsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLast(Step... steps) throws TooManyStepsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void executeActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validateCondition() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "";
	}

	private Step getNextStep() {
		return nextStep;
	}

	private void setNextStep(Step nextStep) {
		this.nextStep = nextStep;
	}

}
