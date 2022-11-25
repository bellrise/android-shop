package net.bellrise.android.shop.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.data.OrderDatabase;
import net.bellrise.android.shop.data.Product;
import net.bellrise.android.shop.data.StaticProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class OrderConfirm extends AppCompatActivity
{
    private static class CachedData
    {
        public String user_name;
        public String user_phone;
    }

    private static final CachedData cache = new CachedData();
    private double total_price = 0;

    /* Some helper data. */
    private static final String[] detail_names = {"mouse", "keyboard", "webcam", "monitor"};
    private static final StaticProducts.Category[] detail_cats = {StaticProducts.Category.MOUSE,
            StaticProducts.Category.KEYBOARD, StaticProducts.Category.WEBCAM,
            StaticProducts.Category.MONITOR};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        Bundle data = getIntent().getExtras();
        if (data == null) {
            Toast.makeText(this, getString(R.string.missing_detail_data), Toast.LENGTH_SHORT).show();
            return;
        }

        fillDetails(data);

        /* Confirm user data is filled before ordering. */
        EditText user_name, user_phone;
        Button button_order;

        user_name = findViewById(R.id.edit_user_name);
        user_phone = findViewById(R.id.edit_user_phone);
        button_order = findViewById(R.id.button_place_order);

        button_order.setOnClickListener(view -> {
            if (user_name.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.missing_name), Toast.LENGTH_SHORT).show();
                user_name.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                return;
            }

            if (user_phone.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.missing_phone_number), Toast.LENGTH_SHORT).show();
                user_phone.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                return;
            }

            placeOrder();
            sendEmail();
            sendSMS(user_phone.getText().toString());

            Toast.makeText(this, "Placed order!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * Store the order in the database & send an email.
     */
    private void placeOrder()
    {
        OrderDatabase db = new OrderDatabase(this);
        ContentValues vals = new ContentValues();
        EditText user_name, user_phone;

        user_name = findViewById(R.id.edit_user_name);
        user_phone = findViewById(R.id.edit_user_phone);

        vals.put(OrderDatabase.COL_USER, user_name.getText().toString());
        vals.put(OrderDatabase.COL_PHONE, user_phone.getText().toString());
        vals.put(OrderDatabase.COL_DATA, serializeOrder().toString());
        vals.put(OrderDatabase.COL_DATE, Calendar.getInstance().getTimeInMillis());

        db.getWritableDatabase().insert(OrderDatabase.TABLE_NAME, null, vals);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void sendEmail()
    {
        Intent mail = new Intent(Intent.ACTION_SENDTO);
        mail.setData(Uri.parse("mailto:"));
        mail.putExtra(Intent.EXTRA_EMAIL, new String[] {"example@example.com"});
        mail.putExtra(Intent.EXTRA_SUBJECT, "PowerStore " + getString(R.string.order));

        StringBuilder content;
        JSONObject root = serializeOrder();

        try {
            content = new StringBuilder();
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                content.append(String.format(Locale.getDefault(), "%s, %s %.2f zł\n",
                        item.getString("name"), getString(R.string.price),
                        item.getDouble("price")));
            }

            String date = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy"));
            content.append(String.format(Locale.getDefault(), "\n%s %s",
                    getString(R.string.ordered_at), date));
            content.append(String.format(Locale.getDefault(), "\n%s %.2f zł",
                    getString(R.string.total_price), total_price));

        } catch (JSONException e) {
            content = new StringBuilder(root.toString());
        }

        mail.putExtra(Intent.EXTRA_TEXT, content.toString());

        getPermission(Manifest.permission.INTERNET);
        startActivity(mail);
    }

    private void sendSMS(String addr)
    {
        SmsManager sms_manager;
        String text;
        Bundle data;
        int n_products;

        getPermission(Manifest.permission.SEND_SMS);

        data = getIntent().getExtras();
        n_products = 1;
        for (String detail_name : detail_names) {
            if (data.getInt(detail_name) > 0)
                n_products++;
        }

        text = String.format(Locale.getDefault(), "%s %s. %s %d %s",
                getString(R.string.placed_order_for), data.getString("username"),
                getString(R.string.bought), n_products, getString(R.string.products));

        sms_manager = SmsManager.getDefault();
        sms_manager.sendTextMessage(addr, null, text, null, null);
    }

    private void getPermission(String perm)
    {
        String[] perms = new String[] {perm};

        do {
            if (checkSelfPermission(perm) == PackageManager.PERMISSION_DENIED)
                requestPermissions(perms, 1);
        } while (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED);
    }

    private JSONObject serializeOrder()
    {
        JSONObject res = new JSONObject();
        JSONArray items = new JSONArray();
        Bundle data = getIntent().getExtras();
        JSONObject item;
        Product p;

        if (data == null)
            return res;

        try {
            /* Add the main box. */
            p = Global.products.get(StaticProducts.Category.BOX, data.getInt("box"));
            item = new JSONObject();

            item.put("name", p.name);
            item.put("desc", p.desc);
            item.put("price", p.price);
            items.put(item);

            /* Add addons to the item list. */
            for (int i = 0; i < detail_names.length; i++) {
                int selected = data.getInt(detail_names[i]);
                if (selected <= 0)
                    continue;

                p = Global.products.get(detail_cats[i], selected - 1);
                item = new JSONObject();

                /* Add a new product to the list. */
                item.put("name", p.name);
                item.put("desc", p.desc);
                item.put("price", p.price);
                items.put(item);
            }

            res.put("total_price", total_price);
            res.put("items", items);
            res.put("error", false);

        } catch (JSONException e) {
            Log.d(Global.TAG, "failed to serialze");
        }

        Log.i(Global.TAG, "serialized order: " + res);
        return res;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        EditText user_name = findViewById(R.id.edit_user_name);
        EditText user_phone = findViewById(R.id.edit_user_phone);
        cache.user_name = user_name.getText().toString();
        cache.user_phone = user_phone.getText().toString();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        EditText user_name = findViewById(R.id.edit_user_name);
        EditText user_phone = findViewById(R.id.edit_user_phone);

        Log.i(Global.TAG, "resuming OrderConfirm with cached data");

        if (cache.user_name != null)
            user_name.setText(cache.user_name);
        if (cache.user_phone != null)
            user_phone.setText(cache.user_phone);
    }

    private void fillDetails(Bundle data)
    {
        EditText username_input;
        TextView detail;
        int index;

        /* Possible username */
        username_input = findViewById(R.id.edit_user_name);
        username_input.setText(data.getString("username"));

        /* PC name */
        detail = findViewById(R.id.detail_box_value);
        detail.setText(Global.products.get(StaticProducts.Category.BOX,
                data.getInt("box")).name);

        int[] detail_ids = {R.id.detail_mouse_value, R.id.detail_keyboard_value,
                R.id.detail_webcam_value, R.id.detail_monitor_value};

        /* Set addon details */
        for (int i = 0; i < 4; i++) {
            detail = findViewById(detail_ids[i]);
            index = data.getInt(detail_names[i]);
            if (index > 0)
                detail.setText(Global.products.get(detail_cats[i], index - 1).name);
        }

        /* Set total price */
        detail = findViewById(R.id.detail_price_value);
        detail.setText(String.format(Locale.getDefault(), "%.2f zł",
                data.getDouble("total_price")));

        total_price = data.getDouble("total_price");
    }
}