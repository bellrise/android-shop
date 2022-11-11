package net.bellrise.android.shop.data;

import net.bellrise.android.shop.R;

import java.security.Policy;
import java.util.ArrayList;

/**
 * This class contains all the statically-available products,
 * before we move into a better & more stable solution - using
 * a database to store all data. For now, just use .products
 * to access it.
 */
public class StaticProducts
{
    public final ArrayList<Product> self;

    public StaticProducts()
    {
        /* Create the static products. */
        self = new ArrayList<>();
        addProduct("Dell Vostro", "3910 MT i5-12400/16GB/512/Win11P",
                R.drawable.dell_vostro, 3349);
        addProduct("HP Pavilion Gaming", "R5/16GB/512/Win10 GTX1650 Super",
                R.drawable.hp_pavilion_gaming, 3749);
        addProduct("Dell Inspiron", "3910 i5-12400/8GB/512/Win11",
                R.drawable.dell_inspiron, 3599);
        addProduct("ASUS ExpertCenter", "D500TC i5-11400/16GB/480/Win10P",
                R.drawable.asus_expertcenter, 2999);
    }

    private void addProduct(String name, String desc, int image_id, double price)
    {
        self.add(new Product(name, desc, image_id, price));
    }
}
