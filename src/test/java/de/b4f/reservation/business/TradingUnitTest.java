package de.b4f.reservation.business;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TradingUnitTest {

    public static final int ID = 42;

    @Test
    public void reserveAUnit_should_returnTheReservedQuantity(){
        int quantity = 1;
        assertThat(unit(quantity).reserve(quantity), is(quantity));
    }

    @Test
    public void reserveAUnit_should_notHaveThisUnitInStock(){
        TradingUnit unit = unit(1);
        unit.reserve(1);
        assertThat(unit.getInStock(),is(0));
    }

    @Test
    public void reserveAUnit_should_haveThisUnitInBasket(){
        TradingUnit unit = unit(1);
        unit.reserve(1);
        assertThat(unit.getInBasket(),is(1));
    }

    @Test
    public void reserveMoreThanExist_should_returnActuallyReserved(){
        assertThat(unit(3).reserve(5),is(3));
    }

    @Test
    public void releaseAUnit_should_returnTheReleasedUnit(){
        assertThat(reservedUnit(1).release(1), is(1));
    }

    @Test
    public void releaseUnit_should_haveTheQuantityInStock(){
        TradingUnit unit = reservedUnit(1);
        unit.release(1);
        assertThat(unit.getInStock(),is(1));
    }

    @Test
    public void releaseUnit_should_notHaveTheQuantityInBasket(){
        TradingUnit unit = reservedUnit(1);
        unit.release(1);
        assertThat(unit.getInBasket(),is(0));
    }

    @Test
    public void releaseMoreThanExist_should_releaseExisting(){
        assertThat(reservedUnit(3).release(5),is(3));
    }

    private TradingUnit reservedUnit(final int quantity) {
        TradingUnit unit = unit(quantity);
        unit.reserve(quantity);
        return unit;
    }

    private TradingUnit unit(final int quantity) {
        return new TradingUnit(ID, quantity);
    }
}
