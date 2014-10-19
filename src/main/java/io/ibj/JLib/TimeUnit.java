package io.ibj.JLib;

/**
 * Created by Joe on 6/30/2014.
 */
public enum TimeUnit {
    MILLISECONDS(1,1D/50D),
    TICKS(50,1),
    SECONDS(1000, 20),
    MINUTES(60000, 1200),
    HOURS(60*60*1000, 60*60*20),
    DAYS(24*60*60*1000, 24*60*60*20),
    WEEKS(7*24*60*60*1000, 7*24*60*60*20),
    YEARS(365*24*60*60*1000, 365*24*60*60*20);

    TimeUnit(long ms, double ticks){
        this.ms = ms;
        this.ticks = ticks;
    }

    private long ms;
    private double ticks;

    public long convertToMs(double value){
        return (long) (ms*value);
    }

    public long convertToTicks(double value){
        return (int) (ticks*value);
    }
}
