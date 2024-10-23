package app.entities;

public class Topping {
    String toppingName;
    float toppingPrice;

    public Topping(String toppingName, float toppingPrice) {
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
    }

    public String getToppingName() {
        return toppingName;
    }

    public float getToppingPrice() {
        return toppingPrice;
    }
}
