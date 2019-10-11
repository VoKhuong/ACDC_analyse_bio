package workflow.steps;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;

public class SimpleStep implements Step {
	
	private Step nextStep;
	
	public SimpleStep(int a, Step... steps) {
		// TODO implements closure function as parameter
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

	public Step getNextStep() {
		return nextStep;
	}

	public void setNextStep(Step nextStep) {
		this.nextStep = nextStep;
	}

}
