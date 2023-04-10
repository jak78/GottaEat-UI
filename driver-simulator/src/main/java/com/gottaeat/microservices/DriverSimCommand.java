package com.gottaeat.microservices;

import com.gottaeat.microservices.driver.DriverSimulatorThread;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Command(name = "Driver Simulator", mixinStandardHelpOptions = true)
public class DriverSimCommand implements Runnable {

    ExecutorService executor =  Executors.newCachedThreadPool();

    @Parameters(paramLabel = "<Number of Drivers>", defaultValue = "8",
            description = "The number of drivers to simulate")
    String numberOfDrivers;

    @Parameters(paramLabel = "<Starting Latitude>", defaultValue = "48.8782",
            description = "The starting latitude for the drivers")
    String startingLat;

    @Parameters(paramLabel = "<Starting Longitude>", defaultValue = "2.2820",
            description = "The starting longitude for the drivers")
    String startingLon;

    @Inject
    @ConfigProperty(name = "location.webservice", defaultValue = "http://localhost:8080/location/driver")
    String webServiceEndpoint;
    @Inject
    @ConfigProperty(name = "reportInterval.ms", defaultValue = "10000")
    int reportInterval;

    @Override
    public void run() {
        System.out.printf("Starting %s drivers!\n", numberOfDrivers);
        for (int idx = 0; idx < Integer.parseInt(numberOfDrivers); idx++) {
            executor.submit(new DriverSimulatorThread(webServiceEndpoint,
                    reportInterval, idx,
                    Float.parseFloat(startingLat),
                    Float.parseFloat(startingLon)));
            try {
                Thread.sleep(1000);  // Stagger the threads.
            } catch (final InterruptedException e) {

            }
        }
    }

}
