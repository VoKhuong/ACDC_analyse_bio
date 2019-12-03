package WorkFlowEngine;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import IHM.MainScene.ANNONCELEVEL;
import javafx.application.Platform;

public class ParallelSplitProxy<T, R> extends ParallelSplit<T, R> {
	
	protected String name;

	public ParallelSplitProxy(String name, @NotNull Node<R> nextNode, @NotNull Function<Object[], R> outputFunction,
			Node<T>[] parallelActivities) {
		super(nextNode, outputFunction, parallelActivities);
		this.name = name;
	}
	
	public ParallelSplitProxy(String name, @NotNull Function<Object[],R> outputFunction, Node<T>... parallelActivities) {
        this(name, new End<>(), outputFunction, parallelActivities);
    }
	
	@Override
    protected Object run(@NotNull Interpreter interpreter) {
		try {
			((InterpreterExtended) interpreter).publish("[" + name + "] Running ParallelSplit", ANNONCELEVEL.LOG);
		} catch(Exception e) {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	((InterpreterExtended) interpreter).publish("[" + name + "] Running ParallelSplit", ANNONCELEVEL.LOG);
			    }
			});
		}
		try {
			return interpreter.interpret(this);
		} catch (InterruptedException e) {
			return null;
		}
	}
}
