module es.prorix.gusanoguason {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.controls;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires java.sql;

    opens es.prorix.gusanoguason to javafx.fxml;
    exports es.prorix.gusanoguason;
    exports es.prorix.gusanoguason.controllers;
    exports es.prorix.gusanoguason.database;
    exports es.prorix.gusanoguason.main;
    exports es.prorix.gusanoguason.models;
    opens es.prorix.gusanoguason.controllers to javafx.fxml;

    opens es.prorix.gusanoguason.database to java.sql;
    opens es.prorix.gusanoguason.main to javafx.fxml;
    opens es.prorix.gusanoguason.models to javafx.fxml;
    opens es.prorix.gusanoguason.views to javafx.fxml;

    opens es.prorix.gusanoguason.resources to javafx.fxml;
    opens es.prorix.gusanoguason.utils to javafx.fxml;
    opens es.prorix.gusanoguason.services to javafx.fxml;
    opens es.prorix.gusanoguason.exceptions to javafx.fxml;
}
