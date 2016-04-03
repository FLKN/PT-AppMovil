package com.ptappmovil.upiita.pt_appmovil;

public class SensorItem {
    private int sensor_id;
    private String sensor_name;

    public SensorItem() {  }

    public SensorItem(String sensor_name, int sensor_id) {
        this.sensor_name = sensor_name;
        this.sensor_id = sensor_id;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }
}
