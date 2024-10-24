package app.entities;

public class ProductLine {
    private int productLineId;
    private int quantity;
    private Cupcake cupcake;

    public ProductLine(int productLineId, Cupcake cupcake, int quantity) {
        this.productLineId = productLineId;
        this.cupcake = cupcake;
        this.quantity = quantity;
    }

    public ProductLine(int productLineId, Cupcake cupcake) {
        this.productLineId = productLineId;
        this.cupcake = cupcake;
    }

    public Cupcake getCupcake() {
        return cupcake;
    }

    public int getProductLineId() {
        return productLineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalPrice() {
        return cupcake.getPrice() * quantity;
    }
}
