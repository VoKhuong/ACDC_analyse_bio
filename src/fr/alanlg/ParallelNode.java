package fr.alanlg;

import fr.alanlg.interpreter.IInterpreter;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant de créer un noeud de workflow permettant
 * de paralléliser
 *
 * @author Alan Le Grand
 */
public class ParallelNode extends Workflow {

    private List<Task> tasks;
    private List<Object> parameters;

    public ParallelNode(List<Task> tasks, List<Object> parameters) {
        this.tasks = tasks;
        this.parameters = parameters;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public Object accept(IInterpreter interpreter) {
        return interpreter.visit(this);
    }
}
