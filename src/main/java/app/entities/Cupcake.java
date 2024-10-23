package app.entities;

import java.util.Map;

public class Cupcake {
        private Bottom bottom;
        private Topping topping;

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