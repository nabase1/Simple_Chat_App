package com.nabase1.simplechatapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private String id;
    private String name;
    private String status;
    private String imageUrl;
    private String email;
    private String contact;


    public Users() {
    }

    public Users(String id, String name, String status, String imageUrl, String email, String contact) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.imageUrl = imageUrl;
        this.email = email;
        this.contact = contact;
    }

    protected Users(Parcel in) {
        id = in.readString();
        name = in.readString();
        status = in.readString();
        imageUrl = in.readString();
        email = in.readString();
        contact = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(imageUrl);
        dest.writeString(email);
        dest.writeString(contact);
    }
}
