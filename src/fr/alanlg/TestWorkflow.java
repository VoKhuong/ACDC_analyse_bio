package fr.alanlg;

import fr.alanlg.interpreter.ConcreteInterpreter;
import fr.alanlg.operation.Print;

import java.util.Arrays;
import java.util.List;

public class TestWorkflow {

    public static void main(String[] args) {

        /*SequenceNode workflow = new SequenceNode(new HelloWorld(), new Print(), new Print());
        workflow.setInput(6);

        ConcreteInterpreter interpreter = new ConcreteInterpreter();
        workflow.accept(interpreter);*/



        /*SequenceNode seq1 = new SequenceNode(new HelloWorld(), new Print());

        Print print = new Print();
        print.setInput("Input override");
        SequenceNode seq2 = new SequenceNode(new HelloWorld(), new Print(), print);

        ConditionNode workflow = new ConditionNode(new GreaterThan(5), seq1, seq2);
        workflow.setInput(5);

        ConcreteInterpreter interpreter = new ConcreteInterpreter();
        workflow.accept(interpreter);*/




        SequenceNode seq1 = new SequenceNode(new Print("1.1"), new Print("1.2"), new Print("1.3"), new Print("1.4"), new Print("1.2"), new Print("1.3"), new Print("1.4"), new Print("1.2"), new Print("1.3"), new Print("1.4"), new Print("1.2"), new Print("1.3"), new Print("1.4"), new Print("1.2"), new Print("1.3"), new Print("1.4"));
        SequenceNode seq2 = new SequenceNode(new Print("2.1"), new Print("2.2"), new Print("2.3"), new Print("2.4"));

        List tasks = Arrays.asList(seq1, seq2);
        List inputs = Arrays.asList("input seq1", "input seq2");

        ParallelNode parallelNode = new ParallelNode(tasks, inputs);

        ConcreteInterpreter interpreter = new ConcreteInterpreter();
        parallelNode.accept(interpreter);

    }

}
