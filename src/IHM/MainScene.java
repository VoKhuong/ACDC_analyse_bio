package IHM;

import IHM.MainScene.ANNONCELEVEL;
import WorkFlowEngine.InterpreterExtended;
import WorkFlowEngine.Workflow;
import WorkflowExamples.Indrop;
import WorkflowExamples.Sarek;
import WorkflowExamples.Unistrap;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainScene extends Scene {
	
	protected ObservableList<Class<?>> workflows;
	protected double size;
	
	public MainScene() {
		super(new BorderPane());
		this.size = 1;
		BorderPane root = (BorderPane)(this.getRoot());
		root.setBottom(constructBottom());
		
		root.setCenter(constructCenter());
		
		root.setLeft(constructLeft());
		
		
	}

	public enum ANNONCELEVEL {
		LOG,
		ERROR,
		INFO,
	};
	
	public void addAnnonce(String str, ANNONCELEVEL level) {
		TextFlow textflow = (TextFlow) ((ScrollPane) ((BorderPane)(this.getRoot())).getBottom()).getContent();
		Text text = new Text();
		if(level == ANNONCELEVEL.ERROR) {
			text.setFill(Color.RED);
			text.setText(String.format("%-15s\t", "[ERROR]") + str + '\n');
		}
		else if(level == ANNONCELEVEL.LOG) {
			text.setFill(Color.WHITE);
			text.setText(String.format("%-15s\t", "[LOG]") + str + '\n');
		}
		else {
			text.setFill(Color.BLUE);
			text.setText(String.format("%-15s\t", "[INFO]") + str + '\n');
		}
		textflow.getChildren().add(text);
	}
	
	public void updateProgressBar(double d) {
		((ProgressBar) this.getRoot().lookup("#progressBar")).setProgress(d / this.size);
	}
	
	private javafx.scene.Node constructCenter() {
		VBox center = new VBox();
		this.workflows = FXCollections.observableArrayList();
		this.updateWorkflows();
		ChoiceBox<Class<?>> workflowChooser = new ChoiceBox<Class<?>>(this.workflows);
		workflowChooser.minWidth(500);
		workflowChooser.prefWidth(500);
		center.getChildren().add(workflowChooser);
		workflowChooser.setId("chooser");
		workflowChooser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @SuppressWarnings("deprecation")
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  ((Text) getRoot().lookup("#descStatus")).setText("Status :\t\tNOT STARTED");
		    	  updateProgressBar(0);
		    	  try {
			    	  Class<?> workflowCass = workflowChooser.getItems().get((Integer) number2);
			    	  ((Text) getRoot().lookup("#descName")).setText("Name :\t\t" + workflowCass.getName());
					((Text) getRoot().lookup("#descNodes")).setText("Nodes :\t\t" + ((Workflow) workflowCass.newInstance()).nbNodes());
				} catch (Exception e) {
					((Text) getRoot().lookup("#descName")).setText("Name :\t\t");
					((Text) getRoot().lookup("#descNodes")).setText("Nodes :\t\t");
				}
		      }
		});
		Button btnUpdateWorkflows = new Button("Update list");
		btnUpdateWorkflows.setOnAction(new EventHandler<ActionEvent>() {
			 
	            @Override
	            public void handle(ActionEvent event) {
	                updateWorkflows();
	                addAnnonce("The list of workflow have been updated.", ANNONCELEVEL.INFO);
	            }
	        });
		Button btnRunWorkflows = new Button("Run workflow");
		btnRunWorkflows.setOnAction(new EventHandler<ActionEvent>() {
			 
	            @Override
	            public void handle(ActionEvent event) {
	                addAnnonce("Running workflow", ANNONCELEVEL.INFO);
	                runWorkflow();
	            }
	        });
		center.setAlignment(Pos.CENTER);
		HBox btns = new HBox();
		btns.getChildren().add(btnUpdateWorkflows);
		btns.getChildren().add(btnRunWorkflows);
		btns.setAlignment(Pos.CENTER);
		btns.setPadding(new Insets(10));
		btns.setSpacing(10);
		center.getChildren().add(btns);
		
		HBox boxInput = new HBox();
		boxInput.setAlignment(Pos.CENTER);
		boxInput.setPadding(new Insets(10));
		boxInput.setSpacing(10);
		ObservableList<String> listType = FXCollections.observableArrayList();
		listType.add("INT");
		listType.add("DOUBLE");
		listType.add("STRING");
		listType.add("STRING []");
		ChoiceBox<String> typeChooser = new ChoiceBox<String>(listType);
		center.getChildren().add(typeChooser);
		TextField input = new TextField();
		input.setPromptText("Input Data");
		input.setId("inputData");
		boxInput.getChildren().add(typeChooser);
		boxInput.getChildren().add(input);
		boxInput.setId("Inputchooser");
		center.getChildren().add(boxInput);
		ProgressBar progressBar = new ProgressBar(0);
		progressBar.setPrefWidth(300);
		progressBar.setPrefHeight(50);
		progressBar.setPadding(new Insets(10));
        progressBar.setId("progressBar");
        center.getChildren().add(progressBar);
		center.setPadding(new Insets(20));
		return center;
	}
	
	private javafx.scene.Node constructBottom() {
		TextFlow textFlow = new TextFlow();
		ScrollPane textFlowWrapper = new ScrollPane(textFlow);
		textFlowWrapper.setPrefHeight(250);
		textFlowWrapper.setPrefWidth(1000);
		textFlow.setStyle("-fx-background-color: black;");
		textFlowWrapper.setStyle("-fx-background: black;");
		return textFlowWrapper;
	}
	
	private Node constructLeft() {
		VBox left = new VBox();
		left.setPadding(new Insets(20));
		Text descName = new Text("Name :\t\t");
		descName.setId("descName");
		// Text descInput = new Text("Input type :\t");
		// descInput.setId("descInput");
		Text descNodes = new Text("Nodes :\t\t");
		descNodes.setId("descNodes");
		Text descStatus = new Text("Status :\t\t");
		descStatus.setId("descStatus");
		descName.setFont(new Font(16));
		left.getChildren().add(descName);
		// descInput.setFont(new Font(16));
		// left.getChildren().add(descInput);
		descNodes.setFont(new Font(16));
		left.getChildren().add(descNodes);
		descStatus.setFont(new Font(16));
		left.getChildren().add(descStatus);
		return left;
	}
	
	protected void runWorkflow() {
		((Text) getRoot().lookup("#descStatus")).setText("Input type :\t\t RUNNING");
		this.updateProgressBar(0);
		@SuppressWarnings("unchecked")
		ChoiceBox<Class<?>> workflowChooser = (ChoiceBox<Class<?>>) ((VBox) ((BorderPane)(this.getRoot())).getCenter()).getChildren().get(0);
		Class<?> workflowClass = workflowChooser.getSelectionModel().getSelectedItem();
		try {
			Workflow workflow = (Workflow) workflowClass.getConstructor(new Class[]{MainScene.class}).newInstance(this);
			this.size = workflow.nbNodes();
			Object result = workflow.getInterpreter().start(workflow.getNode(), getInput());
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	addAnnonce("Output : " + result, ANNONCELEVEL.INFO);
			    }
			});
		} catch (Exception e) {
			addAnnonce("Error running the workflow, an exception has occured.", ANNONCELEVEL.ERROR);
		}
		((Text) getRoot().lookup("#descStatus")).setText("Status :\t\t DONE");
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	updateProgressBar(size);
		    }
		});
	}

	private Object getInput() {
		TextField field = (TextField) this.getRoot().lookup("#inputData");
		@SuppressWarnings("unchecked")
		ChoiceBox<String> inputChooser = (ChoiceBox<String>) ((HBox) (((VBox) (((BorderPane)(this.getRoot())).getCenter())).getChildren().get(2))).getChildren().get(0);
		String inputType = inputChooser.getSelectionModel().getSelectedItem();
		String input = field.getText();
		try {
			switch(inputType) {
			  case "INT":
				  this.addAnnonce("Input data : [INT] " + input, ANNONCELEVEL.INFO);
			    return Integer.parseInt(input);
			case "DOUBLE":
				this.addAnnonce("Input data : [DOUBLE] " + input, ANNONCELEVEL.INFO);
			    return Double.parseDouble(input);
			case "STRING []":
				this.addAnnonce("Input data : [STRING [  ]] " + input, ANNONCELEVEL.INFO);
				return input.split("[,]");
			case "STRING":
				this.addAnnonce("Input data : [STRING] " + input, ANNONCELEVEL.INFO);
				return input;
			default:
				this.addAnnonce("Input data : [STRING] " + input, ANNONCELEVEL.INFO);
				return input;
			}
		} catch (Exception e) {
			addAnnonce("Cast data to type impossible, running as String", ANNONCELEVEL.ERROR);
			return input;
		}
	}

	protected void updateWorkflows() {
		this.workflows.clear();
		this.workflows.add(Indrop.class);
		// this.workflows.add(SamplesExtended.class);
		this.workflows.add(Sarek.class);
		this.workflows.add(Unistrap.class);
	}

}
