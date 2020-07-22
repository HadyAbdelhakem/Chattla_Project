package com.agri.chattla.model;

public class Farmar {

    private String Degree ;
    private String YoE ;

    public Farmar() {
    }

    public Farmar(String degree, String yoE) {
        Degree = degree;
        YoE = yoE;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getYoE() {
        return YoE;
    }

    public void setYoE(String yoE) {
        YoE = yoE;
    }
}
