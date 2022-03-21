/*
An error that I was having in this class was writing the lookup methods. I didn't write the loop correctly so if it found a match I wouldn't return the right part.
I fixed it by having it return the part that was being iterated though that matched the id.
A method that I would add to this would to be to add a lookup by name or id method that return a list with the results to be used by the search
methods in the MainForm and the add and modify Product forms.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private static ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static int partIdCount = 0;
    private static int productIdCount = 0;

    public static int partIdGenerator(){
        partIdCount++;
        return partIdCount;
    }

    public static int productIdGenerator(){
        productIdCount++;
        return productIdCount;
    }

    public static void addPart(Part newPart) {
        partInventory.add(newPart);
    }

    public static void addProduct(Product newProduct){
        productInventory.add(newProduct);
    }

    public static Part lookupPart(int partId){
        for(Part part : partInventory) {
            if(part == null) {
                continue;
            }
            else if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    public static Product lookupProduct(int productId){
        for(Product product : productInventory) {
            if(product == null) {
                continue;
            }
            else if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public static int getPartIndex(Part selectedPart){
        return partInventory.indexOf(selectedPart);
    }

    public static int getProductIndex(Product selectedProduct){
        return productInventory.indexOf(selectedProduct);
    }

    public static void updatePart(int index, Part updatedPart){
        partInventory.set(index, updatedPart);
    }

    public static void updateProduct(int index, Product updatedProduct){
        productInventory.set(index, updatedProduct);
    }

    public static boolean deletePart(Part selectedPart){
        partInventory.remove(selectedPart);
        return true;
    }

    public static boolean deleteProduct(Product selectedProduct){
        productInventory.remove(selectedProduct);
        return true;
    }

    public static ObservableList<Part> getAllParts(){
        return partInventory;
    }

    public static ObservableList<Product> getAllProducts(){
        return productInventory;
    }

}
