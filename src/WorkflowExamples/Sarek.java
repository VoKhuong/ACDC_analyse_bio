package WorkflowExamples;

import java.util.Scanner;

import IHM.MainScene;
import WorkFlowEngine.End;
import WorkFlowEngine.EndProxy;
import WorkFlowEngine.Interpreter;
import WorkFlowEngine.InterpreterExtended;
import WorkFlowEngine.Node;
import WorkFlowEngine.ParallelSplit;
import WorkFlowEngine.ParallelSplitProxy;
import WorkFlowEngine.Sequence;
import WorkFlowEngine.SequenceProxy;
import WorkFlowEngine.Workflow;

public class Sarek<I> implements Workflow<I> {
	
	protected Node<I> node;
	protected Interpreter<I> interpreter;
	
	// https://github.com/SciLifeLab/Sarek
	public static void main(String[] args) {
		Sarek<String> sarek = new Sarek<>();
		sarek.run();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("InputData : ");
		System.out.println(this.interpreter.start(this.node, (I) sc.nextLine()));
	}
	
	@SuppressWarnings("unchecked")
	public Sarek() {
		this.interpreter = new Interpreter<I>();
		
		Sequence<String, String> Map_reads_to_Reference = new SequenceProxy<>("Map_reads_to_Reference", (str) -> str);
		Sequence<String, String> Mark_Duplicates = new SequenceProxy<>("Mark_Duplicates", (str) -> str);
		Sequence<String, String> Base_Requalibration = new SequenceProxy<>("Base_Requalibration", (str) -> str);
		ParallelSplit<String, String> Preprocessing  = new ParallelSplitProxy<>("Preprocessing ",
				(Object[] result) -> { return (String) result[1]; }, Map_reads_to_Reference, Mark_Duplicates, Base_Requalibration);
		
		Sequence<String, String> GATK_Strelka2 = new SequenceProxy<>("GATK_Strelka2", (str) -> str);
		Sequence<String, String> Manta = new SequenceProxy<>("Manta", (str) -> str);
		ParallelSplit<String, String> germlineVC = new ParallelSplitProxy<>("germlineVC",
				(Object[] result) -> { return (String) result[1]; }, GATK_Strelka2, Manta);
		
		Sequence<String, String> MuTect2_Freebayes_Strelka2 = new SequenceProxy<>("MuTect2_Freebayes_Strelka2", (str) -> str);
		Sequence<String, String> MantaBis = new SequenceProxy<>("MantaBis", (str) -> str);
		Sequence<String, String> ASCAT = new SequenceProxy<>("ASCAT", (str) -> str);
		ParallelSplit<String, String> somaticVC = new ParallelSplitProxy<>("somaticVC",
				(Object[] result) -> { return (String) result[1]; }, MuTect2_Freebayes_Strelka2, MantaBis, ASCAT);
		
		Sequence<String, String> Annotate = new SequenceProxy<>("Annotate", (str) -> str);
		End<String> multiQC = new EndProxy<>("multiQC");
		
		Preprocessing.setNextNode(germlineVC);
		germlineVC.setNextNode(somaticVC);
		somaticVC.setNextNode(Annotate);
		Annotate.setNextNode(multiQC);
		
		this.node = (Node<I>) Preprocessing;
	}
	
	public Sarek(MainScene scene) {
		this();
		this.interpreter = new InterpreterExtended<I>(scene);
	}

	@Override
	public Node<I> getNode() {
		return this.node;
	}

	@Override
	public Interpreter<I> getInterpreter() {
		return this.interpreter;
	}

	@Override
	public int nbNodes() {
		return 13;
	}
}
