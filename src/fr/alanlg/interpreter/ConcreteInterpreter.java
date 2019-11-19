package fr.alanlg.interpreter;

import fr.alanlg.ConditionNode;
import fr.alanlg.ParallelNode;
import fr.alanlg.SequenceNode;
import fr.alanlg.Task;
import fr.alanlg.operation.HelloWorld;
import fr.alanlg.operation.Print;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcreteInterpreter implements IInterpreter {

    @Override
    public Object visit(SequenceNode sequenceNode) {
        System.out.println("Begin sequence node");

        Object taskInput = sequenceNode.getInput();
        Object taskOutput = null;
        for (Task task : sequenceNode.getTasks()) {
            if(task.getInput() == null) {
                task.setInput(taskInput);
            }
            taskOutput = task.accept(this);
            taskInput = taskOutput;
        }
        sequenceNode.setOutput(taskOutput);
        System.out.println("End sequence node");
        return taskOutput;
    }

    @Override
    public Object visit(ConditionNode conditionNode) {
        System.out.println("Begin condition node");
        Object input = conditionNode.getInput();

        boolean result = conditionNode.getPredicate().test(input);
        if(result) {
            if(conditionNode.getTaskIfTrue().getInput() == null) {
                conditionNode.getTaskIfTrue().setInput(input);
            }
            conditionNode.getTaskIfTrue().accept(this);
        } else {
            if(conditionNode.getTaskIfFalse().getInput() == null) {
                conditionNode.getTaskIfFalse().setInput(input);
            }
            conditionNode.getTaskIfFalse().accept(this);
        }

        conditionNode.setOutput(result);
        System.out.println("End condition node");
        return result;
    }

    private IInterpreter getInterpreter() {
        return this;
    }

    //TODO gérer l'output
    @Override
    public Object visit(ParallelNode parallelNode) {
        System.out.println("Begin parallel node");
        LinkedList<Object> input = (LinkedList<Object>) parallelNode.getInput();

        // Création d'une pool de thread qui crée des threads selon les besoins
        // la pool réutilise les threads déjà construits s'il y en a de disponibles
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Object>> callableTasks = new ArrayList<>();

        List<Task> taskList = parallelNode.getTasks();
        for (int i = 0; i < taskList.size(); i++) {
            int finalI = i; //pour être utilisé dans la lambda, i doit être final

            Callable<Object> c = () -> {
                // affectation de la valeur d'entrée à la tâche
                taskList.get(finalI).setInput(parallelNode.getParameters().get(finalI));

                return taskList.get(finalI).accept(getInterpreter());
            };
            callableTasks.add(c);
        }

        try {
            // execute toutes les tâches, l'instruction est bloquante
            // puisqu'elle attend que toutes les tâches aient été réalisées
            List<Future<Object>> results = executor.invokeAll(callableTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
        System.out.println("End parallel node");
        return null;
    }

    @Override
    public Object visit(HelloWorld helloWorld) {
        // on applique à la fonction, la donnée d'entrée
        return helloWorld.apply((Integer) helloWorld.getInput());
    }

    @Override
    public Object visit(Print print) {
        return print.apply((String) print.getInput());
    }

}
