package net.bellrise.android.shop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.data.OrderRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class OrderListAdapter extends ArrayAdapter<OrderRecord>
{
    public OrderListAdapter(Context context, ArrayList<OrderRecord> data)
    {
        super(context, R.layout.order_list_item, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        OrderRecord thing = getItem(position);

        if (convertView == null) {
            LayoutInflater inflate = LayoutInflater.from(getContext());
            convertView = inflate.inflate(R.layout.order_list_item, parent, false);

            TextView user = convertView.findViewById(R.id.order_list_item_user);
            TextView stuff = convertView.findViewById(R.id.order_list_item_stuff);

            StringBuilder builder = new StringBuilder();

            /* Parse the ordered items into a readable string. */
            try {
                JSONObject json = new JSONObject(thing.data);
                JSONArray items = json.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject single_order = items.getJSONObject(i);
                    builder.append(single_order.getString("name"))
                            .append(" - ")
                            .append(single_order.getDouble("price"))
                            .append("zł\n");
                }

                builder.append("\n").append(getContext().getString(R.string.total_price))
                        .append(" ").append(json.getDouble("total_price"))
                        .append("zł");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i(Global.TAG, "getView: " + thing.timestamp);
            Instant t = Instant.ofEpochMilli(thing.timestamp);
            LocalDateTime ldt = LocalDateTime.ofInstant(t, ZoneId.systemDefault());
            String date = ldt.format(DateTimeFormatter.ofPattern("d.M HH:mm:ss"));

            user.setText(String.format(Locale.getDefault(), "%s - %s (%s)",
                    thing.name, thing.phone_number, date));
            stuff.setText(builder.toString());
        }

        return convertView;
    }
}
