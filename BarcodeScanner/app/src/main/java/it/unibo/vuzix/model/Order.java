package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Order  implements Parcelable {

    private List<Product> products; //TODO

    private Order(){

    }

    protected Order(Parcel in) {
        this.products = new ArrayList<>();
        in.readList(products, ProductInfo.class.getClassLoader());
    }

    //metodo che fa il parse di un JSONObject ; costruisce e restituisce il prodotto
    // in base al JSONObject passato in ingresso
    //TODO
    public static Order from(JSONObject jsonObject){
        Order product = new Order();
        //In base al Json restituito dalla chiamata
        //http://it2.life365.eu/api/order/idOrder?jwt=...

        return product;
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
}
