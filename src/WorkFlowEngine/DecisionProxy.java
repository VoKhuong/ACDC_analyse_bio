package WorkFlowEngine;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import IHM.MainScene.ANNONCELEVEL;
import javafx.application.Platform;

public class DecisionProxy<T> extends Decision<T> {
	
	protected String name;

	 public DecisionProxy(String name, @NotNull Predicate<T> decisionFunction, @NotNull Node<T> trueCaseNode, @NotNull Node<T> falseCaseNode) {
	        super(decisionFunction, trueCaseNode, falseCaseNode);
	 }
	
	public DecisionProxy(String name, @NotNull Predicate<T> decisionFunction) {
        super(decisionFunction, new End<T>(), new End<T>());
    }
	
	@Override
    protected Object run(@NotNull Interpreter interpreter) {
		try {
			((InterpreterExtended) interpreter).publish("[" + name + "] Running Decision", ANNONCELEVEL.LOG);
		} catch(Exception e) {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	((InterpreterExtended) interpreter).publish("[" + name + "] Running Decision", ANNONCELEVEL.LOG);
			    }
			});
		}
		return interpreter.interpret(this);
    }

	

}
