module com.wapitia.games.worddivision.jfx {
    requires transitive kotlin.stdlib;
    requires kotlin.stdlib.jdk8;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.wapitia.games.worddivision.base;

    opens com.wapitia.games.worddivision.front.jfx to javafx.fxml;
    exports com.wapitia.games.worddivision.front.jfx to javafx.graphics;
}