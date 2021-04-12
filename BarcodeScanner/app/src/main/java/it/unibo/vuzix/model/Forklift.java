package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forklift implements Parcelable {

    private int idRaspberry;
    private String jwt; //Lo vedo come id del magazziniere che si logga
    private Map<Integer, Integer> placementOrderMap;
    private int placementNumber;

    public Forklift(){
        placementOrderMap = new HashMap<>();
    }

    protected Forklift(Parcel in) {
        this.idRaspberry = in.readInt();
        this.jwt = in.readString();
        this.placementOrderMap = new HashMap<>();
        in.readMap(placementOrderMap, Integer.class.getClassLoader());
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
        parcel.writeMap(this.placementOrderMap);
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

    public Map<Integer, Integer> getPlacementOrderMap() {
        return placementOrderMap;
    }

    public void setPlacementOrderMap(Map<Integer, Integer> placementOrderMap) {
        this.placementOrderMap = placementOrderMap;
    }

    public void addElementMap(Integer key, Integer value){
        this.placementOrderMap.put(key, value);
    }

    public List<Integer> getAllOrders(){
        return (List<Integer>) this.placementOrderMap.values();
    }

    public int getPlacementNumber() {
        return placementNumber;
    }

    public void setPlacementNumber(int placementNumber) {
        this.placementNumber = placementNumber;
    }
}
