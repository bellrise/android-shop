package net.bellrise.android.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.bellrise.android.shop.R;
import net.bellrise.android.shop.data.Product;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Dynamic addon selector. You can choose a product array to display, along
 * with a <none> selected possbility.
 */
public class AddonSelector extends BaseAdapter
{
    private final ArrayList<Product> products;
    private final Context context;

    public AddonSelector(Context context, ArrayList<Product> products)
    {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount()
    {
        return products.size() + 1;
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

        if (i == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.empty_spinner_item, null);
            return view;
        }

        view = LayoutInflater.from(context).inflate(R.layout.spinner_item, null);
        img = view.findViewById(R.id.spinner_image);
        text = view.findViewById(R.id.spinner_text);

        img.setImageResource(products.get(i - 1).image_id);
        text.setText(String.format(Locale.getDefault(), "%s, %s %.0f zł",
                products.get(i - 1).name, context.getString(R.string.price),
                products.get(i - 1).price));

        return view;
    }
}
