package Samples;
import WorkFlowEngine.Decision;
import WorkFlowEngine.Interpreter;
import WorkFlowEngine.ParallelSplit;
import WorkFlowEngine.Sequence;

import java.util.Scanner;

public class Samples {

    public static void main(String... args)
    {
        runBasicWorkFlow();
        runParallelWorkFlow();
    }

    /**
     * Read an int and print +0.5 to a positive integer,-0.5 to a negative one.
     */
    public static void runBasicWorkFlow()
    {
        Decision<Integer> firstNode = new Decision<>((Integer i) -> {return i>=0;});

        Sequence<Integer, Double> trueNode = new Sequence<>((Integer i) -> i+0.5);
        Sequence<Integer, Double> falseNode = new Sequence<>((Integer i) -> i-0.5);

        firstNode.setTrueCaseNode(trueNode);
        firstNode.setFalseCaseNode(falseNode);

        Scanner sc = new Scanner(System.in);

        Interpreter<Integer> interpreter = new Interpreter<Integer>();

        System.out.print("Enter an int : ");
        System.out.println(interpreter.start(firstNode, sc.nextInt()));
    }

    /**
     * Create and print a person.
     */
    public static void runParallelWorkFlow()
    {
        Interpreter<Object> interpreter = new Interpreter<>();
        Sequence<Object, String> lastNameNode = new Sequence<>((Object o)-> "Grosmangin");
        Sequence<Object, String> firstNameNode = new Sequence<>((Object o)-> "Quentin");
        Sequence<Object, Integer> age = new Sequence<>((Object o)-> 22);

        ParallelSplit<Object, Person> parallelSplit = new ParallelSplit<>((Object[] result) -> {
            return new Person((String)result[0], (String)result[1], (Integer)result[2]);
        }, lastNameNode, firstNameNode, age);

        System.out.println(interpreter.start(parallelSplit, null));
    }

    /**
     * Class used to test a ParallelSplit.
     */
    public static class Person
    {
        public String lastName;
        public String firstName;
        public int age;


        public Person(String lastName, String firstName, int age) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "lastName='" + lastName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
