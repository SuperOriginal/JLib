package io.ibj.JLib;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Joe on 6/30/2014.
 */
public class TimePeriod {

    public TimePeriod(double value, TimeUnit unit){
        this.value = value;
        this.unit = unit;
    }

    @Getter
    @Setter
    private Double value;
    @Getter
    @Setter
    private TimeUnit unit;

    public long getAsMs(){
        return unit.convertToMs(value);
    }

    public long getAsTicks(){
        return unit.convertToTicks(value);
    }
}
