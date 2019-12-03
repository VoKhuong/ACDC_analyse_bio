package WorkFlowEngine;

import org.jetbrains.annotations.NotNull;

/**
 * An activity to be executed as part of a workflow. Nodes implement the composite design pattern : they reference
 * the next one(s) to be executed. A node uses as input data the output of the previous node, it is set by the
 * interpreter. An End node ends the workflow.
 * @param <T> The input data type.
 */
public abstract class Node<T> implements Flowable {

    private T data;

    public Node() {
        this.data = null;
    }

    T getData() {
        return data;
    }

    void setData(T data) {
        this.data = data;
    }

    /**
     * Execute the node using the given workflow Interpret.
     * @param interpreter The Interpreter to be used
     */
    protected abstract Object run(@NotNull Interpreter interpreter);
}
