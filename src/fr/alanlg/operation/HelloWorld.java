package fr.alanlg.operation;

import java.util.function.Function;

import fr.alanlg.Task;
import fr.alanlg.interpreter.IInterpreter;

/**
 * Tâche permettant de retourner, à partir d'un entier en entrée,
 * "Hello word" concaténé avec l'entier.
 *
 * @author Alan Le Grand
 */
public class HelloWorld extends Task implements Function<Integer, String> {

    @Override
    public String apply(Integer integer) {
        return "Hello world " + integer + " !";
    }

    public Object accept(IInterpreter interpreter) {
        return interpreter.visit(this);
    }
}
