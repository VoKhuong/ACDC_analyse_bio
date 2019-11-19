package fr.alanlg;

import fr.alanlg.interpreter.IInterpreter;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Classe permettant de créer un noeud de workflow réalisant
 * une séquence de tâches.
 *
 * @author Alan Le Grand
 */
public class SequenceNode extends Workflow {

    private LinkedList<Task> tasks;

    public SequenceNode(Task... tasks) {
        this.tasks = new LinkedList<>();
        this.tasks.addAll(Arrays.asList(tasks));
    }

    @Override
    public Object accept(IInterpreter interpreter) {
        return interpreter.visit(this);
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }
}
