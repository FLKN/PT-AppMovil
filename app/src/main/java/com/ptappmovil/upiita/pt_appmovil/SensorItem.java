package com.ptappmovil.upiita.pt_appmovil;

public class SensorItem {
    private int sensor_id;
    private String sensor_nombre;

    public SensorItem() {  }

    public SensorItem(String sensor_nombre, int sensor_id) {
        this.sensor_nombre = sensor_nombre;
        this.sensor_id = sensor_id;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public String getSensor_nombre() {
        return sensor_nombre;
    }

    public void setSensor_nombre(String sensor_nombre) {
        this.sensor_nombre = sensor_nombre;
    }
}
