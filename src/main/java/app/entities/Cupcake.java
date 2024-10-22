package app.entities;

import java.util.Map;

public class Cupcake {
    Bottom bottom;
    Topping topping;

    public Cupcake(Bottom bottom, Topping topping) {
        this.bottom = bottom;
        this.topping = topping;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public Topping getTopping() {
        return topping;
    }
}
