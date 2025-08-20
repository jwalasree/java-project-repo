package bank;

public class User {
    protected String name;
    protected long accNum;
    protected int accPin;
    protected int accBal;

    User(String name, long accNum, int accPin, int accBal) {
        this.name = name;
        this.accNum = accNum;
        this.accPin = accPin;
        this.accBal = accBal;
    }
}