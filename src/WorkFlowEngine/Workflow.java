package WorkFlowEngine;

public interface Workflow<I> {
	void run();
	Node<I> getNode();
	Interpreter<I> getInterpreter();
	int nbNodes();
}
