package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductInfo implements Parcelable {

    private int idOrder;
    private int quantity;
    private boolean picked = false;

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
            //In base al Json restituito dalla chiamata
            //http://it2.life365.eu/api/order/idOrder?jwt=...
            productInfo.setIdOrder(jsonObject.getInt("id"));
            productInfo.setQuantity(jsonObject.getJSONObject("items").getInt("qta"));
            productInfo.setPicked(false); //TODO serve?!
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return productInfo;
    }
    //TODO
    public JSONObject toJson(){
        //N.B ATTENZIONE A DARE AI CAMPI LO STESSO NOME DEL jSON
        // ottenuto mediante la risposta

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getIdOrder());
            jsonObject.put("qta", getQuantity());
            jsonObject.put("picked", isPicked()); //TODO riguardare se serve e come viene usato
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel in) {
            return new ProductInfo(in);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };

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

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }


}
