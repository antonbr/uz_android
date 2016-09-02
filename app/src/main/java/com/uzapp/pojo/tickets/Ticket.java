package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 23.08.16.
 */
public class Ticket {
    @SerializedName("order_number")
    @Expose
    public String orderNumber;
    @SerializedName("fiscal_info_id")
    @Expose
    public String fiscalInfoId;
    @SerializedName("fiscal_info_rro")
    @Expose
    public String fiscalInfoRro;
    @SerializedName("fiscal_info_server")
    @Expose
    public String fiscalInfoServer;
    @SerializedName("fiscal_info_tin")
    @Expose
    public String fiscalInfoTin;
    @SerializedName("document")
    @Expose
    public Document document;
    @SerializedName("qr_image")
    @Expose
    public String qrImage;
    @SerializedName("barcode_image")
    @Expose
    public String barcodeImage;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFiscalInfoId() {
        return fiscalInfoId;
    }

    public void setFiscalInfoId(String fiscalInfoId) {
        this.fiscalInfoId = fiscalInfoId;
    }

    public String getFiscalInfoRro() {
        return fiscalInfoRro;
    }

    public void setFiscalInfoRro(String fiscalInfoRro) {
        this.fiscalInfoRro = fiscalInfoRro;
    }

    public String getFiscalInfoServer() {
        return fiscalInfoServer;
    }

    public void setFiscalInfoServer(String fiscalInfoServer) {
        this.fiscalInfoServer = fiscalInfoServer;
    }

    public String getFiscalInfoTin() {
        return fiscalInfoTin;
    }

    public void setFiscalInfoTin(String fiscalInfoTin) {
        this.fiscalInfoTin = fiscalInfoTin;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    public String getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(String barcodeImage) {
        this.barcodeImage = barcodeImage;
    }
}
