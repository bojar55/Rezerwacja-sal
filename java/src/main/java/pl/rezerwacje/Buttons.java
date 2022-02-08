package pl.rezerwacje;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Buttons {

    public void setStage(ActionEvent event, String s) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }



    public void openRegistrationScreen(ActionEvent event) {
        setStage(event, "/fxml/RegistrationScreen.fxml");
    }

    public void openArchives(ActionEvent event) {
        setStage(event, "/fxml/ArchiveScreen.fxml");
    }

    public void exit(ActionEvent event) throws IOException {
        Dialog.confirmationAlert("Wyjście", "Czy na pewno chcesz wyjść?");
    }

    public void openLoginScreen(ActionEvent event) {
        setStage(event, "/fxml/LoginScreen.fxml");
    }

    public void logout(ActionEvent event) {
        setStage(event, "/fxml/LoginScreen.fxml");
        User.logout();
    }

    public void openAdminPanel(ActionEvent event) {
        if (User.getNick().equals("bojar55")) {
            setStage(event, "/fxml/AdminPanelScreen.fxml");
        } else {
            Dialog.informationAlert("Brak dostępu", "Nie jesteś administratorem");
        }
    }
    public void openUserPanel(ActionEvent event) {
        setStage(event, "/fxml/UserPanelScreen.fxml");
    }

    public void openViewReservation(ActionEvent event) {
        setStage(event, "/fxml/ViewReservationScreen.fxml");
    }

    public void back(ActionEvent event) {
        setStage(event, "/fxml/ViewReservationScreen.fxml");
    }

    public void addReservationScreen(ActionEvent event) {
        setStage(event, "/fxml/AddReservationScreen.fxml");
    }

}
