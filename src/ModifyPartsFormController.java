/*
An error I had to fix in this class was bringing in the data from the Inventory array of parts. I solved it by getting the last clicked
on part id from the MainFormController and running a search through the Inventory.java methods.
Another point of functionality that I would add to this class would be to add in buttons to increase and decrease the inv as needed
when the part is modified.
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

public class ModifyPartsFormController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML Label dynamicTextLabel;
    @FXML TextField IDTextField;
    @FXML TextField NameTextField;
    @FXML TextField InvTextField;
    @FXML TextField PriceTextField;
    @FXML TextField MaxTextField;
    @FXML TextField MinTextField;
    @FXML TextField DynamicTextField;
    @FXML RadioButton inHouse;
    @FXML RadioButton outsourced;

    boolean isInHouse;

    public void SaveModifiedPart(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.NONE);

        int id = MainFormController.clickedOnPartId;
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
                            Inventory.updatePart(Inventory.getPartIndex(MainFormController.clickedOnPart), inHousePart);

                            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        }
                        else if (isInHouse == false){
                            Outsourced outsourcedPart = new Outsourced(id, partName, (Double.parseDouble(partPrice)), (Integer.parseInt(partInv)), (Integer.parseInt(partMax)), (Integer.parseInt(partMin)), partDynamic);
                            Inventory.updatePart(Inventory.getPartIndex(MainFormController.clickedOnPart), outsourcedPart);

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
        Part importedPart = MainFormController.clickedOnPart;
        IDTextField.setText(String.valueOf(importedPart.getId()));
        NameTextField.setText(importedPart.getName());
        InvTextField.setText(String.valueOf(importedPart.getStock()));
        PriceTextField.setText(String.valueOf(importedPart.getPrice()));
        MaxTextField.setText(String.valueOf(importedPart.getMax()));
        MinTextField.setText(String.valueOf(importedPart.getMin()));
        if (importedPart instanceof InHouse){
            DynamicTextField.setText(Integer.toString(((InHouse)importedPart).getMachineId()));
            inHouse.setSelected(true);
            isInHouse = true;
            dynamicTextLabel.setText("Machine ID");
        }
        else {
            DynamicTextField.setText(((Outsourced)importedPart).getCompanyName());
            outsourced.setSelected(true);
            isInHouse = false;
            dynamicTextLabel.setText("Company Name");
        }
    }
}
