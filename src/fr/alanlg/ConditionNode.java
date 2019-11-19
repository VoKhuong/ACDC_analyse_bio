package fr.alanlg;

import fr.alanlg.interpreter.IInterpreter;
import java.util.function.Predicate;

/**
 * Classe permettant de créer un noeud de workflow testant une
 * condition. En fonction de si elle est vrai ou fausse,
 * le workflow continuera vers une tâche ou l'autre.
 *
 * @author Alan Le Grand
 */
public class ConditionNode extends Workflow {

    private Predicate predicate;
    private Task taskIfTrue;
    private Task taskIfFalse;

    public ConditionNode(Predicate predicate, Task taskIfTrue, Task taskIfFalse) {
        this.predicate = predicate;
        this.taskIfTrue = taskIfTrue;
        this.taskIfFalse = taskIfFalse;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Task getTaskIfTrue() {
        return taskIfTrue;
    }

    public Task getTaskIfFalse() {
        return taskIfFalse;
    }

    @Override
    public Object accept(IInterpreter interpreter) {
        return interpreter.visit(this);
    }
}
