package com.web.ecommerce.service;

import java.util.List;

import com.web.ecommerce.entity.Product;
import com.web.ecommerce.entity.ProductImage;
import com.web.ecommerce.entity.ProductOptions;
import com.web.ecommerce.entity.Category;
import com.web.ecommerce.response.CampaignResponse;
import com.web.ecommerce.response.ProductOptionsResponse;

public interface ProductService extends BaseService<Product, Integer> {
    Product findByName(String name);
    
    /**
     * Find product options by product ID
     * @param productId Product ID
     * @return List of product options
     */
    List<ProductOptions> findProductOptionsById(Integer productId);
    
    /**
     * Find product options with option and option value details by product ID
     * @param productId Product ID
     * @return List of product options responses with option and option value details
     */
    List<ProductOptionsResponse> findProductOptionsWithDetailsById(Integer productId);
    
    /**
     * Find product images by product ID
     * @param productId Product ID
     * @return List of product images
     */
    List<ProductImage> findProductImagesById(Integer productId);
    
    /**
     * Find product category by product ID
     * @param productId Product ID
     * @return Category of the product
     */
    Category findProductCategoryById(Integer productId);
    
    /**
     * Find applicable campaigns for a product
     * @param productId Product ID
     * @return List of campaign responses
     */
    List<CampaignResponse> findProductCampaignsById(Integer productId);
}
