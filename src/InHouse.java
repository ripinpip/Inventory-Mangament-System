/*
I didn't have any errors in this class except realizing that I needed to extend it from the Part class.
Writing the set and get methods was pretty simple.
 */

public class InHouse extends Part{

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    public int getMachineId(){
        return machineId;
    }
}
