package com.ptappmovil.upiita.pt_appmovil;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SensorAdapter extends BaseAdapter {

    private Context context;
    private List<SensorItem> items;

    public SensorAdapter(List<SensorItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_sensores, parent, false);
        }

        // Set data into the view.
        TextView sensor_icon = (TextView) rowView.findViewById(R.id.sensor_icon);
        TextView sensor_nombre = (TextView) rowView.findViewById(R.id.sensor_text);

        // Agregando FontAwesome
        Typeface fontawesome = Typeface.createFromAsset(rowView.getContext().getAssets(),"fonts/fontawesome.ttf");
        sensor_icon.setTypeface(fontawesome);

        SensorItem item = this.items.get(position);

        int sensor_id = item.getSensor_id();
        switch (sensor_id){
            // Luz
            case 1: sensor_icon.setText("\uf0eb");
                break;
            // Cerradura
            case 2: sensor_icon.setText("\uf074");
                break;
            // Aire
            case 3: sensor_icon.setText("\uf185");
                break;
            // Accesos
            case 4: sensor_icon.setText("\uf1ad");
                break;
        }
        sensor_nombre.setText(item.getSensor_nombre());


        return rowView;
    }
}
