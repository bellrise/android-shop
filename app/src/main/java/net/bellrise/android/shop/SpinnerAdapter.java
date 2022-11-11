package net.bellrise.android.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter
{
    private final Context context;

    public SpinnerAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return Global.products.self.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ImageView img;
        TextView text;

        view = LayoutInflater.from(context).inflate(R.layout.spinner_item, null);
        img = view.findViewById(R.id.spinner_image);
        text = view.findViewById(R.id.spinner_text);

        img.setImageResource(Global.products.self.get(i).image_id);
        text.setText(Global.products.self.get(i).name);

        return view;
    }
}
