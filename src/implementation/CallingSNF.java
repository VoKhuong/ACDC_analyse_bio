package implementation;

import ihm.Controller;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Action;
import workflow.parameters.Condition;
import workflow.steps.ParallelStep;
import workflow.steps.SimpleStep;

import java.util.concurrent.atomic.AtomicInteger;

public class CallingSNF extends WorkflowImplementation {

	public CallingSNF(Controller controller) {
		super(controller);
	}

	public void run(TextArea workflow_output, boolean stepByStep) {

		nodeNumber = 11;

		Condition condition = objects -> true;

		Action step1A = objects -> {
			counter = new AtomicInteger();
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("1A : prepare samtools genome index");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};
		Action step1B = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("1B : prepare picard genome index");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};
		Action step1C = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("1C : prepare STAR genome index");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};
		Action step1D = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("1D : prepare variant calling file");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action step2 = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("2 : align reads to genome with STAR");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action step3 = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("3 : GATK splitNcigar");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action step4 = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("4 : GATK recalibrate");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action step5 = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("5 : GATK variant calling");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		Action step6A = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("6A : post process VCF file");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};
		Action step6B = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("6B : prepare VCF for allele specific exp.");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};
		Action step6C = objects -> {
			if(stepByStep) waitUnlock();
			waitSecond(1);
			printOnTextArea("6C : GATK allele specific expression");
			counter.getAndIncrement();
			updateProgress(counter.doubleValue()/nodeNumber);
			return null;
		};

		this.thread = new Thread(new Task<Void>() {
			@Override
			public Void call() {
				startWorkflow(stepByStep);

				try {
					Step parallelOne = new ParallelStep(condition, new SimpleStep(condition, step1A, new SimpleStep(condition, step2)), new SimpleStep(condition, step1B));
					Step parallelTwo = new ParallelStep(condition, new SimpleStep(condition, step1C), new SimpleStep(condition, step1D));

					Step parallelGeneral = new ParallelStep(condition, parallelOne, parallelTwo, new SimpleStep(condition, step3, new SimpleStep(condition, step4, new SimpleStep(condition, step5, new SimpleStep(condition, step6A, new SimpleStep(condition, step6B, new SimpleStep(condition, step6C)))))));
					parallelGeneral.activate();

				} catch (TooManyStepsException e) {
					e.printStackTrace();
				}

				endWorkflow(stepByStep);
				return null;
			}
		});

		this.thread.start();

	}

}
