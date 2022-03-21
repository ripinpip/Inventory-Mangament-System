/*
An error I had to fix in this class was adding the associated parts to the table. I fixed this by adding a temporary Observable list in this class that was
later saved into the Product.
I would add more functionality to this page in the future by adding a search bar for the associated parts.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductsFormController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML TextField NameTextField;
    @FXML TextField InvTextField;
    @FXML TextField PriceTextField;
    @FXML TextField MaxTextField;
    @FXML TextField MinTextField;
    @FXML TableColumn completePartsId;
    @FXML TableColumn completePartsName;
    @FXML TableColumn completePartsInv;
    @FXML TableColumn completePartsPrice;
    @FXML TableView completePartsTable;
    @FXML TableColumn AssociatedPartsId;
    @FXML TableColumn AssociatedPartsName;
    @FXML TableColumn AssociatedPartsInv;
    @FXML TableColumn AssociatedPartsPrice;
    @FXML TableView AssociatedPartsTable;
    @FXML TextField partsSearchBar;

    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    Part clickedOnPart;
    int clickedOnPartId;
    int clickedOnRowPosition;

    public void OnSaveClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);

        int id = Inventory.productIdGenerator();
        String productName = NameTextField.getText();
        String productInv = InvTextField.getText();
        String productPrice = PriceTextField.getText();
        String productMax = MaxTextField.getText();
        String productMin = MinTextField.getText();

        if (!(productName.isBlank()||productName.isEmpty()) && !(productInv.isBlank()||productInv.isEmpty()) && !(productPrice.isBlank()||productPrice.isEmpty()) && !(productMax.isBlank()||productMax.isEmpty()) && !(productMin.isBlank()||productMin.isEmpty())) {
            if (productInv.matches("[0-9]+") && productPrice.matches("[0-9,.]+") && productMax.matches("[0-9]+") && productMin.matches("[0-9]+")){
                if ((Integer.parseInt(productMin)) <= (Integer.parseInt(productMax))){
                    if (((Integer.parseInt(productInv)) >= (Integer.parseInt(productMin))) && ((Integer.parseInt(productInv)) <= (Integer.parseInt(productMax)))) {
                        Product product = new Product(id, productName, (Double.parseDouble(productPrice)), (Integer.parseInt(productInv)), (Integer.parseInt(productMax)), (Integer.parseInt(productMin)), associatedParts);
                        Inventory.addProduct(product);

                        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                    else {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("Inv must be in between Min and Max!");
                        alert.show();
                    }
                }
                else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Min must be less than Max!");
                    alert.show();
                }
            }
            else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Inventory, Price, Max, and Min Fields can only contain digits!");
                alert.show();
            }
        }
        else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Each text field must be filled out!");
            alert.show();
        }

    }

    public void OnAddClicked(ActionEvent event){
        if (clickedOnPart != null) {
            associatedParts.add(clickedOnPart);

            final ObservableList<Part> data = associatedParts;
            AssociatedPartsId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
            AssociatedPartsName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
            AssociatedPartsInv.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
            AssociatedPartsPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
            AssociatedPartsTable.setItems(data);
        }
    }

    public void OnPartRemove(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to remove this part?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK){
            AssociatedPartsTable.getItems().remove(clickedOnRowPosition);
        }
    }

    public void onCancelClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        completePartsTable.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Part p = (Part)completePartsTable.getSelectionModel().getSelectedItem();
                clickedOnPartId = p.getId();
                clickedOnPart = Inventory.lookupPart(clickedOnPartId);
            }
        });

        AssociatedPartsTable.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                clickedOnRowPosition = AssociatedPartsTable.getSelectionModel().selectedIndexProperty().get();
            }
        });

        final ObservableList<Part> partsData = Inventory.getAllParts();
        completePartsId.setCellValueFactory(new PropertyValueFactory<Part,Integer>("id"));
        completePartsName.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));
        completePartsInv.setCellValueFactory(new PropertyValueFactory<Part,Integer>("stock"));
        completePartsPrice.setCellValueFactory(new PropertyValueFactory<Part,Double>("price"));
        completePartsTable.setItems(partsData);

        //Parts Search Section
        FilteredList<Part> filteredPartData = new FilteredList<>(partsData, b -> true);

        partsSearchBar.textProperty().addListener((observable, oldvalue, newvalue) -> {
            filteredPartData.setPredicate(PartExtension -> {

                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null){
                    return true;
                }

                String searchkeyword = newvalue.toLowerCase();

                if (PartExtension.getName().toLowerCase().indexOf(searchkeyword) > -1){
                    return true;
                }
                else if (String.valueOf(PartExtension.getId()).indexOf(searchkeyword) > -1){
                    return true;
                }
                else {
                    return false;
                }

            });
        });

        SortedList<Part> sortedPartData = new SortedList<>(filteredPartData);
        sortedPartData.comparatorProperty().bind(completePartsTable.comparatorProperty());
        completePartsTable.setItems(sortedPartData);
        //End Parts Search Section
    }
}
