package de.b4f.reservation.business;

import com.google.common.primitives.Ints;

public class TradingUnit {

    private final int STOCK = 0;
    private final int BASKET = 1;

    private final long id;
    private final int [] quantities;


    public TradingUnit(final long id,final int stockQuantity) {
        this.id = id;
        this.quantities = new int[]{stockQuantity,0};
    }

    public long getId() {
        return id;
    }

    public int getInStock(){
        return quantities[STOCK];
    }

    public int reserve(int quantity){
        return moveQuantities(quantity, STOCK, BASKET);
    }

    public int release(int quantity){
        return moveQuantities(quantity,BASKET,STOCK);
    }

    private int moveQuantities(final int quantity, final int src, final int dest) {
        int reserved = Ints.min(quantities[src], quantity);

        quantities[src]  -= reserved;
        quantities[dest] += reserved;

        return reserved;
    }
}
