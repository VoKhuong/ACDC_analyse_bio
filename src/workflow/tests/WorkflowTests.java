package workflow.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Action;
import workflow.parameters.Condition;
import workflow.parameters.exemples.DistanceKhi2_1D;
import workflow.steps.IfElseStep;
import workflow.steps.ParallelStep;
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
	
	@Test
	void IfElseStepTest() {
		Condition condition = new Condition() {
			public boolean validate(Object...objects) {
				return (boolean) objects[0];
			}
		};
		Condition conditionTrue = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		Action ifAction = new Action() {
			public Object[] execute(Object...objects) {
				objects[0] = "IF";
				return objects;
			}
		};
		Action elseAction = new Action() {
			public Object[] execute(Object...objects) {
				objects[0] = "ELSE";
				return objects;
			}
		};
		
		String stringResult = "+ IFELSESTEP \n= IF + SimpleStep \n= ELSE + SimpleStep ";
		// ============================================
		
		try {
			Step ifStep = new SimpleStep(conditionTrue, ifAction);
			Step elseStep = new SimpleStep(conditionTrue, elseAction);
			Step ifElseStep = new IfElseStep(condition, ifStep, elseStep);
			assertEquals("IF", ifElseStep.activate(true)[0]);
			assertEquals("ELSE", ifElseStep.activate(false)[0]);
			assertEquals(stringResult, ifElseStep.toString());
			// System.out.println(ifElseStep.toString());
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
	}
	
	@Test
	void ParallelStepTest() {
		Action actionPlus = new Action() {
			public Object[] execute(Object...objects) {
				int cpt = 0;
				// System.out.println("PLUS");
				for(Object object: objects) {
					cpt = (int) object;
					cpt++;
				}
				Object[] result = new Object[1];
				result[0] = (Object) cpt;
				return result;
			}
		};
		Action actionAddRes = new Action() {
			public Object[] execute(Object...objects) {
				Object[] result = new Object[objects.length + 1];
				for(int i = 0; i < objects.length; i++)
					result[i] = objects[i];
				result[objects.length] = (Object) "NEW";
				return result;
			}
		};
		Action actionMinus = new Action() {
			public Object[] execute(Object...objects) {
				int cpt = 0;
				// System.out.println("MINUS");
				for(Object object: objects) {
					cpt = (int) object;
					cpt--;
				}
				Object[] result = new Object[1];
				result[0] = (Object) cpt;
				return result;
			}
		};
		Condition conditionTrue = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		
		StringBuilder resultString = new StringBuilder("+ PARALLELSTEP \n");
		resultString.append("= + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep \n");
		resultString.append("= + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep + SimpleStep \n");
		resultString.append("+ SimpleStep ");
		
		// ===========================================
		
		try {
			Step stepPlus = new SimpleStep(conditionTrue, actionPlus);
			Step stepMinus = new SimpleStep(conditionTrue, actionMinus);
			Step stepAddRes = new SimpleStep(conditionTrue, actionAddRes);
			Step parallelStep = new ParallelStep(conditionTrue, stepPlus, stepMinus, stepAddRes);
			for(int i = 0; i < 10; i++) {
				stepPlus.addLast(new SimpleStep(conditionTrue, actionPlus));
				stepMinus.addLast(new SimpleStep(conditionTrue, actionMinus));
			}
			Object[] result = parallelStep.activate(0);
			assertEquals(11, (int) result[0]);
			assertEquals(-11, (int) result[1]);
			assertEquals("NEW", result[2]);
			assertEquals(resultString.toString(), parallelStep.toString());
			// System.out.println(parallelStep.toString());
			
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
	}
	
	@Test
	void DistanceKhi2Test() {
		Action khi2 = new DistanceKhi2_1D();
		Condition conditionTrue = new Condition() {
			public boolean validate(Object...objects) {
				return true;
			}
		};
		double[] estimations = {1, 2, 3, 4, 5};
		double[] observations = {1, 2, 3, 4, 5};
		double[] observations2 = {2, 2, 2, 2, 2};
		double[] observations3 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		double result = -1;
		String strErr = new String();
		try {
			Step step = new SimpleStep(conditionTrue, khi2);
			result = (double) step.activate(estimations, observations)[0];
			assertEquals(result, 0);
			result = (double) step.activate(estimations, observations2)[0];
			assertEquals(result, 7.5);
			strErr = (String) step.activate(estimations, observations3)[0];
			assertEquals(strErr, "ERROR");
		} catch (TooManyStepsException e) {
			fail("Too Many Steps Exception");
		}
	}
}
