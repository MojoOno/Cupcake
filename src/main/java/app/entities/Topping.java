package app.entities;

public class Topping {
    private String toppingName;
    private int toppingId;
    float toppingPrice;

    public Topping(int toppingId,String toppingName, float toppingPrice) {
        this.toppingId = toppingId;
        this.toppingPrice = toppingPrice;
        this.toppingName = toppingName;
    }

    public Topping(int toppingId, float toppingPrice) {
        this.toppingId = toppingId;
        this.toppingPrice = toppingPrice;
    }

    public Topping(String bottomName, int toppingId, float toppingPrice) {
        this.bottomName = bottomName;
        this.toppingId = toppingId;
        this.toppingPrice = toppingPrice;
    }

    public int getToppingId() {
        return toppingId;
    }

    public float getToppingPrice() {
        return toppingPrice;
    }

    public String getToppingName() {
        return toppingName;
    }
}
