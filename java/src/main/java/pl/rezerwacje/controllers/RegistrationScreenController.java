package pl.rezerwacje.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pl.rezerwacje.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RegistrationScreenController extends Buttons {

    @FXML
    private TextField nick;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repeatPassword;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField email;


    public void initialize() throws SQLException, ClassNotFoundException {
        System.out.println(User.getId());
        System.out.println(User.getNick());
    }

    public void registration(ActionEvent event) throws IOException {
        if(nick.getText().isEmpty() || password.getText().isEmpty() || repeatPassword.getText().isEmpty() || name.getText().isEmpty() || surname.getText().isEmpty() || email.getText().isEmpty()) {
            Dialog.informationAlert("Brak danych", "Wszystkie pola muszą zostać uzupełnione");
        } else {
            if(Email.isValid(email.getText())) {
                if(password.getText().equals(repeatPassword.getText()) && password.getText().length()>=8) {
                    Main.dos.writeUTF("Register");
                    Main.dos.writeUTF(nick.getText().toLowerCase());
                    Main.dos.writeUTF(name.getText());
                    Main.dos.writeUTF(surname.getText());
                    Main.dos.writeUTF(password.getText());
                    Main.dos.writeUTF(email.getText());
                } else {
                    Dialog.informationAlert("Błąd haseł", "Hasła nie są identyczne lub ich długość jest mniejsza niż 8");
                }
            } else {
                Dialog.informationAlert("Niepoprawny email", "Email ma niepoprawny format");
            }
            if(!Main.dis.readBoolean()) {
                Dialog.informationAlert("Niepoprawny nick", "Ten nick jest już zajęty");
            }
        }
    }

}
