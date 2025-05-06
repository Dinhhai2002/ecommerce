package com.web.ecommerce.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    @UpdateTimestamp
    private Date updatedAt;
    
    public String amountToCurrencyString(float amountTotal, String currency) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(amountTotal) + currency;
    }
    
    public String amountToCurrencyString(float amountTotal) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(amountTotal) + "đ";
    }
    
    public String getDateFormatVN(Date date) {
        if(date == null) {
            date = new Date();
            return (new SimpleDateFormat("dd/MM/yyyy").format(date));
        } else {
            return (new SimpleDateFormat("dd/MM/yyyy").format(date));
        }
    }
    
    public String getDatetimeFormatVN(Date date) {
        if(date == null) {
            date = new Date();
            return (new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date));
        } else {
            return (new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date));
        }
    }
    
    public String getFullDatetimeFormatVN(Date date) {
        if(date == null) {
            date = new Date();
            return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date));
        } else {
            return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date));
        }
    }
    
    public String getDateFormatVNEmptyIfNull(Date date) {
        if(date == null) {
            return "";
        } else {
            return (new SimpleDateFormat("dd/MM/yyyy").format(date));
        }
    }
    
    public String getDatetimeFormatVNEmptyIfNull(Date date) {
        if(date == null) {
            return "";
        } else {
            return (new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date));
        }
    }
    
    public String getMonthYearFormatVN(Date date) {
        if (date == null) {
            return "";
        } else {
            return (new SimpleDateFormat("MM/yyyy").format(date));
        }
    }
    
    public String getCreatedAtFormatVN() {
        return this.getDatetimeFormatVN(createdAt);
    }
    
    public String getUpdatedAtFormatVN() {
        return this.getDatetimeFormatVN(updatedAt);
    }
}
