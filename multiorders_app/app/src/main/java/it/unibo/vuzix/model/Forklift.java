package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forklift implements Parcelable {

    public static final String FORKLIFT_KEY = "FORKLIFT";

    private int idRaspberry;
    private String jwt; //id Worker
    private final Map<Integer, Integer> orderPlacementMap;
    private int placementNumber;

    /**
     * Constructor and method of Parcelable
     */
    public Forklift(){
        orderPlacementMap = new HashMap<>();
    }

    protected Forklift(Parcel in) {
        this.idRaspberry = in.readInt();
        this.jwt = in.readString();
        this.placementNumber = in.readInt();
        this.orderPlacementMap = new HashMap<>();
        in.readMap(orderPlacementMap, Integer.class.getClassLoader());
    }

    public static final Creator<Forklift> CREATOR = new Creator<Forklift>() {
        @Override
        public Forklift createFromParcel(Parcel in) {
            return new Forklift(in);
        }

        @Override
        public Forklift[] newArray(int size) {
            return new Forklift[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.idRaspberry);
        parcel.writeString(this.jwt);
        parcel.writeInt(this.placementNumber);
        parcel.writeMap(this.orderPlacementMap);
    }

    /***
     * Setter e getter
     */

    public int getIdRaspberry() {
        return idRaspberry;
    }

    public void setIdRaspberry(int idRaspberry) {
        this.idRaspberry = idRaspberry;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Map<Integer, Integer> getOrderPlacementMap() {
        return orderPlacementMap;
    }

    public void addElementMap(Integer key, Integer value){
        this.orderPlacementMap.put(key, value);
    }

    public List<Integer> getAllOrders(){
        return new ArrayList<>(this.orderPlacementMap.keySet());
    }

    public int getPlacementNumber() {
        return placementNumber;
    }

    public void setPlacementNumber(int placementNumber) {
        this.placementNumber = placementNumber;
    }

    /**
     * toString
     */

    @Override
    public String toString() {
        return "Forklift{" +
                "idRaspberry=" + idRaspberry +
                ", jwt='" + jwt + '\'' +
                ", orderPlacementMap=" + orderPlacementMap +
                ", placementNumber=" + placementNumber +
                '}';
    }
}
