package WorkFlowEngine;

/**
 * A node that ends the workflow execution
 * @param <T> The output datatype of the Workflow.
 */
public class End<T> extends Node<T> {

    public End() {
        super();
    }

    @Override
    protected Object run(Interpreter interpreter) {
        return interpreter.interpret(this);
    }
}
