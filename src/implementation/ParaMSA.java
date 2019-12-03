package implementation;

import ihm.Controller;
import javafx.scene.control.TextArea;
import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Action;
import workflow.parameters.Condition;
import workflow.steps.ParallelStep;
import workflow.steps.SimpleStep;

import java.util.concurrent.atomic.AtomicInteger;

public class ParaMSA extends WorkflowImplementation {

	public ParaMSA(Controller controller) {
		super(controller);
	}

	public void run(TextArea workflow_output, boolean stepByStep) {

		nodeNumber = 12;

		Action sequenceFa = objects -> {
			counter = new AtomicInteger();
			waitSecond(1);
			printOnTextArea("Sequences (fa)");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action dafaultAlignements = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Default alignements");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action alternativeAlignements = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Alternative alignements");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action bootstrap100 = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Bootstrap x 100 per default alignement");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action concatenate = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Concatenate");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action phylogeneticTrees = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Phylogenetic trees");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action selectAlignements = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Select alignements");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action bootstrap10 = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Bootstrap x 1, 4, 10, 25 or 100 per default alignement");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action defaultSupportTrees = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Default support trees");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action alternativeAlignementSupportTrees = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Alternative alignement support trees");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action refTree = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Ref tree");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action evaluatePhylogenetic = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Evaluate phylogenetic");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action evaluateSupports = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Evaluate supports");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Condition condition = objects -> true;


		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				startWorkflow(stepByStep);
				try {
					Step parallelAlignementStep = new ParallelStep(
							condition,
							new SimpleStep(condition, concatenate, new SimpleStep(condition, evaluatePhylogenetic)),
							new SimpleStep(condition, selectAlignements, new SimpleStep(condition, bootstrap10, new SimpleStep(condition, alternativeAlignementSupportTrees)))
					);

					Step defaultAlignmentStep = new SimpleStep(condition, dafaultAlignements, new SimpleStep(condition, bootstrap100, new SimpleStep(condition, defaultSupportTrees)));
					Step alternativeAlignmentStep = new SimpleStep(condition, alternativeAlignements, new SimpleStep(condition, selectAlignements, new SimpleStep(condition, bootstrap10, new SimpleStep(condition, alternativeAlignementSupportTrees))));

					Step parallelStep = new ParallelStep(
							condition,
							defaultAlignmentStep,
							alternativeAlignmentStep,
							new SimpleStep(condition, phylogeneticTrees, new SimpleStep(condition, refTree, new SimpleStep(condition, evaluatePhylogenetic, new SimpleStep(condition, evaluateSupports))))
					);

					Step step = new SimpleStep(condition, sequenceFa, parallelStep);
					step.activate();
				} catch (TooManyStepsException e) {
					e.printStackTrace();
				}
				endWorkflow(stepByStep);
			}
		});

		this.thread.start();


	}

}
