package com.agri.chattla.model;

import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem;

import java.io.Serializable;

public class Item implements PayableItem, Serializable {
    private String description;
    private String earningRuleID = null;
    private String height = null;
    private String length = null;
    private String originalPrice = null;
    private String price;
    private String qty;
    private String[] reservationCodes;
    private String sku;
    private String variantCode = null;
    private String weight = null;
    private String width = null;

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String str) {
        this.height = str;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String str) {
        this.length = str;
    }

    public String getVariantCode() {
        return this.variantCode;
    }

    public void setVariantCode(String str) {
        this.variantCode = str;
    }

    public String getEarningRuleID() {
        return this.earningRuleID;
    }

    public void setEarningRuleID(String str) {
        this.earningRuleID = str;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String str) {
        this.weight = str;
    }

    public String[] getReservationCodes() {
        return this.reservationCodes;
    }

    public void setReservationCodes(String[] strArr) {
        this.reservationCodes = strArr;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setPrice(String str) {
        this.price = str;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String str) {
        this.sku = str;
    }

    public String getQty() {
        return this.qty;
    }

    public void setQty(String str) {
        this.qty = str;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String str) {
        this.width = str;
    }

    public String getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(String str) {
        this.originalPrice = str;
    }

    public Item() {
    }

    public Item(String str, String str2, String str3, String str4) {
        setSku(str);
        setDescription(str2);
        setQty(str3);
        setPrice(str4);
    }

    public Item(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String[] strArr, String str10) {
        setSku(str);
        setDescription(str2);
        setQty(str3);
        setPrice(str4);
        setOriginalPrice(str5);
        setWidth(str6);
        setHeight(str7);
        setWeight(str8);
        setVariantCode(str9);
        setReservationCodes(strArr);
        setEarningRuleID(str10);
    }

    public Item(String str, String str2, String str3, String str4, String str5, String str6, String[] strArr, String str7) {
        setSku(str);
        setDescription(str2);
        setQty(str3);
        setPrice(str4);
        setOriginalPrice(str5);
        setVariantCode(str6);
        setReservationCodes(strArr);
        setEarningRuleID(str7);
    }

    public String getFawryItemOriginalPrice() {
        return this.originalPrice;
    }

    public String getFawryItemDescription() {
        return this.description;
    }

    public String getFawryItemSKU() {
        return this.sku;
    }

    public String getFawryItemPrice() {
        return this.price;
    }

    public String getFawryItemQuantity() {
        return this.qty;
    }

    public String getFawryItemVariantCode() {
        return this.variantCode;
    }

    public String[] getFawryItemReservationCodes() {
        return this.reservationCodes;
    }

    public String getFawryItemHeight() {
        return this.height;
    }

    public String getFawryItemLength() {
        return this.length;
    }

    public String getFawryItemWeight() {
        return this.weight;
    }

    public String getFawryItemEarningRuleID() {
        return this.earningRuleID;
    }

    public String getFawryItemWidth() {
        return this.width;
    }
}
