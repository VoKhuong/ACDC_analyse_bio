package workflow.parameters;

/**
 * Representation of an action used to pass a function as a parameter to a Step constructor
 * @author Vo Lam Nhat Khuong
 * @since 17/11/2019
 *
 */
public interface Action {
	Object[] execute(Object... objects);
}
