package WorkFlowEngine;

import IHM.MainScene;
import IHM.MainScene.ANNONCELEVEL;

public class InterpreterExtended<I> extends Interpreter<I> {
	
	protected MainScene mainscene;
	protected int cpt;

	public InterpreterExtended(MainScene scene) {
		this.mainscene = scene;
		this.cpt = 0;
	}
	
	public InterpreterExtended(MainScene scene, int i) {
		this.mainscene = scene;
		this.cpt = i;
	}
	
	public InterpreterExtended(InterpreterExtended<I> interpreter) {
		this(interpreter.mainscene, interpreter.cpt);
	}
	
	public void publish(String str, ANNONCELEVEL level) {
		this.mainscene.addAnnonce(str, level);
		this.mainscene.updateProgressBar(cpt);
		cpt++;
	}
	
	/**
     * Execute the parallels activities and fork the output data.
     * @param split The ParallelSplit to be run.
     * @param <T> The input data type
     * @param <R> The output data type
     * @return The workflow final output.
     * @throws InterruptedException
     */
	@Override
    <T,R> Object interpret(ParallelSplit<T, R> split) throws InterruptedException {
        int nbThreads = split.getParallelActivities().size();
        Object[] rawResult = new Object[nbThreads];
        Thread[] threadArray = new Thread[nbThreads];

        // Start every parallel activity, and store the result into 'rawResult' at the end
        for(int i=0; i<nbThreads; i++)
        {
            int currentI = i;
            @SuppressWarnings("unchecked")
			Thread thread = new Thread(() ->
            {
                Interpreter<I> interpreter = new InterpreterExtended<I>((InterpreterExtended) this);
                rawResult[currentI] = interpreter.start((Node<I>) split.getParallelActivities().get(currentI), (I) split.getData());
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
	
}
