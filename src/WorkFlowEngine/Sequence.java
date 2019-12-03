package WorkFlowEngine;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A simple node that take execute a single function, and pass the result to the next node.
 * @param <T> The input data type.
 * @param <R> The output data type.
 */
public class Sequence<T, R> extends Node<T> {

    @NotNull
    private Function<T,R> operation;
    @NotNull
    private Node<R> nextNode;

    /**
     * Create a new Seuquence node.
     * @param operation The function to be executed. Cannot be null.
     * @param nextNode The next node to be executed. If null, ends the workflow. Cannot be null.
     */
    public Sequence(@NotNull Function<T,R> operation, @NotNull Node<R> nextNode) {
        super();
        setOperation(operation);
        setNextNode(nextNode);
    }

    /**
     * Create a new Seuqence node with an End as next node.
     * @param operation The function to be executed. Cannot be null.
     */
    public Sequence(@NotNull Function<T,R> operation)
    {
        this(operation, new End<R>());
    }

    public @NotNull Function<T,R> getOperation() {
        return operation;
    }

    public void setOperation(@NotNull Function<T,R> operation) {
        this.operation = operation;
    }

    public @NotNull Node<R> getNextNode() {
        return nextNode;
    }

    public void setNextNode(@NotNull Node<R> nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    protected Object run(@NotNull Interpreter interpreter) {
        return interpreter.interpret(this);
    }
}
