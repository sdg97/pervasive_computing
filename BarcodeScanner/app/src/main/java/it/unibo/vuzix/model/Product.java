package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Interface for classes whose instances can be written to and restored from a Parcel.
// TODO https://www.vogella.com/tutorials/AndroidParcelable/article.html
//TODO https://guides.codepath.com/android/using-parcelable
public class Product implements Parcelable {
    //TODO tutte le info sono contenute in item a parte la lista di productInfo
    private int id;
    private String codicesenza;
    private String warehousePlace;
    private ProductInfo productInfo; //TODO

    public Product(){

    }

    protected Product(Parcel in) {
        this.codicesenza = in.readString();
        this.warehousePlace = in.readString();
        this.id = in.readInt();
        /*this.productInfo = new ArrayList<>();
        in.readList(productInfo, ProductInfo.class.getClassLoader());*/
        this.productInfo = new ProductInfo();
    }

    //metodo che fa il parse di un JSONObject ; costruisce e restituisce il prodotto
    // in base al JSONObject passato in ingresso
    //TODO
    public static List<Product> from(JSONObject jsonObject){
        List<Product> products = new ArrayList<>();
        try {
            //In base al Json restituito dalla chiamata
            //http://it2.life365.eu/api/order/idOrder?jwt=...

            for (int i = 0; i < jsonObject.getJSONArray("items").length(); i++) {
                Product product = new Product();
                ProductInfo productInfo1 = new ProductInfo();
                productInfo1.setIdOrder(jsonObject.getInt("id"));

                product.setId(jsonObject.getJSONArray("items").getJSONObject(i).getInt("id"));
                product.setWarehousePlace(jsonObject.getJSONArray("items").getJSONObject(i).getString("warehouse_place"));
                product.setCodicesenza(jsonObject.getJSONArray("items").getJSONObject(i).getString("Codicesenza"));
                productInfo1.setQuantity(jsonObject.getJSONArray("items").getJSONObject(i).getInt("qta"));
                product.setProductInfo(productInfo1);

                products.add(product);
            }

            /*
            *  product.setId(jsonObject.getJSONObject("items").getInt("id"));
            product.setWarehousePlace(jsonObject.getJSONObject("items").getString("warehouse_place"));
            product.setCodicesenza(jsonObject.getJSONObject("items").getString("Codicesenza"));
            productInfo1.setQuantity(jsonObject.getJSONObject("items").getInt("qta"));
            productInfo1.setIdOrder(jsonObject.getInt("id"));
            product.setProductInfo(productInfo1);
            * */

            //product.setId(jsonObject.getInt("id"));
            //product.setCodicesenza(jsonObject.getString("Codicesenza"));
            //product.setWarehousePlace(jsonObject.getString("warehouse_place"));
            //product.setListInfo(jsonObject.getJSONArray(""));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return products;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.codicesenza);
        parcel.writeString(this.warehousePlace);
        //parcel.writeTypedList(this.productInfo);
        //TODO ????? parcel.write
    }

    /***
     * Setter e getter
     */
    public void setCodicesenza(String codicesenza) {
        this.codicesenza = codicesenza;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setWarehousePlace(String warehousePlace) {
        this.warehousePlace = warehousePlace;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public String getCodicesenza() {
        return codicesenza;
    }

    public String getWarehousePlace() {
        return warehousePlace;
    }

    public int getId() {
        return id;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    //TODO
    public JSONObject toJson(){
        //N.B ATTENZIONE A DARE AI CAMPI LO STESSO NOME DEL jSON
        // ottenuto mediante la risposta

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("warehouse_place", getWarehousePlace());
            jsonObject.put("Codicesenza", getCodicesenza());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(codicesenza, product.codicesenza) &&
                Objects.equals(warehousePlace, product.warehousePlace) &&
                Objects.equals(productInfo, product.productInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicesenza, warehousePlace, id, productInfo);
    }
}
