package com.gottaeat.microservices.location.driver.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DriverPositionSignal implements Comparable<DriverPositionSignal> {

    @NotNull
    public float latitude;

    @NotNull
    public float longitude;

    @NotNull
    public long timestamp;

    @Min(1)
    public long driverId;

    public DriverPositionSignal() {
    }

    public DriverPositionSignal(float lat, float lon, long ts, long driverId) {
        this.latitude = lat;
        this.longitude = lon;
        this.driverId = driverId;
        this.timestamp = ts;
    }

    @Override
    public int compareTo(DriverPositionSignal o) {
        return Long.compare(this.timestamp, o.timestamp);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DriverPositionSignal{");
        sb.append("latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", driverId=").append(driverId);
        sb.append('}');
        return sb.toString();
    }
}
