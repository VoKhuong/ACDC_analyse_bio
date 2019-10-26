package workflow.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Action;
import workflow.parameters.Condition;
import workflow.steps.SimpleStep;

class WorkflowTests {

	@Test
	void callbackTest() {
		Action action = new Action() {
			public Object[] execute(Object...objects) {
				return objects;
			}
		};
		Condition condition = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		Boolean[] result = new Boolean[1];
		result[0] = true;
		assertArrayEquals(action.execute(true), result);
		assertEquals(condition.validate(), true);
	}
	
	@Test
	void hasNextTest() {
		Action action = new Action() {
			public Object[] execute(Object...objects) {
				return objects;
			}
		};
		Condition condition = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		try {
			Step step = new SimpleStep(condition, action);
			assert(!step.hasNext());
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
	}
	
	@Test
	void stepCreationTest() {
		Action action = new Action() {
			public Object[] execute(Object...objects) {
				return objects;
			}
		};
		Condition condition = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		
		try {
			Step lastStep = new SimpleStep(condition, action);
			assertEquals(lastStep.toString(), "+ SimpleStep ");
			assertEquals(lastStep.length(), 1);
			Step secondStep = new SimpleStep(condition, action, lastStep);
			assertEquals(secondStep.toString(), "+ SimpleStep + SimpleStep ");
			assertEquals(secondStep.length(), 2);
			Step firstStep = new SimpleStep(condition, action, secondStep);
			assertEquals(firstStep.toString(), "+ SimpleStep + SimpleStep + SimpleStep ");
			assertEquals(firstStep.length(), 3);
			firstStep.add(new SimpleStep(condition, action));
			assertEquals(firstStep.toString(), "+ SimpleStep + SimpleStep ");
			assertEquals(firstStep.length(), 2);
			firstStep.addLast(new SimpleStep(condition, action));
			assertEquals(firstStep.toString(), "+ SimpleStep + SimpleStep + SimpleStep ");
			assertEquals(firstStep.length(), 3);
			firstStep.addLast(new SimpleStep(condition, action, new SimpleStep(condition, action)));
			assertEquals(firstStep.toString(), "+ SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep ");
			assertEquals(firstStep.length(), 5);
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
	}
	
	@Test
	void linearWorkflowTest() {
		Action action = new Action() {
			public Object[] execute(Object...objects) {
				int cpt = 0;
				for(Object object: objects) {
					cpt = (int) object;
					cpt++;
				}
				Object[] result = new Object[1];
				result[0] = (Object) cpt;
				return result;
			}
		};
		Condition condition = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		
		try {
			Step steps = new SimpleStep(condition, action, new SimpleStep(condition, action, new SimpleStep(condition, action)));
			Object[] result = steps.activate(0);
			assertEquals((int) result[0], 3);
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
	}
	
	@Test
	void conditionTest() {
		Action action = new Action() {
			public Object[] execute(Object...objects) {
				int cpt = 0;
				for(Object object: objects) {
					cpt = (int) object;
					cpt++;
				}
				Object[] result = new Object[1];
				result[0] = (Object) cpt;
				return result;
			}
		};
		Condition condition = new Condition() {
			public boolean validate(Object...objects) {
				for(Object object: objects) {
					return (int) object > 10;
				}
				return false;
			}
		};
		try {
			Step steps = new SimpleStep(condition, action, new SimpleStep(condition, action, new SimpleStep(condition, action)));
			Object[] resultTrue = steps.activate(11);
			Object[] resultFalse = steps.activate(9);
			assertEquals((int) resultTrue[0], 14);
			assertNull(resultFalse); // The whole object become null
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
		
	}
}
