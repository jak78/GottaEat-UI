package com.gottaeat.microservices.location.driver.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DriverLocation implements Comparable<DriverLocation> {

    @NotNull
    public float latitude;

    @NotNull
    public float longitude;

    @NotNull
    public long timestamp;

    @Min(1)
    public long driverId;

    public String gridId;

    public DriverLocation() {
    }

    public DriverLocation(DriverPositionSignal signal) {
        this.latitude = signal.latitude;
        this.longitude = signal.longitude;
        this.driverId = signal.driverId;
        this.timestamp = signal.timestamp;
    }

    @Override
    public int compareTo(DriverLocation o) {
        return Long.compare(this.timestamp, o.timestamp);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DriverLocation{");
        sb.append("latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", driverId=").append(driverId);
        sb.append(", gridId='").append(gridId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
