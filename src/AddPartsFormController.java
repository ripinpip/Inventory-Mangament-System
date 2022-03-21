/*
An error I had to fix in this class was adding the checks into the save button. I had to make it work in an order and display the correct
error messages according to each if statement. Keeping track of the checks and the error statements was a bit confusing.
Another point of functionality that I would add in the page would to add a section where the user could write their own custom comments
about the part.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartsFormController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML private Label dynamicTextLabel;
    @FXML private TextField NameTextField;
    @FXML private TextField InvTextField;
    @FXML private TextField PriceTextField;
    @FXML private TextField MaxTextField;
    @FXML private TextField MinTextField;
    @FXML private TextField DynamicTextField;
    @FXML private RadioButton inHouse;

    boolean isInHouse = true;

    public void AddPartsInHouseRadio(ActionEvent event) throws IOException {
        isInHouse = true;
        dynamicTextLabel.setText("Machine ID");
    }

    public void AddPartsOutsourcedRadio(ActionEvent event) throws IOException {
        isInHouse = false;
        dynamicTextLabel.setText("Company Name");
    }

    public void AddPartsSave(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);

        int id = Inventory.partIdGenerator();
        String partName = NameTextField.getText();
        String partInv = InvTextField.getText();
        String partPrice = PriceTextField.getText();
        String partMax = MaxTextField.getText();
        String partMin = MinTextField.getText();
        String partDynamic = DynamicTextField.getText();

        if (!(partName.isBlank()||partName.isEmpty()) && !(partInv.isBlank()||partInv.isEmpty()) && !(partPrice.isBlank()||partPrice.isEmpty()) && !(partMax.isBlank()||partMax.isEmpty()) && !(partMin.isBlank()||partMin.isEmpty())) {
            if (partInv.matches("[0-9]+") && partPrice.matches("[0-9,.]+") && partMax.matches("[0-9]+") && partMin.matches("[0-9]+")){
                if ((Integer.parseInt(partMin)) <= (Integer.parseInt(partMax))){
                    if (((Integer.parseInt(partInv)) >= (Integer.parseInt(partMin))) && ((Integer.parseInt(partInv)) <= (Integer.parseInt(partMax)))) {
                        if (isInHouse == true && partDynamic.matches("[0-9]+")) {
                            InHouse inHousePart = new InHouse(id, partName, (Double.parseDouble(partPrice)), (Integer.parseInt(partInv)), (Integer.parseInt(partMax)), (Integer.parseInt(partMin)), (Integer.parseInt(partDynamic)));
                            Inventory.addPart(inHousePart);

                            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        }
                        else if (isInHouse == false){
                            Outsourced outsourcedPart = new Outsourced(id, partName, (Double.parseDouble(partPrice)), (Integer.parseInt(partInv)), (Integer.parseInt(partMax)), (Integer.parseInt(partMin)), partDynamic);
                            Inventory.addPart(outsourcedPart);

                            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        }
                        else {
                            alert.setAlertType(Alert.AlertType.ERROR);
                            alert.setContentText("Machine ID can only contain digits!");
                            alert.show();
                        }
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

    public void onCancelClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inHouse.setSelected(true);
    }
}
