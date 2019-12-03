package WorkFlowEngine;

/**
 * Interpreter used to run a workflow. Contains the logic of the workflow engine.
 * @param <I> The type of the initial input data.
 */
public class Interpreter<I> {

    /**
     * Run a workflow by starting executing its first node.
     * @param firstJob The first node to be executed.
     * @param inputData The input data for the first node.
     */
    public Object start(Node<I> firstJob, I inputData) {
        firstJob.setData(inputData);
        return firstJob.run(this);
    }

    protected <T> Object runNextNode(Node<T> nextNode, T input)
    {
        nextNode.setData(input);
        return nextNode.run(this);
    }

    /**
     * Run the current activity, and then the following one.
     * @param sequence The activity to be runed.
     * @param <T> The node input data type
     * @param <R> The node input data type
     * @return The workflow final output
     */
    <T, R> Object interpret(Sequence<T,R> sequence) {
        R output = sequence.getOperation().apply(sequence.getData());

        Node nextNode= sequence.getNextNode();
        return runNextNode(nextNode, output);
    }

    /**
     * Run the decision function of the node, and then run the next activity depending of the result.
     * @param decision The Decision node to be executed.
     * @param <T> The input data type.
     * @return The workflow final output.
     */
     <T> Object interpret(Decision<T> decision) {
        Node nextNode;

        if(decision.getDecisionFunction().test(decision.getData()))
        {
            nextNode = decision.getTrueCaseNode();
        }
        else
        {
            nextNode = decision.getFalseCaseNode();
        }

         return runNextNode(nextNode, decision.getData());
    }

    /**
     * Execute the parallels activities and fork the output data.
     * @param split The ParallelSplit to be run.
     * @param <T> The input data type
     * @param <R> The output data type
     * @return The workflow final output.
     * @throws InterruptedException
     */
    <T,R> Object interpret(ParallelSplit<T, R> split) throws InterruptedException {
        int nbThreads = split.getParallelActivities().size();
        Object[] rawResult = new Object[nbThreads];
        Thread[] threadArray = new Thread[nbThreads];

        // Start every parallel activity, and store the result into 'rawResult' at the end
        for(int i=0; i<nbThreads; i++)
        {
            int currentI = i;
            Thread thread = new Thread(() ->
            {
                Interpreter<Object> interpreter = new Interpreter<>();
                rawResult[currentI] = interpreter.start((Node<Object>) split.getParallelActivities().get(currentI), split.getData());
            });
            thread.start();
            threadArray[i] = thread;
        }

        // Wait for the end of every activity
        for(Thread thread : threadArray)
            thread.join();

        // Refine the result using the output function
        R refinedResult = split.getOutputFunction().apply(rawResult);

        return runNextNode(split.getNextNode(), refinedResult);
    }

    /**
     * Ends the workflow.
     * @param endNode The End node containing the data to be returned
     * @param <T> The output data type
     * @return The workflow output.
     */
    public <T> T interpret(End<T> endNode) {
        return endNode.getData();
    }
}
