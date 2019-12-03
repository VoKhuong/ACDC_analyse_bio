package implementation;

import ihm.Controller;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class WorkflowImplementation {

    Controller controller;
    Boolean stepLocker;
    AtomicInteger counter;
    AtomicBoolean running = new AtomicBoolean();
    Thread thread;
    int nodeNumber;
    long startTime;

    public WorkflowImplementation(Controller controller) {
        this.controller = controller;
        this.stepLocker = true;
    }


    public abstract void run(TextArea workflow_output, boolean stepByStep);

    public void waitUnlock() {
        while (this.stepLocker) {
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {}
        }
        this.lockStep();
    }

    public void updateProgress(double val) {
        controller.updateProgress(val);
    }

    public void unlockStep() {
        this.stepLocker = false;
    }

    public void lockStep() {
        this.stepLocker = true;
    }

    void waitSecond(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void printOnTextArea(String string) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.printOnTextArea(string);
            }
        });
    }

    public void endWorkflow(boolean stepByStep) {
        this.controller.endWorkflow(stepByStep);
        this.controller.setStatus("Done");
        this.printOnTextArea("");
        this.printOnTextArea("=======================");
        this.printOnTextArea("Execution terminée");
        this.printOnTextArea("Temps d'execution : " + (System.currentTimeMillis()-this.startTime)/ 1000F + " secondes");
    }

    public void startWorkflow(boolean stepByStep) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                printOnTextArea("Début d'execution");
                printOnTextArea("=======================");
                printOnTextArea("");
                controller.setNumberNode(nodeNumber);
                controller.setStatus("Running");
                running.set(true);
                controller.startWorkflow(stepByStep);
                startTime = System.currentTimeMillis();
            }
        });
    }

}
