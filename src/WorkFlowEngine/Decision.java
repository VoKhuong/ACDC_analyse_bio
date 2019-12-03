package WorkFlowEngine;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * A node that determines the next step by executing a decision function that returns a boolean.
 * @param <T> The input data type
 */
public class Decision<T> extends Node<T> {

    @NotNull
    private Predicate<T> decisionFunction;
    @NotNull
    private Node<T> trueCaseNode;
    @NotNull
    private Node<T> falseCaseNode;

    /**
     * Create a new Decision node.
     * @param decisionFunction The boolean function used to determine the next node te be executed.
     * @param trueCaseNode The node to be executed if the decision function returns true. If null, ends the workflow.
     * @param falseCaseNode The node to be executed if the decision function returns false. If null, ends the workflow.
     */
    public Decision(@NotNull Predicate<T> decisionFunction, @NotNull Node<T> trueCaseNode, @NotNull Node<T> falseCaseNode) {
        super();

        setDecisionFunction(decisionFunction);
        setTrueCaseNode(trueCaseNode);
        setFalseCaseNode(falseCaseNode);
    }

    /**
     * Create a new Decision node, with End nodes as next nodes.
     * @param decisionFunction The boolean function used to determine the next node te be executed.
     */
    public Decision(@NotNull Predicate<T> decisionFunction)
    {
        this(decisionFunction, new End<T>(), new End<T>());
    }

    public @NotNull Predicate<T> getDecisionFunction() {
        return decisionFunction;
    }

    public void setDecisionFunction(@NotNull Predicate<T> decisionFunction) {
        this.decisionFunction = decisionFunction;
    }

    public @NotNull Node<T> getTrueCaseNode() {
        return trueCaseNode;
    }

    public void setTrueCaseNode(@NotNull Node<T> trueCaseNode) {
        this.trueCaseNode = trueCaseNode;
    }

    public @NotNull Node<T> getFalseCaseNode() {
        return falseCaseNode;
    }

    public void setFalseCaseNode(@NotNull Node<T> falseCaseNode) {
        this.falseCaseNode = falseCaseNode;
    }

    @Override
    protected Object run(@NotNull Interpreter interpreter) {
        return interpreter.interpret(this);
    }
}
