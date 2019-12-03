package WorkflowExamples;

import java.util.Scanner;

import IHM.MainScene;
import WorkFlowEngine.Decision;
import WorkFlowEngine.DecisionProxy;
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

public class Unistrap<I> implements Workflow<I> {
	
	protected Node<I> node;
	protected Interpreter<I> interpreter;
	
	// https://github.com/cbcrg/unistrap
	public static void main(String[] args) {
		Unistrap<String> unistrap = new Unistrap<>();
		unistrap.run();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		System.out.print("From path : ");
		System.out.println("Result : " + this.interpreter.start(this.node, (I) sc.nextLine()));
	}
	
	@SuppressWarnings("unchecked")
	public Unistrap() {
		this.interpreter = new Interpreter<I>();
		Sequence<String, String> fromPath = new SequenceProxy<>("fromPath", (str) -> str);
		Decision<String> ifEmpty = new DecisionProxy<>("ifEmpty", (str) -> {return str.isEmpty();});
		Sequence<String, String> ifEmptyTrue = new SequenceProxy<>("ifEmptyTrue - file_names", (str) -> "file_names");
		Sequence<String, String> ifEmptyFalse = new SequenceProxy<>("ifEmptyFalse - file_names", (str) -> str);
		Sequence<String, String> get_shuffle_replicates = new SequenceProxy<>("get_shuffle_replicates", (str) -> str);
		Sequence<String, String> shuffle_replicates = new SequenceProxy<>("shuffle_replicates", (str) -> str);
		
		fromPath.setNextNode(ifEmpty);
		ifEmpty.setTrueCaseNode(ifEmptyTrue);
		ifEmpty.setFalseCaseNode(ifEmptyFalse);
		ifEmptyTrue.setNextNode(get_shuffle_replicates);
		ifEmptyFalse.setNextNode(get_shuffle_replicates);
		get_shuffle_replicates.setNextNode(shuffle_replicates);
		
		Sequence<String, String> collectFiles = new SequenceProxy<>("collectFiles", (str) -> str);
		Sequence<String, String> msa_trees = new SequenceProxy<>("msa_trees", (str) -> str);
		collectFiles.setNextNode(msa_trees);
		
		Sequence<String, String> msa_trees2 = new SequenceProxy<>("msa_trees2", (str) -> str);
		Sequence<String, String> first = new SequenceProxy<>("first", (str) -> str);
		msa_trees2.setNextNode(first);
		
		ParallelSplit<String, String> get_msa_trees = new ParallelSplitProxy<>("get_sma_trees",
				(Object[] result) -> { return (String) result[1]; }, collectFiles, msa_trees2);

		Sequence<String, String> stable_trees = new SequenceProxy<>("stable_trees", (str) -> str);
		Sequence<String, String> most_stable_tree = new SequenceProxy<>("most_stable_tree", (str) -> str);
		ParallelSplit<String, String> get_stable_msa_trees = new ParallelSplitProxy<>("get_stable_msa_trees",
				(Object[] result) -> { return (String) result[1]; }, stable_trees, most_stable_tree);
		
		get_msa_trees.setNextNode(get_stable_msa_trees);
		
		Sequence<String, String> get_seqboot_replicates = new SequenceProxy<>("get_seqboot_replicates", (str) -> str);
		Sequence<String, String> replicates = new SequenceProxy<>("replicates", (str) -> str);
		Sequence<String, String> get_replicates_trees = new SequenceProxy<>("get_replicates_trees", (str) -> str);
		Sequence<String, String> collectFilesBis = new SequenceProxy<>("collectFilesBis", (str) -> str);
		get_seqboot_replicates.setNextNode(replicates);
		replicates.setNextNode(get_replicates_trees);
		get_replicates_trees.setNextNode(collectFilesBis);
		
		End<String> get_shootstrap_tree = new EndProxy<>("get_shootstrap_tree");
		ParallelSplit<String, String> get_msa_replicates = new ParallelSplitProxy<>("get_msa_replicates",
				(Object[] result) -> { return (String) result[1]; }, get_msa_trees, get_seqboot_replicates);
		get_msa_replicates.setNextNode(get_shootstrap_tree);
		
		shuffle_replicates.setNextNode(get_msa_replicates);
		this.node = (Node<I>) fromPath;
	}
	
	public Unistrap(MainScene scene) {
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
		return 20;
	}
}
