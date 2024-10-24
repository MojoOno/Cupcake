package app.entities;

public class Topping {
    private String bottomName;
    private int toppingId;
    float toppingPrice;

    public Topping(int toppingId, float toppingPrice) {
        this.toppingId = toppingId;
        this.toppingPrice = toppingPrice;
    }

    public int getToppingId() {
        return toppingId;
    }

    public float getToppingPrice() {
        return toppingPrice;
    }

    public String getBottomName() {
        return bottomName;
    }
}
