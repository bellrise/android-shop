package net.bellrise.android.shop.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.bellrise.android.shop.AddonCallback;
import net.bellrise.android.shop.BaseActivity;
import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.adapter.AddonSelector;
import net.bellrise.android.shop.adapter.PCSelector;
import net.bellrise.android.shop.data.StaticProducts;

import java.util.Locale;


public class Order extends BaseActivity implements AdapterView.OnItemSelectedListener
{
    private static class Cache
    {
        public int box_selected;
        public int[] addons_selected;
        public String username;
    }

    /* Store some cached state about the program state, for user
       experience, meaning the settings will be kept intact if
       they want to return to the order page. */
    private static Cache cache;

    private int selected;
    private double price;
    private String username;
    private TextView pc_name;
    private TextView pc_desc;
    private TextView pc_price;
    private TextView total_price;
    private ImageView pc_image;
    private AddonSelector[] addon_selectors;
    private AddonCallback[] addon_callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        PCSelector pc_selector;
        Spinner pc_spinner;
        Button order_button;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /* Init class vars */
        selected = 0;
        pc_spinner = findViewById(R.id.spinner);
        pc_name = findViewById(R.id.pc_name);
        pc_desc = findViewById(R.id.pc_desc);
        pc_image = findViewById(R.id.pc_image);
        pc_price = findViewById(R.id.pc_price);
        total_price = findViewById(R.id.total_price);
        order_button = findViewById(R.id.button_order);
        addon_selectors = new AddonSelector[4];
        addon_callbacks = new AddonCallback[4];

        /* Get logged username from extras. */
        Bundle data = getIntent().getExtras();
        username = data.isEmpty() ? "" : data.getString("username");

        /* Set spinner adapter */
        pc_selector = new PCSelector(getApplicationContext());
        pc_spinner.setAdapter(pc_selector);
        pc_spinner.setOnItemSelectedListener(this);

        order_button.setOnClickListener(view -> {
            Intent x = new Intent(this, OrderConfirm.class);

            x.putExtra("total_price", price);
            x.putExtra("box", selected);
            x.putExtra("mouse", addon_callbacks[0].selected);
            x.putExtra("keyboard", addon_callbacks[1].selected);
            x.putExtra("webcam", addon_callbacks[2].selected);
            x.putExtra("monitor", addon_callbacks[3].selected);
            x.putExtra("username", username);

            startActivity(x);

            Log.i(Global.TAG, "confirmed order");
        });

        setAddonSpinners();
        updateSelection();
    }

    private void setAddonSpinners()
    {
        Spinner[] spins;

        spins = new Spinner[] {
                findViewById(R.id.spinner_addon_mouse),
                findViewById(R.id.spinner_addon_keyboard),
                findViewById(R.id.spinner_addon_webcam),
                findViewById(R.id.spinner_addon_monitor)
        };

        StaticProducts.Category[] cats = {
            StaticProducts.Category.MOUSE,
            StaticProducts.Category.KEYBOARD,
            StaticProducts.Category.WEBCAM,
            StaticProducts.Category.MONITOR
        };

        for (int i = 0; i < spins.length; i++) {
            addon_callbacks[i] = new AddonCallback();
            addon_callbacks[i].callback = _unused -> updateTotalPrice();
            addon_callbacks[i].category = cats[i];

            addon_selectors[i] = new AddonSelector(getApplicationContext(),
                    Global.products.getAll(cats[i]));
            spins[i].setAdapter(addon_selectors[i]);
            spins[i].setOnItemSelectedListener(addon_callbacks[i]);
        }
    }

    private void updateSelection()
    {
        String name, desc, price;
        int image;

        name = Global.products.get(StaticProducts.Category.BOX, selected).name;
        desc = Global.products.get(StaticProducts.Category.BOX, selected).desc;
        image = Global.products.get(StaticProducts.Category.BOX, selected).image_id;
        price = String.format(Locale.getDefault(), "%.0f zł",
                Global.products.get(StaticProducts.Category.BOX, selected).price);

        pc_name.setText(name);
        pc_desc.setText(desc);
        pc_image.setImageResource(image);
        pc_price.setText(price);
    }

    private void updateTotalPrice()
    {
        price = Global.products.get(StaticProducts.Category.BOX, selected).price;

        /* Get prices from addons. */
        for (AddonCallback addon_callback : addon_callbacks) {
            if (addon_callback.selected <= 0)
                continue;
            price += Global.products.get(addon_callback.category,
                    addon_callback.selected - 1).price;
        }

        total_price.setText(String.format(Locale.getDefault(), "%.2f zł", price));
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        cache = new Cache();
        cache.box_selected = selected;
        cache.addons_selected = new int[4];
        cache.username = username;

        for (int i = 0; i < 4; i++)
            cache.addons_selected[i] = addon_callbacks[i].selected;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (cache == null)
            return;

        Log.i(Global.TAG, "resuming Order with cached data");

        username = cache.username;
        selected = cache.box_selected;
        ((Spinner) findViewById(R.id.spinner)).setSelection(selected);

        final int[] ids = {R.id.spinner_addon_mouse, R.id.spinner_addon_keyboard,
                R.id.spinner_addon_webcam, R.id.spinner_addon_monitor};

        for (int i = 0; i < 4; i++)
            ((Spinner) findViewById(ids[i])).setSelection(cache.addons_selected[i]);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        selected = i;
        updateSelection();
        updateTotalPrice();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        selected = 0;
        updateSelection();
    }
}
