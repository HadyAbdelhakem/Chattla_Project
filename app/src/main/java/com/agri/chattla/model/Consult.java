package com.agri.chattla.model;


import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem;

import java.io.Serializable;
import java.io.StringReader;

public class Consult implements Serializable, PayableItem {

    private String image;
    private String SAimage;
    private String WAimage;
    private String category;
    private String desc;
    private String AgriType;
    private String Cropitem;
    private String nearCrops;
    private String area;
    private String landType;
    private String IrrType;
    private String waterChannel;
    private String ProblemText;
    private String lat;
    private String lng;
    private String voice;
    private String time;
    private String status;
    private String sender;
    private String id;
    private String ExpertId;
    private String qty;
    private String price;
    private String sku;
    private String description;
    private String farmerToken;
    private String merchantRefNum;
    private String PaymentStatus;
    private String weather;
    private String topic;
    private String addValue;
    private String codeExpertId;
    private String addressLine;
    private String openSubscription;
    private String Subscriber1;
    private String Subscriber2;
    private String Subscriber3;
    private  long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMerchantRefNum() {
        return merchantRefNum;
    }

    public void setMerchantRefNum(String merchantRefNum) {
        this.merchantRefNum = merchantRefNum;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getFarmerToken() {
        return farmerToken;
    }

    public void setFarmerToken(String farmerToken) {
        this.farmerToken = farmerToken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getExpertId() {
        return ExpertId;
    }

    public void setExpertId(String expertId) {
        ExpertId = expertId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Consult() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSAimage() {
        return SAimage;
    }

    public void setSAimage(String SAimage) {
        this.SAimage = SAimage;
    }

    public String getWAimage() {
        return WAimage;
    }

    public void setWAimage(String WAimage) {
        this.WAimage = WAimage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getAgriType() {
        return AgriType;
    }

    public void setAgriType(String agriType) {
        AgriType = agriType;
    }

    public String getCropitem() {
        return Cropitem;
    }

    public void setCropitem(String cropitem) {
        Cropitem = cropitem;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNearCrops() {
        return nearCrops;
    }

    public void setNearCrops(String nearCrops) {
        this.nearCrops = nearCrops;
    }

    public String getIrrType() {
        return IrrType;
    }

    public void setIrrType(String irrType) {
        IrrType = irrType;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getWaterChannel() {
        return waterChannel;
    }

    public void setWaterChannel(String waterChannel) {
        this.waterChannel = waterChannel;
    }

    public String getProblemText() {
        return ProblemText;
    }

    public void setProblemText(String problemText) {
        ProblemText = problemText;
    }

    public String getAddValue() {
        return addValue;
    }

    public void setAddValue(String addValue) {
        this.addValue = addValue;
    }

    public String getCodeExpertId() {
        return codeExpertId;
    }

    public void setCodeExpertId(String codeExpertId) {
        this.codeExpertId = codeExpertId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getOpenSubscription() {
        return openSubscription;
    }

    public void setOpenSubscription(String openSubscription) {
        this.openSubscription = openSubscription;
    }

    public String getSubscriber1() {
        return Subscriber1;
    }

    public void setSubscriber1(String subscriber1) {
        this.Subscriber1 = subscriber1;
    }

    public String getSubscriber2() {
        return Subscriber2;
    }

    public void setSubscriber2(String subscriber2) {
        this.Subscriber2 = subscriber2;
    }

    public String getSubscriber3() {
        return Subscriber3;
    }

    public void setSubscriber3(String subscriber3) {
        this.Subscriber3 = subscriber3;
    }

    @Override
    public String getFawryItemDescription() {
        return null;
    }

    @Override
    public String getFawryItemSKU() {
        return null;
    }

    @Override
    public String getFawryItemPrice() {
        return null;
    }

    @Override
    public String getFawryItemQuantity() {
        return null;
    }

    @Override
    public String getFawryItemVariantCode() {
        return null;
    }

    @Override
    public String[] getFawryItemReservationCodes() {
        return new String[0];
    }

    @Override
    public String getFawryItemHeight() {
        return null;
    }

    @Override
    public String getFawryItemWidth() {
        return null;
    }

    @Override
    public String getFawryItemLength() {
        return null;
    }

    @Override
    public String getFawryItemWeight() {
        return null;
    }

    @Override
    public String getFawryItemEarningRuleID() {
        return null;
    }

    @Override
    public String getFawryItemOriginalPrice() {
        return null;
    }
}

