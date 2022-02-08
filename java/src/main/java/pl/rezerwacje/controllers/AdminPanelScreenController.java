package pl.rezerwacje.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pl.rezerwacje.Buttons;
import pl.rezerwacje.Dialog;
import pl.rezerwacje.Main;

import java.io.IOException;


public class AdminPanelScreenController extends Buttons {

    @FXML
    private TextField nameRoom;
    @FXML
    private TextField numberOfPlace;
    @FXML
    private TextField nameType;
    @FXML
    private ComboBox reservationComboBox;

    private ObservableList<String> reservations;

    public void initialize() throws IOException {
        loadReservationComboBox();
    }

    public void addRoom(ActionEvent event) throws IOException {
        if(nameRoom.getText().isEmpty() || numberOfPlace.getText().isEmpty()) {
            Dialog.informationAlert("Brak danych", "Wszystkie pola muszą zostać uzupełnione");
        } else {
            Main.dos.writeUTF("Add room");
            Main.dos.writeUTF(nameRoom.getText());
            Main.dos.writeUTF(numberOfPlace.getText());
        }
        nameRoom.clear();
        numberOfPlace.clear();
    }

    public void addType(ActionEvent event) throws IOException {
        if(nameType.getText().isEmpty()) {
            Dialog.informationAlert("Brak nazwy", "Pole nazwa musi zostać uzupełniona");
        } else {
            Main.dos.writeUTF("Add type");
            Main.dos.writeUTF(nameType.getText());
        }
        nameType.clear();
    }

    private void loadReservationComboBox() throws IOException {
        Main.dos.writeUTF("Load reservation");
        reservations = FXCollections.observableArrayList();
        while(Main.dis.readBoolean()) {
            reservations.add(Main.dis.readUTF());
        }
        reservationComboBox.setItems(null);
        reservationComboBox.setItems(reservations);
    }

    public void deleteReservation(ActionEvent event) throws IOException {
        if(reservationComboBox.getSelectionModel().isEmpty()) {
            Dialog.informationAlert("Brak rezerwacji", "Nie wybrano rezerwacji do usunięcia !");
        } else {
            Main.dos.writeUTF("Delete reservation");
            String[] tmp = String.valueOf(reservationComboBox.getValue()).split("  ");
            Main.dos.writeUTF(tmp[0]);
        }
        loadReservationComboBox();
    }
}
