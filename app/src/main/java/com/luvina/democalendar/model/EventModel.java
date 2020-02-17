package com.luvina.democalendar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents an event
 */
public class EventModel implements Parcelable {
    // Declare all attributes of an event
    private int id, notify;
    private String name, note, startDate, endDate;
    private byte[] image;

    public EventModel() {

    }

    protected EventModel(Parcel in) {
        id = in.readInt();
        notify = in.readInt();
        name = in.readString();
        note = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        image = in.createByteArray();
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {
            return new EventModel(in);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(notify);
        dest.writeString(name);
        dest.writeString(note);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeByteArray(image);
    }
}
