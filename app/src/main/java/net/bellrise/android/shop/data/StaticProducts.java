package net.bellrise.android.shop.data;

import net.bellrise.android.shop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class contains all the statically-available products,
 * before we move into a better & more stable solution - using
 * a database to store all data. For now, just use the get()
 * and getAll() methods to access it.
 */
public class StaticProducts
{
    public enum Category
    {
        BOX,
        MOUSE,
        KEYBOARD,
        WEBCAM,
        MONITOR
    }

    private final HashMap<Category, ArrayList<Product>> products;

    public StaticProducts()
    {
        /* Create the static products. I'm sorry for this mess,
           I know this is disgusting. */
        products = new HashMap<>();

        add(Category.BOX, "Dell Vostro", "3910 MT i5-12400/16GB/512/Win11P", R.drawable.dell_vostro, 3349);
        add(Category.BOX, "HP Pavilion Gaming", "R5/16GB/512/Win10 GTX1650 Super", R.drawable.hp_pavilion_gaming, 3749);
        add(Category.BOX, "Dell Inspiron", "3910 i5-12400/8GB/512/Win11", R.drawable.dell_inspiron, 3599);
        add(Category.BOX, "ASUS ExpertCenter", "D500TC i5-11400/16GB/480/Win10P", R.drawable.asus_expertcenter, 2999);

        add(Category.MOUSE, "SteelSeries Prime", "18000dpi", R.drawable.steelseries_prime, 99);
        add(Category.MOUSE, "Dell MS116 Prime", "1000dpi", R.drawable.dell_ms116, 44);
        add(Category.MOUSE, "Logitech G102", "8000dpi", R.drawable.logitech_g102, 149);

        add(Category.KEYBOARD, "Logitech MK270", "Wireless", R.drawable.logitech_mk270, 139);
        add(Category.KEYBOARD, "Keychron K3", "Wireless RGB", R.drawable.keychron_k3, 469);
        add(Category.KEYBOARD, "Silver Monkey K90m", "Wireless Membrane", R.drawable.silver_monkey_k90m, 90);

        add(Category.WEBCAM, "Logitech C920", "+Microphone FullHD", R.drawable.logitech_c920, 449);

        add(Category.MONITOR, "Acer SB271BMIX", "27\" 1920x1080", R.drawable.acer_sb271bmix, 679);
        add(Category.MONITOR, "HP V24i", "23.8\" 1920x1080", R.drawable.hp_v24i, 579);
    }

    public int size(Category cat)
    {
        return Objects.requireNonNull(products.get(cat)).size();
    }

    public Product get(Category cat, int i)
    {
        return Objects.requireNonNull(products.get(cat)).get(i);
    }

    public ArrayList<Product> getAll(Category cat)
    {
        return products.get(cat);
    }

    private void add(Category cat, String name, String desc, int image_id, double price)
    {
        if (!products.containsKey(cat))
            products.put(cat, new ArrayList<>());
        Objects.requireNonNull(products.get(cat))
                .add(new Product(name, desc, image_id, price));
    }
}
