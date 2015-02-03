package com.atomic.ArduinoApp.Handlers;


import com.atomic.ArduinoApp.BaseActivity;
import com.atomic.ArduinoApp.Values.Action;

import java.sql.Timestamp;
import java.util.Random;

/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class DatabaseHelper {

    public enum Type {
        CONFIG,
        TIMESTAMP;

        public static String parse(Type type) {
            if(type == CONFIG)
                return "config";

            if(type == TIMESTAMP)
                return "timestamp";

            return null;
        }
    }

    public String serialize(String action) {
        DBTimestamp stamp = new DBTimestamp(action);
        return stamp.toString();
    }

    public void addRandomValues() {
        DatabaseHandler db = BaseActivity.getDatabaseHandler();
        String[] actions = {Action.APPLIANCE_USE.toString(), Action.MOTION.toString(), Action.HEAT_ADJUST.toString(),
                            Action.LIGHTS_DISBALED.toString(), Action.LIGHTS_ENABLED.toString()};
        Random r = new Random();
        for(int i = 0; i < 50; i++) {
            int j = r.nextInt(actions.length);
            db.add(Type.TIMESTAMP, String.valueOf(i), this.serialize(actions[j]), actions[j]);
        }
    }

    private class DBTimestamp {
        String action;
        long time = -1L;
        Timestamp timestamp = null;

        public DBTimestamp(String action) {
            this.action = action;
            time = System.currentTimeMillis();
            timestamp = new Timestamp(time);
        }
        @Override
        public String toString() {
            return "[" + timestamp + "]:[" + action + "]";
        }
    }
}
