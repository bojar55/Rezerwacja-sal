package pl.rezerwacje.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import pl.rezerwacje.Buttons;
import pl.rezerwacje.Main;

import java.io.IOException;

public class ArchiveScreenController extends Buttons {

    @FXML
    private ListView<String> archiveListView;

    public void initialize() throws IOException {
        showArchive();
    }

    public void showArchive() throws IOException {
        archiveListView.getItems().clear();
        Main.dos.writeUTF("Show archive");
        String[] allReservation = Main.dis.readUTF().split("!");

        for(int i=0; i<allReservation.length; i++) {
            archiveListView.getItems().add(i, allReservation[i]);
        }
    }
}
