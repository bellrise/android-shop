package net.bellrise.android.shop;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.bellrise.android.shop.views.About;
import net.bellrise.android.shop.views.AddUser;
import net.bellrise.android.shop.views.Login;
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
        views.put(R.id.menu_add_user, AddUser.class);
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
        /* Check if the user wants to log out. */
        if (item.getItemId() == R.id.menu_log_out) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

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
