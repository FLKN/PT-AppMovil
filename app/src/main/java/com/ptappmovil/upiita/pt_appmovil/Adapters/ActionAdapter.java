package com.ptappmovil.upiita.pt_appmovil.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ptappmovil.upiita.pt_appmovil.Items.ActionItem;
import com.ptappmovil.upiita.pt_appmovil.R;

import java.util.List;

public class ActionAdapter extends BaseAdapter {

    private Context context;
    private List<ActionItem> items;

    public ActionAdapter(Context context, List<ActionItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() { return this.items.size(); }

    @Override
    public Object getItem(int position) { return this.items.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_actions, parent, false);
        }

        // Set data into the view.
        TextView action_icon = (TextView) rowView.findViewById(R.id.action_icon);
        TextView action_name = (TextView) rowView.findViewById(R.id.action_text);

        // Agregando FontAwesome
        Typeface fontawesome = Typeface.createFromAsset(rowView.getContext().getAssets(),"fonts/fontawesome.ttf");
        action_icon.setTypeface(fontawesome);

        ActionItem item = this.items.get(position);

        action_icon.setText(item.getAction_icon());
        action_name.setText(item.getAction_name());

        return rowView;
    }
}
