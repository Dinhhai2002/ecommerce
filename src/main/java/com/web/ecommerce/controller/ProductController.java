package com.web.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.Category;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.entity.ProductImage;
import com.web.ecommerce.entity.ProductOptions;
import com.web.ecommerce.exception.ProductNotFoundException;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.CampaignResponse;
import com.web.ecommerce.response.CategoryResponse;
import com.web.ecommerce.response.ProductImageResponse;
import com.web.ecommerce.response.ProductOptionsResponse;
import com.web.ecommerce.response.ProductResponse;
import com.web.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController<Product, ProductResponse, Integer> {
    
    @Autowired
    private ProductService productService;
    
    public ProductController() {
        super(ProductResponse::new);
    }
    
    /**
     * Get product options by product ID
     * @param id Product ID
     * @return List of product options with details
     */
    @GetMapping("/{id}/options")
    @Operation(summary = "Get product options", description = "Lấy danh sách tùy chọn của sản phẩm theo product_id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Danh sách tùy chọn trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<List<ProductOptionsResponse>>> getProductOptions(
            @Parameter(description = "ID của sản phẩm") @PathVariable Integer id) {
        try {
            List<ProductOptionsResponse> responseList = productService.findProductOptionsWithDetailsById(id);
            return buildResponse(responseList, HttpStatus.OK, "success");
        } catch (ProductNotFoundException e) {
            return buildResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    /**
     * Get product images by product ID
     * @param id Product ID
     * @return List of product images
     */
    @GetMapping("/{id}/images")
    @Operation(summary = "Get product images", description = "Lấy danh sách hình ảnh của sản phẩm theo product_id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Danh sách hình ảnh trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<List<ProductImageResponse>>> getProductImages(
            @Parameter(description = "ID của sản phẩm") @PathVariable Integer id) {
        try {
            List<ProductImage> images = productService.findProductImagesById(id);
            List<ProductImageResponse> responseList = ProductImageResponse.mapToList(images);
            return buildResponse(responseList, HttpStatus.OK, "success");
        } catch (ProductNotFoundException e) {
            return buildResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    /**
     * Get product category by product ID
     * @param id Product ID
     * @return Category of the product
     */
    @GetMapping("/{id}/category")
    @Operation(summary = "Get product category", description = "Lấy thông tin danh mục của sản phẩm theo product_id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thông tin danh mục trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm hoặc danh mục"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<CategoryResponse>> getProductCategory(
            @Parameter(description = "ID của sản phẩm") @PathVariable Integer id) {
        try {
            Category category = productService.findProductCategoryById(id);
            if (category == null) {
                return buildResponse(null, HttpStatus.NOT_FOUND, "Category not found for product ID: " + id);
            }
            CategoryResponse response = new CategoryResponse(category);
            return buildResponse(response, HttpStatus.OK, "success");
        } catch (ProductNotFoundException e) {
            return buildResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    /**
     * Get product campaigns by product ID
     * @param id Product ID
     * @return List of campaigns applicable to the product
     */
    @GetMapping("/{id}/campaigns")
    @Operation(summary = "Get product campaigns", description = "Lấy thông tin giảm giá áp dụng cho sản phẩm theo product_id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Danh sách chiến dịch trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<List<CampaignResponse>>> getProductCampaigns(
            @Parameter(description = "ID của sản phẩm") @PathVariable Integer id) {
        try {
            List<CampaignResponse> campaigns = productService.findProductCampaignsById(id);
            return buildResponse(campaigns, HttpStatus.OK, "success");
        } catch (ProductNotFoundException e) {
            return buildResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
