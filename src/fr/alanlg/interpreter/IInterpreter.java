package fr.alanlg.interpreter;

import fr.alanlg.ConditionNode;
import fr.alanlg.ParallelNode;
import fr.alanlg.SequenceNode;
import fr.alanlg.operation.HelloWorld;
import fr.alanlg.operation.Print;

/**
 * Interface pour l'interpreteur.
 * Cet interpreteur va passer dans les noeuds proposés par
 * le workflow, et dans les opérations.
 *
 * @author Alan Le Grand
 */
public interface IInterpreter {

    /**
     * Méthode qui visite le noeud de séquence
     * @param sequenceNode noeud de séquence du workflow
     * @return objet retourné par la séquence
     */
    Object visit(SequenceNode sequenceNode);

    /**
     * Méthode qui visite le noeud de condition
     * @param conditionNode noeud de condition du workflow
     * @return valeur (vrai/faux) de la condition
     */
    Object visit(ConditionNode conditionNode);

    /**
     * Méthode qui visite le noeud de parallélisation
     * @param parallelNode noeud de parallélisation du workflow
     * @return objet retourné par le noeud de parallélisation
     */
    Object visit(ParallelNode parallelNode);

    /**
     * Méthode qui visite l'opération HelloWorld
     * @param helloWorld opération HelloWorld
     * @return objet retourné par l'opération, Hello world concaténé avec l'entier en paramètre
     */
    Object visit(HelloWorld helloWorld);

    /**
     * Méthode qui visite l'opération Print
     * @param print opération Print
     * @return objet retourné par l'opération (valeur d'entrée)
     */
    Object visit(Print print);

}
