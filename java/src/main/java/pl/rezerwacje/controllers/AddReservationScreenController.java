package pl.rezerwacje.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pl.rezerwacje.Buttons;
import pl.rezerwacje.Dialog;
import pl.rezerwacje.Main;
import pl.rezerwacje.User;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.regex.Pattern;

public class AddReservationScreenController extends Buttons {

    @FXML
    private DatePicker date;
    @FXML
    private TextField timeFrom;
    @FXML
    private TextField timeTo;
    @FXML
    private ComboBox roomComboBox;
    @FXML
    private ComboBox typeComboBox;

    private ObservableList<String> rooms;
    private ObservableList<String> types;

    public void initialize() throws IOException {
        loadRoomComboBox();
        loadTypeComboBox();
    }

    public void reset(ActionEvent event) throws IOException {
        timeFrom.clear();
        timeTo.clear();
        loadRoomComboBox();
        loadTypeComboBox();
        date.setValue(null);
    }

    public void add(ActionEvent event) throws IOException {

        Pattern goodTime  = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]");

        if(timeTo.getText().isEmpty() || timeFrom.getText().isEmpty() || roomComboBox.getSelectionModel().isEmpty() || typeComboBox.getSelectionModel().isEmpty() || date.getValue() == null) {
            Dialog.informationAlert("Brak danych", "Wszystkie pola muszą zostać uzupełnione");
        } else {
            String timeFS = timeFrom.getText();
            String[] timeFSTmp = timeFS.split(":");
            int[] timeF = new int[3];
            String timeTS = timeTo.getText();
            String[] timeTSTmp = timeTS.split(":");
            int[] timeT = new int[3];
            if (!((goodTime.matcher(timeFS).matches()) && (goodTime.matcher(timeTS)).matches()))
            {
                Dialog.informationAlert("Zła godzina", "Podany zapis godzin jest niepoprawny");
            }
            for(int i=0; i<timeFSTmp.length; i++) {
                timeF[i] = Integer.parseInt(timeFSTmp[i]);
                timeT[i] = Integer.parseInt(timeTSTmp[i]);
            }
            if(00<=timeF[0] && timeF[0]<=24 && 00<=timeF[1] && timeF[1]<=59 && 00<=timeF[2] && timeF[2]<=59 &&
                    00<=timeT[0] && timeT[0]<=24 && 00<=timeT[1] && timeT[1]<=59 && 00<=timeT[2] && timeT[2]<=59) {
                if(timeF[0] > timeT[0]) {
                    Dialog.informationAlert("Niepoprawne godziny", "Godzina do musi być większa");
                } else if(timeF[0] == timeT[0]) {
                    if(timeF[1] > timeT[1]) {
                        Dialog.informationAlert("Niepoprawne minuty", "Minuty do muszą być większe");
                    } else {
                        Main.dos.writeUTF("Add reservation");
                        Main.dos.writeUTF(date.getValue()+" "+timeFS);
                        Main.dos.writeUTF(date.getValue()+" "+timeTS);
                        Main.dos.writeUTF(roomComboBox.getSelectionModel().getSelectedItem().toString());
                        Main.dos.writeUTF(typeComboBox.getSelectionModel().getSelectedItem().toString());
                        Main.dos.writeInt(User.id);
                        String answer = Main.dis.readUTF();
                        if(answer.contains("Bad")) Dialog.informationAlert("Zły termin", "Ten termin jest już zajęty");
                        if(answer.contains("Good")) Dialog.informationAlert("Udało się", "Twoja rezerwacja została dodana");
                    }
                } else {
                    Main.dos.writeUTF("Add reservation");
                    Main.dos.writeUTF(date.getValue()+" "+timeFS);
                    Main.dos.writeUTF(date.getValue()+" "+timeTS);
                    Main.dos.writeUTF(roomComboBox.getSelectionModel().getSelectedItem().toString());
                    Main.dos.writeUTF(typeComboBox.getSelectionModel().getSelectedItem().toString());
                    Main.dos.writeInt(User.id);
                    String answer = Main.dis.readUTF();
                    if(answer.contains("Bad")) Dialog.informationAlert("Zły termin", "Ten termin jest już zajęty");
                    if(answer.contains("Good")) Dialog.informationAlert("Udało się", "Twoja rezerwacja została dodana");
                }
            } else {
                Dialog.informationAlert("Niepoprawna godzina", "Taka godzina nie istnieje");
            }
        }
    }

    private void loadRoomComboBox() throws IOException {
        Main.dos.writeUTF("Load room");
        rooms = FXCollections.observableArrayList();
        while(Main.dis.readBoolean()) {
            rooms.add(Main.dis.readUTF());
        }
        roomComboBox.setItems(null);
        roomComboBox.setItems(rooms);
    }

    private void loadTypeComboBox() throws IOException {
        Main.dos.writeUTF("Load type");
        types = FXCollections.observableArrayList();
        while(Main.dis.readBoolean()) {
            types.add(Main.dis.readUTF());
        }
        typeComboBox.setItems(null);
        typeComboBox.setItems(types);
    }
}
