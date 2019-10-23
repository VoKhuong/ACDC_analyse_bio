package workflow.parameters;

public interface Condition {
	boolean validate(Object...objects );
}
