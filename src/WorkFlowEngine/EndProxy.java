package WorkFlowEngine;

import IHM.MainScene.ANNONCELEVEL;
import javafx.application.Platform;

public class EndProxy<T> extends End<T> {
	
	protected String name;
	
	public EndProxy(String name) {
		super();
		this.name = name;
	}
	
	@Override
    protected Object run(Interpreter interpreter) {
		try {
			((InterpreterExtended) interpreter).publish("[" + name + "] Running End", ANNONCELEVEL.LOG);
		} catch(Exception e) {
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	((InterpreterExtended) interpreter).publish("[" + name + "] Running End", ANNONCELEVEL.LOG);
			    }
			});
		}
		return interpreter.interpret(this);
    }

}
