package net.bellrise.android.shop;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.bellrise.android.shop.views.About;
import net.bellrise.android.shop.views.Order;
import net.bellrise.android.shop.views.OrderList;
import net.bellrise.android.shop.views.SaveSettings;
import net.bellrise.android.shop.views.SendSms;
import net.bellrise.android.shop.views.Share;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity
{
    /* Used in onOptionsItemSelected. */
    private static HashMap<Integer, Class<?>> views;

    public BaseActivity()
    {
        views = new HashMap<>();
        views.put(R.id.menu_order, Order.class);
        views.put(R.id.menu_order_list, OrderList.class);
        views.put(R.id.menu_send_sms, SendSms.class);
        views.put(R.id.menu_share, Share.class);
        views.put(R.id.menu_save_settings, SaveSettings.class);
        views.put(R.id.menu_about, About.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        for (int key : views.keySet()) {
            if (item.getItemId() != key)
                continue;

            /* We have found a match, launch that activity. */
            Intent a = new Intent(this, views.get(key));
            startActivity(a);
            return true;
        }

        return false;
    }
}
