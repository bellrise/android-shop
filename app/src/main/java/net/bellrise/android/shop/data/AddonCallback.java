package net.bellrise.android.shop.data;

import android.view.View;
import android.widget.AdapterView;

import java.util.function.Consumer;

/**
 * This class stores the selected element index of a addon spinner.
 */
public class AddonCallback implements AdapterView.OnItemSelectedListener
{
    public int selected = -1;
    public Consumer<Void> callback;
    public StaticProducts.Category category;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        selected = i;
        if (callback != null)
            callback.accept(null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        selected = -1;
    }
}
