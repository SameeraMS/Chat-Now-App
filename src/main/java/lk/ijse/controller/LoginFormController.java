package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.client.Client;
import lk.ijse.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginFormController {
    public JFXTextField txtName;

    public static String name;
    public ImageView imageView;
    private File file;

    public void initialize() throws IOException {
        startServer();
    }

    public void txtUsernameOnAction(ActionEvent actionEvent) throws IOException {
        btnLoginOnAction(actionEvent);
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        load();
        txtName.clear();
        imageView.setImage(new Image("/asserts/images/images.png"));
    }

    private void load() throws IOException {
        if (Pattern.matches("^[a-zA-Z\\s]+", txtName.getText())) {
            Client client = new Client(txtName.getText(), imageView);
            Thread thread = new Thread(client);
            thread.start();

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/chatForm.fxml"));
            Parent root = fxmlLoader.load();

           // ChatFormController chatForm =  fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle(txtName.getText()+"'s Chat");
            stage.setAlwaysOnTop(true);
        }
    }

    private void startServer() throws IOException {
        Server server = Server.getServerSocket();
        Thread thread = new Thread(server);
        thread.start();
    }

    public void btnImageChooserOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtName.getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                imageView.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
