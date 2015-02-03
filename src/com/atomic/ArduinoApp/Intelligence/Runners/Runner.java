package com.atomic.ArduinoApp.Intelligence.Runners;

import com.atomic.ArduinoApp.Values.RunnerType;

/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class Runner implements Runnable {

    private String name = null;

    public Runner(String name, RunnerType type, Object... values) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return this.getID();
    }

    public void run() {

    }
}
