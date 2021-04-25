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
    private String id;
    private String codicesenza;
    private String warehousePlace;
    private ProductInfo productInfo = new ProductInfo();

    public Product(){
    }

    protected Product(Parcel in) {
        this.id = in.readString();
        this.codicesenza = in.readString();
        this.warehousePlace = in.readString();
        this.productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.id);
        parcel.writeString(this.codicesenza);
        parcel.writeString(this.warehousePlace);
        parcel.writeParcelable(this.productInfo, flags);
    }

    //metodo che fa il parse di un JSONObject ; costruisce e restituisce il prodotto
    // in base al JSONObject passato in ingresso
    public static List<Product> from(JSONObject jsonObject){
        List<Product> products = new ArrayList<>();
        try {
            //In base al Json restituito dalla chiamata
            //http://it2.life365.eu/api/order/idOrder?jwt=...

            //Per rendere più veloce la prova mettere 2 qui
            for (int i = 0; i < jsonObject.getJSONArray("items").length(); i++) {
                Product product = new Product();
                ProductInfo productInfo1 = new ProductInfo();
                productInfo1.setIdOrder(jsonObject.getInt("id"));

                product.setId(jsonObject.getJSONArray("items").getJSONObject(i).getString("barcode"));
                product.setWarehousePlace(jsonObject.getJSONArray("items").getJSONObject(i).getString("warehouse_place"));
                product.setCodicesenza(jsonObject.getJSONArray("items").getJSONObject(i).getString("Codicesenza"));
                productInfo1.setQuantity(jsonObject.getJSONArray("items").getJSONObject(i).getInt("qta"));
                product.setProductInfo(productInfo1);

                products.add(product);
            }
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



    /***
     * Setter e getter
     */
    public void setCodicesenza(String codicesenza) {
        this.codicesenza = codicesenza;
    }

    public void setId(String id){
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

    public String getId() {
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", codicesenza='" + codicesenza + '\'' +
                ", warehousePlace='" + warehousePlace + '\'' +
                ", productInfo=" + productInfo +
                '}';
    }
}
