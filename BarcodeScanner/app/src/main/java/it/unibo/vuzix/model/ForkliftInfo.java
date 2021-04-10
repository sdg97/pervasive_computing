package it.unibo.vuzix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ForkliftInfo implements Parcelable {

    private int idRaspberry;

    protected ForkliftInfo(Parcel in) {
    }

    public static final Creator<ForkliftInfo> CREATOR = new Creator<ForkliftInfo>() {
        @Override
        public ForkliftInfo createFromParcel(Parcel in) {
            return new ForkliftInfo(in);
        }

        @Override
        public ForkliftInfo[] newArray(int size) {
            return new ForkliftInfo[size];
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
