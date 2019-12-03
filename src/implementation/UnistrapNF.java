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

public class UnistrapNF extends WorkflowImplementation {

	public UnistrapNF(Controller controller) {
		super(controller);
	}

	public void run(TextArea workflow_output, boolean stepByStep) {

		nodeNumber = 7;

		Action getShuffleReplicate = objects -> {
			counter = new AtomicInteger();
			waitSecond(1);
			printOnTextArea("Simple step : getShuffleReplicate");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action getMsaReplicate = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Simple step : getMsaReplicate");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action get_seqboot_replicate = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Simple step : get_seqboot_replicate");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action get_replicate_trees = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Simple step : get_replicate_trees");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action get_msa_trees = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Simple step : get_msa_trees");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action get_stable_msa_trees = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Simple step : get_stable_msa_trees");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action get_shootstrap_tree = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("Simple step : get_shootstrap_tree");
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
					Step stepsMsaReplicate = new SimpleStep(condition, get_msa_trees, new SimpleStep(condition, get_stable_msa_trees));
					Step stepsMsaReplicate2 = new SimpleStep(condition, get_seqboot_replicate, new SimpleStep(condition, get_replicate_trees));

					Step parallelStep = new ParallelStep(condition, stepsMsaReplicate, stepsMsaReplicate2, new SimpleStep(condition, get_shootstrap_tree));

					Step step = new SimpleStep(condition, getShuffleReplicate, new SimpleStep(condition, getMsaReplicate, parallelStep));
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
