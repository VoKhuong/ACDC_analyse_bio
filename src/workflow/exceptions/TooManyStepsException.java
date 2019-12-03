package workflow.exceptions;

/**
 * Exception triggered when too many steps passed as parameters
 * @author Vo Lam Nhat Khuong
 * @since 17/11/2019
 * @version 0.1
 */
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
