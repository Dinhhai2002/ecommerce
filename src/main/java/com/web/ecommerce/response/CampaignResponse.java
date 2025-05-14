package com.web.ecommerce.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.Campaign;
import com.web.ecommerce.entity.CampaignCategory;
import com.web.ecommerce.entity.CampaignProduct;

import lombok.Data;

@Data
public class CampaignResponse {
    private int id;
    
    private String name;
    
    private String description;
    
    @JsonProperty("start_date")
    private Date startDate;
    
    @JsonProperty("end_date")
    private Date endDate;
    
    @JsonProperty("is_active")
    private boolean isActive;
    
    @JsonProperty("discount_type")
    private String discountType;
    
    @JsonProperty("discount_value")
    private BigDecimal discountValue;
    
    private int status;
    
    public CampaignResponse() {
        
    }
    
    public CampaignResponse(Campaign entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.isActive = entity.isActive();
        this.status = entity.getStatus();
    }
    
    public CampaignResponse(Campaign campaign, CampaignProduct campaignProduct) {
        this(campaign);
        this.discountType = campaignProduct.getDiscountType();
        this.discountValue = campaignProduct.getDiscountValue();
    }
    
    public CampaignResponse(Campaign campaign, CampaignCategory campaignCategory) {
        this(campaign);
        this.discountType = campaignCategory.getDiscountType();
        this.discountValue = campaignCategory.getDiscountValue();
    }
    
    public static List<CampaignResponse> mapToList(List<Campaign> entities) {
        return entities.stream().map(x -> new CampaignResponse(x)).collect(Collectors.toList());
    }
} 