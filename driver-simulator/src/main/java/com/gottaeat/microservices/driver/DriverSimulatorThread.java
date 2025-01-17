package com.gottaeat.microservices.driver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gottaeat.microservices.location.driver.domain.DriverPositionSignal;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;


public class DriverSimulatorThread implements Runnable {

    private final int PARK_PROBABILITY = 5;
    private final int MOVEMENT_FACTOR = 800;

    private final Logger LOGGER = LoggerFactory.getLogger(DriverSimulatorThread.class);

    final Random rnd = new Random();
    final ObjectMapper mapper = new ObjectMapper();

    final String webServiceEndpoint;
    final int reportInterval;
    final long driverId;
    final DriverPositionSignal START;

    public DriverSimulatorThread(String webServiceEndpoint, int interval, long driverId, float lat, float lon) {
        this.webServiceEndpoint = webServiceEndpoint;
        this.reportInterval = interval;
        this.driverId = driverId;
        this.START = new DriverPositionSignal(lat, lon, System.currentTimeMillis(), 0);
    }

    @Override
    public void run() {
        DriverPositionSignal prev = this.START;
        while (true) {
            try {
                DriverPositionSignal update = nextSignal(prev);
                sendSignal(update);
                prev = update;
                int sleepMs = this.reportInterval + rnd.nextInt(this.reportInterval / 4);
                Thread.sleep(sleepMs);
            } catch (Exception e) {
                LOGGER.error("Car crashed!", e);
            }
        }
    }

    private void sendSignal(DriverPositionSignal signal) throws IOException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            StringEntity entity = new StringEntity(mapper.writeValueAsString(signal), ContentType.APPLICATION_JSON);
            HttpPost httpPost = new HttpPost(webServiceEndpoint);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);

            if (response.getCode() > 400) {
                LOGGER.error("Response code " + response.getCode());
            }
        }

    }

    private DriverPositionSignal nextSignal(DriverPositionSignal prev) {
        DriverPositionSignal signal = new DriverPositionSignal();
        signal.driverId = this.driverId;
        signal.timestamp = System.currentTimeMillis();

        if (rnd.nextInt(100) < PARK_PROBABILITY) { // Chance that the car "parks"
            signal.latitude = prev.latitude;
            signal.longitude = prev.longitude;
        } else {
            signal.latitude = prev.latitude + (rnd.nextFloat() / MOVEMENT_FACTOR * (rnd.nextBoolean() ? 1 : -1));
            signal.longitude = prev.longitude + (rnd.nextFloat() / MOVEMENT_FACTOR * (rnd.nextBoolean() ? 1 : -1));
        }
        return signal;
    }
}
