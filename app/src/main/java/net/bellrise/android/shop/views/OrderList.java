package net.bellrise.android.shop.views;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.bellrise.android.shop.BaseActivity;
import net.bellrise.android.shop.Global;
import net.bellrise.android.shop.R;
import net.bellrise.android.shop.adapter.OrderListAdapter;
import net.bellrise.android.shop.data.OrderDatabase;
import net.bellrise.android.shop.data.OrderRecord;

import java.util.ArrayList;

public class OrderList extends BaseActivity
{
    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_order_list);
        super.onCreate(savedInstanceState);

        ArrayList<OrderRecord> stuff;
        ArrayAdapter<?> adapter;
        ListView list;

        /* Read the oreders from the database. */
        OrderDatabase db = new OrderDatabase(this);
        Cursor res = db.getReadableDatabase().query(OrderDatabase.TABLE_NAME,
                new String[] {"user", "phone", "data"}, null, null, null, null, null);

        stuff = new ArrayList<>();
        while (res.moveToNext()) {
            stuff.add(new OrderRecord(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2)
            ));
        }

        Log.i(Global.TAG, "stuff.size(): " + stuff.size());

        list = findViewById(R.id.order_list);
        adapter = new OrderListAdapter(this, stuff);

        list.setAdapter(adapter);
    }
}
