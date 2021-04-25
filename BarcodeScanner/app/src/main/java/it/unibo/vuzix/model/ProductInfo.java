package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductInfo implements Parcelable {

    private int idOrder;
    private int quantity;
    private boolean picked = false;

    /**
     * Constructor and method of Parcelable
     */

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel parcel) {
            return new ProductInfo(parcel);
        }

        @Override
        public ProductInfo[] newArray(int i) {
            return new ProductInfo[i];
        }
    };

    public ProductInfo(){

    }

    protected ProductInfo(Parcel in) {
        idOrder = in.readInt();
        quantity = in.readInt();
        picked = in.readByte() != 0;
    }

    public static ProductInfo from(JSONObject jsonObject){
        ProductInfo productInfo = new ProductInfo();
        try {
            productInfo.setIdOrder(jsonObject.getInt("id"));
            productInfo.setQuantity(jsonObject.getJSONObject("items").getInt("qta"));
            productInfo.setPicked(false); //TODO serve?!
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return productInfo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idOrder);
        dest.writeInt(quantity);
        dest.writeByte((byte) (picked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }


    /*
     * Getter e Setter
     */

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    /**
     * toString
     */

    @Override
    public String toString() {
        return "ProductInfo{" +
                "idOrder=" + idOrder +
                ", quantity=" + quantity +
                ", picked=" + picked +
                '}';
    }
}
