package workflow.exceptions;

public class TooManyStepsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -837782420051500126L;
	
	@Override
	public String getMessage() {
		return "Too much steps passed to method";
	}

}
