package com.nklabs.debugmodule;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Lm75bDriver {

    // To be removed
    Random random = new Random();

    BlockingQueue<Integer> temperatureQueue;
    TemperatureProducerThread temperatureProducerThread;
    Thread thread;

    private final byte TEMP_REGISTER      = 0x00;
    private final byte CONF_REGISTER      = 0x01;
    private final byte TOS_REGISTER       = 0x02;
    private final byte THYST_REGISTER     = 0x03;

    private byte slaveAddress = 0x48; // 1 0 0 1 A2 A1 A0
    private int temperature;

    public enum Lm75bState {
        NORMAL, SHUTDOWN, OS_COMPARATOR, OS_INTERRUPT, OS_ACTIVE_LOW, OS_ACTIVE_HIGH
    }
    private Lm75bState lm75bRunState;


    Lm75bDriver(byte deviceAddressPins) {

        byte SLAVE_ADDRESS_MASK = 0x07;
        slaveAddress |= (deviceAddressPins & SLAVE_ADDRESS_MASK);
        this.setRunMode(Lm75bState.NORMAL);
        this.setOverTemperatureShutdownMode(Lm75bState.OS_COMPARATOR);
        this.setOverTemperatureShutdownPolarity(Lm75bState.OS_ACTIVE_LOW);
        this.setOverTemperatureShutdownThreshold(80);
        this.setHysteresisRegister(75);

        temperatureQueue = new ArrayBlockingQueue<Integer>(25);
        temperatureProducerThread = new TemperatureProducerThread(temperatureQueue);
        thread = new Thread(temperatureProducerThread);
    }

    Lm75bDriver() {

    }

    public void runTemperatureProducer() {
        thread.start();
    }
    public void stopTemperatureProducer() {
        temperatureProducerThread.stop();
        //thread.isAlive()
    }

    public byte getSlaveAddress() {
        return slaveAddress;
    }

    public void setSlaveAddress(byte slaveAddress) {
        this.slaveAddress = slaveAddress;
    }

    public int getTemperature() {

        // I2C slave select
        // I2C register read of TEMP register
        // convert 2 bytes to int
        temperature = random.nextInt(100);
        return temperature;
    }

    public void setRunMode(Lm75bState state) {

        if( state == Lm75bState.NORMAL) {
            // I2C slave select
            // Set !shutdown bit to CONF reg
            // Write to i2c device
        } else if (state == Lm75bState.SHUTDOWN) {
            // I2C slave select
            // Set shutdown bit to CONF reg
            // Write to i2c device
        }
        else {

        }
    }
    public void setOverTemperatureShutdownMode(Lm75bState state) {

        if(state == Lm75bState.OS_COMPARATOR) {

        } else if (state == Lm75bState.OS_INTERRUPT) {

        }
        else {

        }
    }

    public void setOverTemperatureShutdownPolarity(Lm75bState state) {

        if(state == Lm75bState.OS_ACTIVE_LOW) {

        } else if (state == Lm75bState.OS_ACTIVE_HIGH) {

        }
        else {

        }

    }

    public boolean isShutDown() {
        // I2C slave select
        // read shutdown bit to CONF reg
        // read to i2c device
        return false;
    }

    public void setOverTemperatureShutdownThreshold(int temp) {
        // I2C slave select
        // write TOS with temp value
    }

    public int getOverTemperatureShutdownThreshold() {
        // I2C slave select
        // read TOS with temp value
        return 80;
    }

    public void setHysteresisRegister(int thyst) {
        // I2C slave select
        // write Thyst with temp value
    }

    public int getHysteresisRegister() {

        return 75;
    }


    class TemperatureProducerThread implements Runnable {

        private BlockingQueue<Integer> temperatureQueue;
        private boolean loop = false;

        TemperatureProducerThread(BlockingQueue<Integer> queue) {
            this.temperatureQueue = queue;
        }
        void stop() {
            this.loop = false;
        }
        @Override
        public void run() {
            while(loop) {
                try {
                    Random random = new Random();
                    temperatureQueue.put(random.nextInt(45));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }
    }
}
