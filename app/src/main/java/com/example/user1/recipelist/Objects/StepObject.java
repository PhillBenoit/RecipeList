package com.example.user1.recipelist.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */

public class StepObject implements Parcelable {

    private int id;
    private String description, short_description, video_url, thumbnail_url;

    public StepObject() {
        id = 0;
        description = "";
        short_description = "";
        video_url = "";
        thumbnail_url = "";
    }

    public StepObject(int id, String description, String short_description,
                      String video_url, String thumbnail_url) {
        this.id = id;
        this.description = description;
        this.short_description = short_description;
        this.video_url = video_url;
        this.thumbnail_url = thumbnail_url;
    }

    protected StepObject(Parcel in) {
        id = in.readInt();
        description = in.readString();
        short_description = in.readString();
        video_url = in.readString();
        thumbnail_url = in.readString();
    }

    public static final Creator<StepObject> CREATOR = new Creator<StepObject>() {
        @Override
        public StepObject createFromParcel(Parcel in) {
            return new StepObject(in);
        }

        @Override
        public StepObject[] newArray(int size) {
            return new StepObject[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeString(short_description);
        parcel.writeString(video_url);
        parcel.writeString(thumbnail_url);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
