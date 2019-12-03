package WorkflowExamples;

import java.util.Scanner;

import WorkFlowEngine.Decision;
import WorkFlowEngine.DecisionProxy;
import WorkFlowEngine.Interpreter;
import WorkFlowEngine.Node;
import WorkFlowEngine.ParallelSplit;
import WorkFlowEngine.Sequence;
import WorkFlowEngine.SequenceProxy;
import WorkFlowEngine.Workflow;

public class SamplesExtended implements Workflow {
	
	protected Node<?> node;
	protected Interpreter<?> interpreter;
	
	public static void main(String[] args)
    {
        SamplesExtended samples = new SamplesExtended();
        samples.run();
    }

    /**
     * Read an int and print +0.5 to a positive integer,-0.5 to a negative one.
     */
    public static void runBasicWorkFlow()
    {
        Decision<Integer> firstNode = new DecisionProxy<>("firstNode", (Integer i) -> {return i>=0;});

        Sequence<Integer, Double> trueNode = new SequenceProxy<>("trueNode", (Integer i) -> i+0.5);
        Sequence<Integer, Double> falseNode = new SequenceProxy<>("falseNode", (Integer i) -> i-0.5);

        firstNode.setTrueCaseNode(trueNode);
        firstNode.setFalseCaseNode(falseNode);

        @SuppressWarnings("resource")
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
        Sequence<Object, String> lastNameNode = new SequenceProxy<>("lastNameNode", (Object o)-> "Grosmangin");
        Sequence<Object, String> firstNameNode = new SequenceProxy<>("firstNameNode", (Object o)-> "Quentin");
        Sequence<Object, Integer> age = new SequenceProxy<>("age", (Object o)-> 22);

        @SuppressWarnings("unchecked")
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

	@Override
	public void run() {
		runBasicWorkFlow();
        runParallelWorkFlow();
	}

	@Override
	public Node<?> getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interpreter<?> getInterpreter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nbNodes() {
		return 0;
	}
}
