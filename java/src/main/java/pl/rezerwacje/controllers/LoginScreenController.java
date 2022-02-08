package pl.rezerwacje.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pl.rezerwacje.Buttons;
import pl.rezerwacje.Dialog;
import pl.rezerwacje.Main;
import pl.rezerwacje.User;

import java.io.IOException;

public class LoginScreenController extends Buttons {

    @FXML
    private TextField nick;
    @FXML
    private PasswordField password;

    public void login(ActionEvent event) throws IOException {
        if(nick.getText().isEmpty() || password.getText().isEmpty()) {
            Dialog.informationAlert("Błąd logowania", "Wprowadzono niepoprawne dane lub nie uzupełniono wszystkich pól");
        } else {
            Main.dos.writeUTF("Login");
            Main.dos.writeUTF(nick.getText().toLowerCase());
            if(Main.dis.readBoolean()) {
                Main.dos.writeUTF(password.getText());
                if(Main.dis.readBoolean()) {
                    User.setId(Integer.parseInt(Main.dis.readUTF()));
                    User.setNick(Main.dis.readUTF());
                    setStage(event, "/fxml/ViewReservationScreen.fxml");
                } else {
                    Dialog.informationAlert("Złe hasło", "Wprowadzono niepoprawne hasło");
                }
            } else {
                Dialog.informationAlert("Zły login", "Nie ma takiego użytkownika");
            }

        }
    }

}
