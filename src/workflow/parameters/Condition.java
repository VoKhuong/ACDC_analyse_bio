package workflow.parameters;

/**
 * Representation of a condition used to pass a function as a parameter to a Step constructor
 * @author Vo Lam Nhat Khuong
 * @since 17/11/2019
 *
 */
public interface Condition {
	boolean validate(Object...objects );
}
