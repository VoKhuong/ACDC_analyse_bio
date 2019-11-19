package fr.alanlg.condition;

import java.util.function.Predicate;

/**
 * Classe permettant de créer une condition "plus grand que".
 * Elle compare son entier (donné au constructeur) à l'entier
 * donné en paramètre à la méthode test.
 *
 * @author Alan Le Grand
 */
public class GreaterThan implements Predicate<Integer> {

    private Integer thisInt;

    public GreaterThan(Integer integer) {
        this.thisInt = integer;
    }

    @Override
    public boolean test(Integer integer) {
        System.out.println("Test if " + integer + " is greater than " + this.thisInt + ". So : " + (integer > this.thisInt));
        return integer > this.thisInt;
    }
}
