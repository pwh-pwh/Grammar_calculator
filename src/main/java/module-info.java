module com.coderpwh.grammar_processor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
//    requires org.antlr.antlr4.runtime;

    opens com.coderpwh.grammar_processor to javafx.fxml;
    exports com.coderpwh.grammar_processor;
}