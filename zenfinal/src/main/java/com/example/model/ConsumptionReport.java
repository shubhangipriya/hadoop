package com.example.model;

public class ConsumptionReport {

    String villageName;
    Double amount;

    public ConsumptionReport(String villageName, Double amount) {
        this.villageName = villageName;
        this.amount = amount;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
