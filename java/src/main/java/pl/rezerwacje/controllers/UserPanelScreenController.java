package pl.rezerwacje.controllers;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import pl.rezerwacje.*;

import java.io.IOException;

public class UserPanelScreenController extends Buttons {

    @FXML
    private Label myText;
    @FXML
    private ComboBox reservationsssComboBox;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField repeatNewPassword;

    private ObservableList<String> reser;

    public void initialize() throws IOException {
        loadMyText();
        loadMyReservation();
    }

    private void loadMyReservation() throws IOException {
        reser = FXCollections.observableArrayList();
        Main.dos.writeUTF("My reservation");
        Main.dos.writeInt(User.getId());
        while(Main.dis.readBoolean()) {
            reser.add(Main.dis.readUTF());
        }
        reservationsssComboBox.setItems(null);
        reservationsssComboBox.setItems(reser);
    }

    private void loadMyText() throws IOException {
        Main.dos.writeUTF("Text");
        Main.dos.writeUTF(String.valueOf(User.getId()));
        //myText.setText("Cześć "+Main.dis.readUTF()+"!/nOto Twoje rezerwacje");
        if(Main.dis.readBoolean()) {
            myText.setText("Cześć "+Main.dis.readUTF()+"!\nTutaj możesz usunąć swoje rezerwacje oraz zmienić hasło.");
        }
        myText.setStyle("-fx-font-size:16;");
    }

    @FXML
    private void changePassword() throws Exception {
        if(newPassword.getText().length() >=8 ) {
            if(newPassword.getText().equals(repeatNewPassword.getText())) {
                Main.dos.writeUTF("New password");
                Main.dos.writeUTF(newPassword.getText());
                Main.dos.writeUTF(User.nick);
            } else {
                Dialog.informationAlert("Złe hasło", "Hasła nie są identyczne");
            }
        } else {
            Dialog.informationAlert("Złe hasło", "Hasło jest zbyt krótkie");
        }
    }

    public void deleteReservation(ActionEvent event) throws IOException {
        if(reservationsssComboBox.getSelectionModel().isEmpty()) {
            Dialog.informationAlert("Brak rezerwacji", "Nie wybrano rezerwacji do usunięcia !");
        } else {
            Main.dos.writeUTF("Delete reservation");
            String[] tmp = String.valueOf(reservationsssComboBox.getValue()).split("  ");
            Main.dos.writeUTF(tmp[0]);
        }
        loadMyReservation();
    }
}
