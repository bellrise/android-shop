package net.bellrise.android.shop.data;

public class Product
{
    public String name;
    public String desc;
    public int image_id;
    public double price;

    public Product(String name, String desc, int image_id, double price)
    {
        this.name = name;
        this.desc = desc;
        this.image_id = image_id;
        this.price = price;
    }
}
