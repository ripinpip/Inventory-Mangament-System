/*
I didn't run into any error when I was writing this class and just stuck to what the UML document wanted me to do.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private static ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int max;
    private int min;

    public Product(int id, String name, double price, int stock, int max, int min, ObservableList associatedParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.max = max;
        this.min = min;
        this.associatedParts = associatedParts;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public void setMax(int max){
        this.max = max;
    }

    public void setMin(int min){
        this.min = min;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getStock(){
        return stock;
    }

    public int getMax(){
        return max;
    }

    public int getMin(){
        return min;
    }

    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }

    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
