package com.web.ecommerce.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WebsiteStatisticalResponse {
	@JsonProperty("total_users")
	private int totalUsers;
	
	@JsonProperty("total_revenue")
	private BigDecimal totalRevenue;
	
	@JsonProperty("total_products")
	private int totalProducts;
	
	@JsonProperty("total_orders")
	private int totalOrders;
	
	@JsonProperty("daily_revenue")
    private BigDecimal dailyRevenue;

    @JsonProperty("monthly_revenue")
    private BigDecimal monthlyRevenue;

    @JsonProperty("yearly_revenue")
    private BigDecimal yearlyRevenue;

	public WebsiteStatisticalResponse(int totalUsers, BigDecimal totalRevenue, int totalProducts, int totalOrders) {
		this.totalUsers = totalUsers;
		this.totalRevenue = totalRevenue;
		this.totalProducts = totalProducts;
		this.totalOrders = totalOrders;
	}
	
	public WebsiteStatisticalResponse(int totalUsers, BigDecimal totalRevenue, int totalProducts, int totalOrders, BigDecimal dailyRevenue, BigDecimal monthlyRevenue, BigDecimal yearlyRevenue) {
        this.totalUsers = totalUsers;
        this.totalRevenue = totalRevenue;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.dailyRevenue = dailyRevenue;
        this.monthlyRevenue = monthlyRevenue;
        this.yearlyRevenue = yearlyRevenue;
    }
}
