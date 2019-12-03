package WorkFlowEngine;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * A node that executes other activities in parallel. It uses a set output function to convert the output
 * of each parallel activity into the final expected data type.
 * @param <T> The input data type
 * @param <R> The output datatype
 */
public class ParallelSplit<T, R> extends Node<T>{

    @NotNull private Node<R> nextNode;
    @NotNull private List<Node<T>> parallelActivities;
    @NotNull private Function<Object[], R> outputFunction;

    /**
     * Create a new ParallelSplit.
     * @param nextNode The node be exectued after activities have joined.
     * @param outputFunction The function to convert the output of each parallel activity into the expected end data
     *                       type. The outputs of the activity are passed as a Object[] parameter. The result of each
     *                       activity is stored at the same index than the corresponding activity.
     * @param parallelActivities The workflow activities to be executed.
     */
    public ParallelSplit(@NotNull Node<R> nextNode, @NotNull Function<Object[],R> outputFunction, Node<T>... parallelActivities) {
        super();
        this.nextNode = nextNode;
        this.parallelActivities = Arrays.asList(parallelActivities);
        this.outputFunction = outputFunction;
    }

    /**
     * Create a new ParallelSplit followed by an End node.
     * @param outputFunction The function to convert the output of each parallel activity into the expected end data
     *                       type. The outputs of the activity are passed as a Object[] parameter. The result of each
     *                       activity is stored at the same index than the corresponding activity.
     * @param parallelActivities The workflow activities to be executed.
     */
    public ParallelSplit(@NotNull Function<Object[],R> outputFunction, Node<T>... parallelActivities) {
        this(new End<>(), outputFunction, parallelActivities);
    }


    public @NotNull Node<R> getNextNode() {
        return nextNode;
    }

    public void setNextNode(@NotNull Node<R> nextNode) {
        this.nextNode = nextNode;
    }

    public @NotNull List<Node<T>> getParallelActivities() {
        return parallelActivities;
    }


    /**
     *
     * @return The function to convert the output of each parallel activity into the expected end data
     *         type. The outputs of the activity are passed as a Object[] parameter. The result of each activity is
     *         stored at the same index than the corresponding activity.
     */
    public @NotNull Function<Object[], R> getOutputFunction() {
        return outputFunction;
    }

    /**
     *
     * @param outputFunction The function to convert the output of each parallel activity into the expected end data
     *                       type. The outputs of the activity are passed as a Object[] parameter. The result of each
     *                       activity is stored at the same index than the corresponding activity.
     */
    public void setOutputFunction(@NotNull Function<Object[], R> outputFunction) {
        this.outputFunction = outputFunction;
    }

    @Override
    protected Object run(@NotNull Interpreter interpreter) {
        try {
            return interpreter.interpret(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
