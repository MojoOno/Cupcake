package app.entities;

public class Bottom {
    String bottomName;
    private int bottomId;
    float bottomPrice;

    public Bottom(int bottomId, float bottomPrice) {
        this.bottomId = bottomId;
        this.bottomPrice = bottomPrice;
    }

    public String getBottomName() {
        return bottomName;
    }

    public float getBottomPrice() {
        return bottomPrice;
    }
}
