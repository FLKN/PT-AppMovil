package com.ptappmovil.upiita.pt_appmovil.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptappmovil.upiita.pt_appmovil.Items.SensorItem;
import com.ptappmovil.upiita.pt_appmovil.Items.ServiceItem;
import com.ptappmovil.upiita.pt_appmovil.R;

import java.util.List;

public class ServiceAdapter extends BaseAdapter{

    private Context context;
    private List<ServiceItem> items;

    public ServiceAdapter(Context context, List<ServiceItem> items) {
        this.context = context;
        this.items = items;
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
            rowView = inflater.inflate(R.layout.list_services, parent, false);
        }

        // Set data into the view.
        TextView dish_id = (TextView) rowView.findViewById(R.id.dish_id);
        TextView dish_name = (TextView) rowView.findViewById(R.id.dish_name);
        TextView dish_cost = (TextView) rowView.findViewById(R.id.dish_cost);

        ServiceItem item = this.items.get(position);

        dish_id.setText(item.getDish_id());
        dish_name.setText(item.getDish_name());
        dish_cost.setText(item.getDish_cost());

        return rowView;
    }
}
