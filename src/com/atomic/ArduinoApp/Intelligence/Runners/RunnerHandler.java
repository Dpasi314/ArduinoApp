package com.atomic.ArduinoApp.Intelligence.Runners;

import com.atomic.ArduinoApp.Values.Action;
import com.atomic.ArduinoApp.Values.RunnerType;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;
/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class RunnerHandler {

    private Action action = null;
    private RunnerType type = null;
    private Object values = null;

    private static List<Runner> liveRunners = new ArrayList<>();
    public RunnerHandler(Action action, RunnerType type, Object... values) {
        this.action = action;
        this.type = type;
        this.values = values;
    }

    public void create() {
        Runner r = new Runner("action-" + generateID(), type, values);
        this.addRunner(r);
    }

    private void addRunner(Runner runner) {
        liveRunners.add(runner);
    }

    private String generateID() {
        Random r = new Random();
        Integer[] i = new Integer[4];

        for(int j = 0; j < 4; j++) {
            i[j] = r.nextInt(10);
        }

        return i[0] + "" + i[1] + "" + i[2] + "" + i[3];
    }
}
