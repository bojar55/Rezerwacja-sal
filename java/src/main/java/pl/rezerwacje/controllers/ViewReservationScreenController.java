package pl.rezerwacje.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import pl.rezerwacje.Buttons;
import pl.rezerwacje.Main;

import java.io.IOException;

public class ViewReservationScreenController extends Buttons {

    @FXML
    private ListView<String> reservationListView;

    public void initialize() throws IOException {
        showReservation();
    }

    public void showReservation() throws IOException {
        reservationListView.getItems().clear();
        Main.dos.writeUTF("Show reservation");
        String[] allReservation = Main.dis.readUTF().split("!");

        for(int i=0; i<allReservation.length; i++) {
            reservationListView.getItems().add(i, allReservation[i]);
        }
    }


}
