package net.bellrise.android.shop.data;

public class OrderRecord
{
    public String name;
    public String phone_number;
    public String data;
    public long timestamp;

    public OrderRecord(String name, String phone_number, String data, long timestamp)
    {
        this.name = name;
        this.phone_number = phone_number;
        this.data = data;
        this.timestamp = timestamp;
    }
}
