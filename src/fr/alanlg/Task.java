package fr.alanlg;

import fr.alanlg.interpreter.IInterpreter;

/**
 * Classe abstraite qui représente toute tâches qui peut être interprêtée.
 * Concrètement une tâche peut être une opération (package operation),
 * ou bien même un élément d'un worflow (noeud de condition, séquence, ...)
 *
 * @author Alan Le Grand
 */
public abstract class Task {

    private Object input = null;
    private Object output = null;

    public Object getInput() {
        return input;
    }

    public Object getOutput() {
        return output;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    /**
     * Permet à l'interpreteur d'acceder à la tâche
     *
     * @param interpreter interpreteur
     * @return valeur de sortie (output) de la tâche
     */
    public abstract Object accept(IInterpreter interpreter);

}
