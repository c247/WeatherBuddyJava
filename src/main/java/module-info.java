module com.example.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.chatapp to javafx.fxml;
    exports com.example.chatapp;
}