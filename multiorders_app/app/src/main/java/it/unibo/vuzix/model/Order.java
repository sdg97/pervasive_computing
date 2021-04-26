package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Order  implements Parcelable {

    private List<Product> products = new ArrayList<>();

    /**
     * Constructor and method of Parcelable
     */
    public Order(){    }

    protected Order(Parcel in) {
        in.readTypedList(products, Product.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.products);
    }

    /**
     * Getter and Setter
     */

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "Order{" +
                "products=" + products +
                '}';
    }
}
