package com.ccc.pojo;

import java.util.Date;

public class Address {
    private Integer addressID;              //联系人ID
    private String  addressContact;      //联系人姓名
    private String  addressDesc;           //联系人地址
    private String  addressTel;             //联系人电话
    private Integer  addressCreateBy;  //创建人
    private Date addressCreationDate;//创建时间
    private Integer addressModifyBy;//修改者
    private Date addressModifyDate;//修改时间
    private Integer addressUserID;//用户ID

    public Integer getAddressID() {
        return addressID;
    }

    public void setAddressID(Integer addressID) {
        this.addressID = addressID;
    }

    public String getAddressContact() {
        return addressContact;
    }

    public void setAddressContact(String addressContact) {
        this.addressContact = addressContact;
    }

    public String getAddressDesc() {
        return addressDesc;
    }

    public void setAddressDesc(String addressDesc) {
        this.addressDesc = addressDesc;
    }

    public String getAddressTel() {
        return addressTel;
    }

    public void setAddressTel(String addressTel) {
        this.addressTel = addressTel;
    }

    public Integer getAddressCreateBy() {
        return addressCreateBy;
    }

    public void setAddressCreateBy(Integer addressCreateBy) {
        this.addressCreateBy = addressCreateBy;
    }

    public Date getAddressCreationDate() {
        return addressCreationDate;
    }

    public void setAddressCreationDate(Date addressCreationDate) {
        this.addressCreationDate = addressCreationDate;
    }

    public Integer getAddressModifyBy() {
        return addressModifyBy;
    }

    public void setAddressModifyBy(Integer addressModifyBy) {
        this.addressModifyBy = addressModifyBy;
    }

    public Date getAddressModifyDate() {
        return addressModifyDate;
    }

    public void setAddressModifyDate(Date addressModifyDate) {
        this.addressModifyDate = addressModifyDate;
    }

    public Integer getAddressUserID() {
        return addressUserID;
    }

    public void setAddressUserID(Integer addressUserID) {
        this.addressUserID = addressUserID;
    }
}
