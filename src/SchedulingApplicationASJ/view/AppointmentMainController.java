/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingApplicationASJ.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author eunice
 */
public class AppointmentMainController implements Initializable {

    @FXML
    private AnchorPane appointmentMain;
    @FXML
    private TabPane tabs;
    @FXML
    private Tab monthly;
    @FXML
    private TableView<?> monthAptTable;
    @FXML
    private TableColumn<?, ?> monthDescription;
    @FXML
    private TableColumn<?, ?> monthContact;
    @FXML
    private TableColumn<?, ?> monthLocation;
    @FXML
    private TableColumn<?, ?> monthStart;
    @FXML
    private TableColumn<?, ?> monthEnd;
    @FXML
    private Label monthCustomerLabel;
    @FXML
    private TableView<?> weekAptTable;
    @FXML
    private TableColumn<?, ?> weekDescription;
    @FXML
    private TableColumn<?, ?> weekContact;
    @FXML
    private TableColumn<?, ?> weekLocation;
    @FXML
    private TableColumn<?, ?> weekStart;
    @FXML
    private TableColumn<?, ?> weekEnd;
    @FXML
    private Label weekCustomerLabel;
    @FXML
    private TableView<?> customerTable;
    @FXML
    private TableColumn<?, ?> customerId;
    @FXML
    private TableColumn<?, ?> customerName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleCustomerClick(MouseEvent event) {
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
    }

    @FXML
    private void handleModifyButton(ActionEvent event) {
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
    }
    
}
