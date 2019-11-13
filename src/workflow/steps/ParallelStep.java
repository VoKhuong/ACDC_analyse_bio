package workflow.steps;

import java.util.Arrays;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Condition;

public class ParallelStep implements Step {

	private Condition condition;
	private Step stepOne;
	private Step stepTwo;
	private Step nextStep;
	
	public ParallelStep(Condition condition, Step stepOne, Step stepTwo, Step... steps) throws TooManyStepsException {
		this.condition = condition;
		this.stepOne = stepOne;
		this.stepTwo = stepTwo;
		this.add(steps);
	}

	@Override
	public void add(Step... steps) throws TooManyStepsException {
		for(Step step: steps) {
			this.nextStep = step;
			if(steps.length > 1) {
				Step[] newSteps = Arrays.copyOfRange(steps, 1, steps.length);
				this.nextStep.add(newSteps);
			}
			break;
		}
	}

	@Override
	public void addLast(Step... steps) throws TooManyStepsException {
		if(this.hasNext())
			this.nextStep.addLast(steps);
		else
			this.add(steps);
	}

	@Override
	public boolean hasNext() {
		return this.nextStep != null;
	}

	@Override
	public Object[] executeActions(Object... objects) {
		Object[] result = new Object[2];
		
		Thread threadOne = new Thread() {
			public void run() {
				try {
					result[0] = stepOne.activate(objects)[0];
				} catch (NullPointerException e) {
					result[0] = null;
				}
			};
		};
		
		Thread threadTwo = new Thread() {
			public void run() {
				try {
					result[1] = stepTwo.activate(objects)[0];
				} catch (NullPointerException e) {
					result[1] = null;
				}
			};
		};
		
		threadOne.start();
		threadTwo.start();
		
		try {
			threadOne.join();
			threadTwo.join();
			return result;
		} catch (InterruptedException e) {
			return null;
		}
	}

	@Override
	public Object[] activate(Object... objects) {
		if(this.validateCondition(objects)) {
			objects = this.executeActions(objects);
			if(this.hasNext())
				return this.nextStep.activate(objects);
			return objects;
		}
		return null;
	}

	@Override
	public boolean validateCondition(Object... objects) {
		return this.condition.validate(objects);
	}

	@Override
	public int length() {
		int ifLength = 1;
		int elseLength = 1;
		if(this.stepOne != null)
			ifLength = 1 + this.stepOne.length();
		if(this.stepTwo != null)
			elseLength = 1 + this.stepTwo.length();
		
		if(this.hasNext())
			return Math.max(ifLength, elseLength) + this.nextStep.length();
		return Math.max(ifLength, elseLength);
	}
	
	@Override
	public String toString() {
		if(this.hasNext())
			return "+ PARALLELSTEP \n= " + this.stepOne.toString() + "\n= " + this.stepTwo.toString() + "\n" + this.nextStep.toString();
		return "+ PARALLELSTEP \n= " + this.stepOne.toString() + "\n= " + this.stepTwo.toString();
	}

}
