/*
An error I had to fix in this class was getting the search bars to work. I had to look up exactly how to do it without using a ton of if statements
that would iterate through the arrays of parts and products.
I would add more functionality to this class by adding in ways to sort the tables by their data.
 */

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
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML TableColumn id;
    @FXML TableColumn name;
    @FXML TableColumn inv;
    @FXML TableColumn price;
    @FXML TableView partsTable;
    @FXML TableColumn productId;
    @FXML TableColumn productName;
    @FXML TableColumn productInv;
    @FXML TableColumn productPrice;
    @FXML TableView productTable;
    @FXML TextField partsSearchBar;
    @FXML TextField productsSearchBar;

    static Part clickedOnPart;
    static Product clickedOnProduct;
    static int clickedOnPartId;
    static int clickedOnProductId;

    public void switchToAddPartForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddPartsForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToModifyPartForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ModifyPartsForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAddProductForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddProductsForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToModifyProductForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ModifyProductsForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deletePart(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.NONE);
        boolean isPartUsed = false;

        for(Product product : Inventory.getAllProducts()) {
            for(Part part : product.getAllAssociatedParts()){
                if (part.getId() == clickedOnPartId){
                    isPartUsed = true;
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Cannot delete part! Part is used in a product!");
                    alert.show();
                }
            }
        }

        if (isPartUsed == false){
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this part?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK){
                Inventory.deletePart(clickedOnPart);
                partsTable.getItems().remove(partsTable.getSelectionModel().getSelectedItems());
            }
        }

    }

    public void deleteProduct(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK){
            Inventory.deleteProduct(clickedOnProduct);
            productTable.getItems().remove(productTable.getSelectionModel().getSelectedItems());
        }
    }

    public void OnClickExit(ActionEvent event){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partsTable.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Part p = (Part)partsTable.getSelectionModel().getSelectedItem();
                clickedOnPartId = p.getId();
                clickedOnPart = Inventory.lookupPart(clickedOnPartId);
            }
        });

        productTable.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Product p = (Product) productTable.getSelectionModel().getSelectedItem();
                clickedOnProductId = p.getId();
                clickedOnProduct = Inventory.lookupProduct(clickedOnProductId);
            }
        });

        final ObservableList<Part> partsData = Inventory.getAllParts();
        id.setCellValueFactory(new PropertyValueFactory<Part,Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Part,String>("name"));
        inv.setCellValueFactory(new PropertyValueFactory<Part,Integer>("stock"));
        price.setCellValueFactory(new PropertyValueFactory<Part,Double>("price"));
        partsTable.setItems(partsData);

        final ObservableList<Product> productsData = Inventory.getAllProducts();
        productId.setCellValueFactory(new PropertyValueFactory<Product,Integer>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product,Integer>("name"));
        productInv.setCellValueFactory(new PropertyValueFactory<Product,Integer>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Product,Integer>("price"));
        productTable.setItems(productsData);

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
        sortedPartData.comparatorProperty().bind(partsTable.comparatorProperty());
        partsTable.setItems(sortedPartData);
        //End Parts Search Section

        //Products Search Section
        FilteredList<Product> filteredProductData = new FilteredList<>(productsData, b -> true);

        productsSearchBar.textProperty().addListener((observable, oldvalue, newvalue) -> {
            filteredProductData.setPredicate(ProductExtension -> {

                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null){
                    return true;
                }

                String searchkeyword = newvalue.toLowerCase();

                if (ProductExtension.getName().toLowerCase().indexOf(searchkeyword) > -1){
                    return true;
                }
                else if (String.valueOf(ProductExtension.getId()).indexOf(searchkeyword) > -1){
                    return true;
                }
                else {
                    return false;
                }

            });
        });

        SortedList<Product> sortedProductdata = new SortedList<>(filteredProductData);
        sortedProductdata.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedProductdata);
        //End Product Search Section
    }
}
