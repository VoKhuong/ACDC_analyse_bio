module ACDC_2 {
	requires transitive javafx.controls;
	requires transitive javafx.base;
	requires transitive javafx.graphics;
	requires javafx.media;
	requires org.jetbrains.annotations;
	
	exports IHM;
	exports Samples;
	exports WorkFlowEngine;
	exports WorkflowExamples;
}