package com.agri.chattla.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notification implements Serializable {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("notification")
    @Expose
    private Notification_ notification;
    private final static long serialVersionUID = 1314083417229385323L;

    public Notification() {
    }

    public Notification(String to, Data data, Notification_ notification) {
        this.to = to;
        this.data = data;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification_ getNotification() {
        return notification;
    }

    public void setNotification(Notification_ notification) {
        this.notification = notification;
    }

    public static class Notification_ implements Serializable {

        @SerializedName("title")
        @Expose
        private String title;

        public Notification_(String title, String text) {
            this.title = title;
            this.text = text;
        }

        @SerializedName("text")
        @Expose
        private String text;
        private final static long serialVersionUID = 7517778768451268060L;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

    public static class Data implements Serializable
    {

        @SerializedName("extra_information")
        @Expose
        private String extraInformation;
        private final static long serialVersionUID = 264604977749989104L;

        public String getExtraInformation() {
            return extraInformation;
        }

        public Data(String extraInformation) {
            this.extraInformation = extraInformation;
        }

        public void setExtraInformation(String extraInformation) {
            this.extraInformation = extraInformation;
        }

    }
}