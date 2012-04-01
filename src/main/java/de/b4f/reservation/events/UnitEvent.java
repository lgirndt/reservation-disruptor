package de.b4f.reservation.events;

import com.google.common.base.Objects;
import com.lmax.disruptor.EventFactory;

public class UnitEvent {

    private EventType type;
    private long unitId;
    private int operand;
    private int result;

    public UnitEvent(final EventType type, final long unitId, final int operand) {
        this.type = type;
        this.unitId = unitId;
        this.operand = operand;
        this.result = 0;
    }

    public void assignFrom(UnitEvent other){
        this.type = other.type;
        this.unitId = other.unitId;
        this.operand = other.operand;
        this.result = other.result;
    }

    public EventType getType() {
        return type;
    }

    public void setType(final EventType type) {
        this.type = type;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(final long unitId) {
        this.unitId = unitId;
    }

    public int getOperand() {
        return operand;
    }

    public void setOperand(final int operand) {
        this.operand = operand;
    }

    public int getResult() {
        return result;
    }

    public void setResult(final int result) {
        this.result = result;
    }

    public static EventFactory<UnitEvent> FACTORY = new EventFactory<UnitEvent>() {
        @Override
        public UnitEvent newInstance() {
            return new UnitEvent(EventType.NOOP,0l,0);
        }
    };


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type",type)
                .add("unitId",unitId)
                .add("operand",operand)
                .add("result",result)
                .toString();
    }
}
