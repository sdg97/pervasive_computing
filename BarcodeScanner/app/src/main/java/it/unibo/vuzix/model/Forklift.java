package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class Forklift implements Parcelable {

    private int idRaspberry;
    private Map<Integer, Integer> placementOrderMap;
    private String jwt; //Lo vedo come id del magazziniere che si logga

    protected Forklift(Parcel in) {
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
    }
}
