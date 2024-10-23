package app.entities;

public class ProductLine {
    private int productLineId;
    private int quantity;
    private Cupcake cupcake;

    public ProductLine(int productLineId, Cupcake cupcake) {
        this.productLineId = productLineId;
        this.cupcake = cupcake;
    }

}
