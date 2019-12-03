package WorkFlowEngine;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import IHM.MainScene.ANNONCELEVEL;
import javafx.application.Platform;

public class SequenceProxy<T, R> extends Sequence<T, R> {
	
	protected String name;

	public SequenceProxy(String name, @NotNull Function<T, R> operation, @NotNull Node<R> nextNode) {
		super(operation, nextNode);
		this.name = name;
	}
	
	public SequenceProxy(String name, @NotNull Function<T,R> operation)
    {
        this(name, operation, new End<R>());
    }
	
	@Override
    protected Object run(@NotNull Interpreter interpreter) {
		try {
			((InterpreterExtended) interpreter).publish("[" + name + "] Running Sequence", ANNONCELEVEL.LOG);
		} catch(Exception e) {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	((InterpreterExtended) interpreter).publish("[" + name + "] Running Sequence", ANNONCELEVEL.LOG);
			    }
			});
		}
		return interpreter.interpret(this);
    }

}
