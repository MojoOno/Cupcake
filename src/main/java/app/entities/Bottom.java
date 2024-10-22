package app.entities;

public class Bottom {
    String bottomName;
    float bottomPrice;

    public Bottom(String bottomName, float bottomPrice) {
        this.bottomName = bottomName;
        this.bottomPrice = bottomPrice;
    }

    public String getBottomName() {
        return bottomName;
    }

    public float getBottomPrice() {
        return bottomPrice;
    }
}
