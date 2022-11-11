package net.bellrise.android.shop.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.bellrise.android.shop.BaseActivity;
import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.SpinnerAdapter;


public class Order extends BaseActivity implements AdapterView.OnItemSelectedListener
{
    private int selected;
    private TextView pc_name;
    private TextView pc_desc;
    private ImageView pc_image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SpinnerAdapter adapter;
        Spinner spinner;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /* Init class vars */
        selected = 0;
        spinner = findViewById(R.id.spinner);
        pc_name = findViewById(R.id.pc_name);
        pc_desc = findViewById(R.id.pc_desc);
        pc_image = findViewById(R.id.pc_image);

        /* Set spinner adapter */
        adapter = new SpinnerAdapter(getApplicationContext());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        updateSelection();
    }

    private void updateSelection()
    {
        pc_name.setText(Global.products.self.get(selected).name);
        pc_desc.setText(Global.products.self.get(selected).desc);
        pc_image.setImageResource(Global.products.self.get(selected).image_id);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        selected = i;
        Log.d(Global.TAG, "onItemSelected: selected = " + i);
        updateSelection();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        selected = 0;
        updateSelection();
    }
}
