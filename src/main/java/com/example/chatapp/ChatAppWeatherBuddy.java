package com.example.chatapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;


public class ChatAppWeatherBuddy extends Application {
    private TextArea chatArea;
    private TextField messageField;

    public void start(Stage stage) {
        // Set up the chat area
        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setVgap(10);
        layout.setHgap(10);
        layout.add(chatArea, 0, 0, 2, 1);
        layout.add(messageField, 0, 1);
        layout.add(sendButton, 1, 1);

        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("Chat App");
        stage.setScene(scene);
        stage.show();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            if (isCity(message)) {
                String temperature = getTemperature(message);
                if (temperature != null) {
                    chatArea.appendText("You: " + message + "\n");
                    chatArea.appendText("Temperature: " + temperature + " degrees\n");
                } else {
                    chatArea.appendText("Could not get temperature for " + message + "\n");
                }
            } else {
                chatArea.appendText("You: " + message + "\n");
            }
            messageField.clear();
        }
    }

    private boolean isCity(String message) {
        // TODO: Implement city detection logic
        // You could use a regex or a city API to check if the message is a city
        return true;
    }

    private String getTemperature(String city) {
        String apiKey = "4930fa519fd54f8a206023e7381f9b4b";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject mainObject = jsonObject.getJSONObject("main");
                double temperature = mainObject.getDouble("temp");
                temperature = temperature - 273.15;
                return String.format("%.1f", temperature);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
