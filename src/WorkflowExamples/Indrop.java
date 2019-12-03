package WorkflowExamples;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import IHM.MainScene;
import WorkFlowEngine.End;
import WorkFlowEngine.EndProxy;
import WorkFlowEngine.Interpreter;
import WorkFlowEngine.InterpreterExtended;
import WorkFlowEngine.Node;
import WorkFlowEngine.Sequence;
import WorkFlowEngine.SequenceProxy;
import WorkFlowEngine.Workflow;

public class Indrop<I> implements Workflow<I> {
	
	protected Node<I> node;
	protected Interpreter<I> interpreter;
	
	// https://github.com/biocorecrg/indrop
	public static void main(String[] args) {
		Indrop<String[]> indrop = new Indrop<>();
		indrop.run();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		String[] inputData = new String[4];
		System.out.print("File 1: cell barcode : ");
		inputData[0] = sc.nextLine();
		System.out.print("File 2: cell barcode + UMI : ");
		inputData[1] = sc.nextLine();
		System.out.print("File 3: gene read : ");
		inputData[2] = sc.nextLine();
		System.out.print("File 4: library_tag : ");
		inputData[3] = sc.nextLine();
		
		for(String s: (String[]) this.interpreter.start(this.node, (I) inputData))
			System.out.println(s);
	}
	
	@SuppressWarnings("unchecked")
	public Indrop() {
		this.interpreter = new Interpreter<I>();
		Sequence<String[], String[]> getParams = new SequenceProxy<>("getParams", (str) -> str);
		Sequence<String[], String[]> Run_FastQC_on_raw_reads = new SequenceProxy<>("QC", (str) -> str);
		Sequence<String[], String[]> Indexing = new SequenceProxy<>("Indexing", (str) -> str);
		Sequence<String[], String[]> dropTag = new SequenceProxy<>("dropTag", (str) -> str);
		Sequence<String[], String[]> Alignment = new SequenceProxy<>("Alignment", (str) -> str);
		Sequence<String[], String[]> dropEst = new SequenceProxy<>("dropEst", (str) -> str);
		Sequence<String[], String[]> dropReport = new SequenceProxy<>("dropReport", (str) -> str);
		End<String[]> multiQC = new EndProxy<>("multiQC");
		
		getParams.setNextNode(Run_FastQC_on_raw_reads);
		Run_FastQC_on_raw_reads.setNextNode(Indexing);
		Indexing.setNextNode(dropTag);
		dropTag.setNextNode(Alignment);
		Alignment.setNextNode(dropEst);
		dropEst.setNextNode(dropReport);
		dropReport.setNextNode(multiQC);
		
		this.node = (Node<I>) getParams;
	}
	
	public Indrop(MainScene scene) {
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
		return 8;
	}
}
