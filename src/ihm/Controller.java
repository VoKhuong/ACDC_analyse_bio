package ihm;

import implementation.CallingSNF;
import implementation.ParaMSA;
import implementation.UnistrapNF;
import implementation.WorkflowImplementation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import workflow.Step;
import workflow.exceptions.TooManyStepsException;
import workflow.parameters.Action;
import workflow.parameters.Condition;
import workflow.steps.SimpleStep;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    @FXML
    private AnchorPane workflow_pane;
    @FXML
    private ListView<String> workflow_list;
    @FXML
    private TextArea workflow_output;
    @FXML
    private Button run_workflow;
    @FXML
    private ProgressBar workflow_progress_bar;
    @FXML
    private Button run_workflow_step;
    @FXML
    private Button next_workflow_step;
    @FXML
    private Button stop_workflow_step;
    @FXML
    private Text stat_nb_node;
    @FXML
    private Text stat_status;
    @FXML
    private Text stat_progress;
    @FXML
    private TextField input_input;
    @FXML
    private ChoiceBox<String> input_type_input;
    @FXML
    private ChoiceBox<String> input_type_ouput;
    @FXML
    private TextField input_nb_thread;

    private GridPositionner gridPositionner;
    private String selectedWorkflowName = "CallingSNF";
    private Map<String, WorkflowImplementation> classes = new HashMap<>();
    private WorkflowImplementation runningWorkflow;

    @FXML
    protected void initialize() throws IOException {

        WorkflowImplementation callingSNF = new CallingSNF(this);
        WorkflowImplementation paraMSA = new ParaMSA(this);
        WorkflowImplementation unistrapNF = new UnistrapNF(this);
        this.classes.put("CallingSNF", callingSNF);
        this.classes.put("ParaMSA", paraMSA);
        this.classes.put("UnistrapNF", unistrapNF);

        ObservableList<String> workflowNames = FXCollections.observableArrayList("CallingSNF", "ParaMSA", "UnistrapNF");
        this.workflow_list.setItems(workflowNames);
        this.workflow_list.getSelectionModel().select(workflowNames.get(0));

        ObservableList<String> inputTypes = FXCollections.observableArrayList("VCF", "CSV", "Excel");
        ObservableList<String> outputTypes = FXCollections.observableArrayList("txt", "VCF");

        input_type_input.setItems(inputTypes);
        input_type_input.getSelectionModel().selectFirst();
        input_type_ouput.setItems(outputTypes);
        input_type_ouput.getSelectionModel().selectFirst();

        this.gridPositionner = GridPositionner.getInstance();


    }

    @FXML
    public void createConditionNode() {
        System.out.println("Creation d'un noeud de condition");
    }

    @FXML
    public void createSequenceNode() {
        System.out.println("Creation d'un noeud de séquence");
    }

    @FXML
    public void createParallelNode() {
        System.out.println("Creation d'un noeud de parallelisation");
    }

    @FXML
    public void runSelectedWorkflow() {
        this.eraseTextArea();
        this.printOnTextArea("Workflow " + workflow_list.getSelectionModel().getSelectedItem() + " lancé");

        this.runningWorkflow = this.classes.get(workflow_list.getSelectionModel().getSelectedItem());
        this.runningWorkflow.run(workflow_output, false);
    }

    @FXML
    public void nextWorkflowStep() {
        runningWorkflow.unlockStep();
    }

    public void startWorkflow(boolean stepByStep) {
        this.run_workflow.setDisable(true);
        this.run_workflow_step.setDisable(true);
        this.next_workflow_step.setDisable(!stepByStep);
    }

    public void endWorkflow(boolean stepByStep) {
        this.run_workflow.setDisable(false);
        this.run_workflow_step.setDisable(false);
        this.next_workflow_step.setDisable(true);
    }

    @FXML
    public void runSelectedWorkflowByStep() {
        this.eraseTextArea();
        this.printOnTextArea("Workflow " + workflow_list.getSelectionModel().getSelectedItem() + " lancé en mode step by step");

        this.runningWorkflow = this.classes.get(workflow_list.getSelectionModel().getSelectedItem());
        this.runningWorkflow.run(workflow_output, true);
    }

    public void updateProgress(double val) {
         this.workflow_progress_bar.setProgress(val);
         this.stat_progress.setText((int)(val * 100) + "%");
    }

    public void setNumberNode(int nbNode) {
        this.stat_nb_node.setText(Integer.toString(nbNode));
    }

    public void setStatus(String status) {
        this.stat_status.setText(status);
    }

    public void printOnTextArea(String string) {
        String lastString = workflow_output.getText();
        workflow_output.setText(lastString + "\n" + string);
    }

    public void eraseTextArea() {
        workflow_output.setText("");
    }

    @FXML
    public void createSimpleNode() {
        SimpleNode node = new SimpleNode();
        node.setLayoutX(gridPositionner.getX());
        node.setLayoutY(gridPositionner.getY());
        gridPositionner.addElement(node.getPrefWidth(), node.getPrefHeight());
        workflow_pane.getChildren().add(node);

        Action action = objects -> {
            System.out.println("Action exe");
            return null;
        };

        Condition condition = objects -> true;

        try {
            Step step = new SimpleStep(condition,  action);
            step.activate();
        } catch (TooManyStepsException e) {
            e.printStackTrace();
        }

    }

}
