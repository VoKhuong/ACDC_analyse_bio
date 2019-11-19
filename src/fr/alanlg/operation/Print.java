package fr.alanlg.operation;

import fr.alanlg.Task;
import fr.alanlg.interpreter.IInterpreter;

import java.util.function.Function;

/**
 * Tâche permettant, à partir d'un String, de l'afficher sur la sortie standard.
 * La tâche retourne la string passé en entrée.
 *
 * @author Alan Le Grand
 */
public class Print extends Task implements Function<String, String> {

    public Print() {

    }

    public Print(String s) {
        this.setInput(s);
    }

    @Override
    public String apply(String s) {
        System.out.println(s);
        return s;
    }

    @Override
    public Object accept(IInterpreter interpreter) {
        return interpreter.visit(this);
    }
}
