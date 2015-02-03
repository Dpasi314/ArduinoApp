package com.atomic.ArduinoApp.Intelligence;

import com.atomic.ArduinoApp.Values.Action;

/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class IntelligenceCore {
    private Confidence confidence = null;
    private Action action = null;

    public IntelligenceCore(Action action) {
        this.action = action;
    }

    public void scoreConfidence() {
        confidence = Confidence.getConfidence(action);
    }

    public float getScore() {
        return confidence.getScore();
    }

    public Action getAction() {
        return action;
    }
}
