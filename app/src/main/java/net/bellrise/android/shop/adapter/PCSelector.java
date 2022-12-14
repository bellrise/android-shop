package net.bellrise.android.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.data.StaticProducts;

public class PCSelector extends BaseAdapter
{
    private final Context context;

    public PCSelector(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return Global.products.size(StaticProducts.Category.BOX);
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

        /* Format the spinner element. */
        img.setImageResource(Global.products.get(StaticProducts.Category.BOX, i).image_id);
        text.setText(Global.products.get(StaticProducts.Category.BOX, i).name);

        return view;
    }
}
