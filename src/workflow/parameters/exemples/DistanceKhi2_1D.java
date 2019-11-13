package workflow.parameters.exemples;

import workflow.parameters.Action;

public class DistanceKhi2_1D implements Action {

	@Override
	public Object[] execute(Object... objects) {
		Object[] result = new Object[1];
		double resultInDouble = 0;
		try {
			double[] estimations = (double[])objects[0];
			double[] observations = (double[])objects[1];
			if(estimations.length != observations.length)
				throw new IllegalArgumentException();
			for(int i = 0; i < estimations.length; i++) {
				resultInDouble += Math.pow(estimations[i] - observations[i], 2) / observations[i];
			}
			result[0] = (Object) resultInDouble;
		} catch (Exception e) {
			result[0] = (Object) "ERROR";
		}
		return result;
	}

}
