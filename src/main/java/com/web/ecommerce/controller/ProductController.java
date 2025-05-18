package com.web.ecommerce.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Category;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.entity.ProductImage;
import com.web.ecommerce.exception.ProductNotFoundException;
import com.web.ecommerce.model.ProductFillterDto;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.CampaignResponse;
import com.web.ecommerce.response.CategoryResponse;
import com.web.ecommerce.response.ProductImageResponse;
import com.web.ecommerce.response.ProductOptionsResponse;
import com.web.ecommerce.response.ProductResponse;
import com.web.ecommerce.service.IFirebaseImageService;
import com.web.ecommerce.service.ProductImageService;
import com.web.ecommerce.service.ProductOptionsService;
import com.web.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController<Product, ProductResponse, Integer> {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductOptionsService productOptionsService;

	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private IFirebaseImageService iFirebaseImageService;
	
	public ProductController() {
		super(ProductResponse::new);
	}

	@GetMapping("/{id}/detail")
	public ResponseEntity<BaseResponse<ProductResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<ProductResponse> response = new BaseResponse<>();
		Product product = productService.findOne(id);

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		List<ProductOptionsResponse> productOptionsResponses = new ProductOptionsResponse()
				.mapToList(productOptionsService.findByProductId(product.getId()));
		List<ProductImageResponse> productImagesResponses = new ProductImageResponse()
				.mapToList(productImageService.findByProductId(product.getId()));
		response.setData(new ProductResponse(product, productOptionsResponses, productImagesResponses));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Filter products with various criteria
	 * 
	 * @return Filtered products with pagination information
	 */
	@GetMapping("/filter")
	@Operation(summary = "Filter products", description = "Lọc sản phẩm theo các tiêu chí như giá, danh mục, v.v.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Danh sách sản phẩm trả về thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
			@ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ"),
			@ApiResponse(responseCode = "500", description = "Lỗi server") })
	public ResponseEntity<BaseResponse<BaseListDataResponse<ProductResponse>>> filterProducts(
			@RequestParam(name = "product_id", required = false, defaultValue = "-1") Integer productId,
			@RequestParam(name = "price_from", required = false, defaultValue = "0") BigDecimal priceFrom,
			@RequestParam(name = "price_to", required = false, defaultValue = "0") BigDecimal priceTo,
			@RequestParam(name = "category_ids", required = false, defaultValue = "") String categoryIds,
			@RequestParam(name = "options", required = false, defaultValue = "") String options,
			@RequestParam(name = "sort", required = false, defaultValue = "name_asc") String sort,
			@RequestParam(name = "is_show_parent", required = false, defaultValue = "-1") int isShowParent,
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
		try {
			BaseResponse<BaseListDataResponse<ProductResponse>> response = new BaseResponse<>();
			// Create pagination object from parameters
			Pagination pagination = new Pagination(page, limit);

			// Call service with filter parameters
			StoreProcedureListResult<ProductFillterDto> product = productService.filterProductsStore(productId,
					priceFrom, priceTo, categoryIds, options, sort, isShowParent, keySearch, status, pagination);
			BaseListDataResponse<ProductResponse> listData = new BaseListDataResponse<>();
			listData.setList(new ProductResponse().mapToListDTO(product.getResult()));
			listData.setTotalRecord(product.getTotalRecord());

			response.setData(listData);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Get product options by product ID
	 * 
	 * @param id Product ID
	 * @return List of product options with details
	 */
	@GetMapping("/{id}/options")
	@Operation(summary = "Get product options", description = "Lấy danh sách tùy chọn của sản phẩm theo product_id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Danh sách tùy chọn trả về thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
			@ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
			@ApiResponse(responseCode = "500", description = "Lỗi server") })
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
	 * 
	 * @param id Product ID
	 * @return List of product images
	 */
	@GetMapping("/{id}/images")
	@Operation(summary = "Get product images", description = "Lấy danh sách hình ảnh của sản phẩm theo product_id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Danh sách hình ảnh trả về thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
			@ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
			@ApiResponse(responseCode = "500", description = "Lỗi server") })
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
	 * 
	 * @param id Product ID
	 * @return Category of the product
	 */
	@GetMapping("/{id}/category")
	@Operation(summary = "Get product category", description = "Lấy thông tin danh mục của sản phẩm theo product_id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Thông tin danh mục trả về thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
			@ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm hoặc danh mục"),
			@ApiResponse(responseCode = "500", description = "Lỗi server") })
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
	 * 
	 * @param id Product ID
	 * @return List of campaigns applicable to the product
	 */
	@GetMapping("/{id}/campaigns")
	@Operation(summary = "Get product campaigns", description = "Lấy thông tin giảm giá áp dụng cho sản phẩm theo product_id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Danh sách chiến dịch trả về thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
			@ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm"),
			@ApiResponse(responseCode = "500", description = "Lỗi server") })
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/{id}/image")
	public ResponseEntity<BaseResponse> uploadBanner(@RequestParam(name = "file") MultipartFile file,
			@PathVariable("id") int id) throws Exception {
		BaseResponse response = new BaseResponse();
		Product product = productService.findOne(id);

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Not found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		String fileName = iFirebaseImageService.save(file);

		String imageUrl = iFirebaseImageService.getImageUrl(fileName);

		ProductImage productImage = new ProductImage();
		productImage.setImageUrl(imageUrl);
		productImage.setProductId(product.getId());
		productImageService.create(productImage);

		response.setData(new ProductImageResponse(productImage));
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Upload nhiều hình ảnh cho sản phẩm
	 * @param files Danh sách file ảnh cần upload
	 * @param id ID của sản phẩm
	 * @return Danh sách thông tin ảnh đã upload
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/{id}/images/multiple")
	public ResponseEntity<BaseResponse> uploadMultipleImages(
			@RequestParam(name = "files") MultipartFile[] files,
			@PathVariable("id") int id) throws Exception {
		
		BaseResponse response = new BaseResponse();
		Product product = productService.findOne(id);

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy sản phẩm");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra nếu không có file nào được gửi lên
		if (files == null || files.length == 0) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không có file ảnh nào được gửi lên");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		List<ProductImageResponse> imageResponses = new ArrayList<>();
		
		// Upload từng file và lưu thông tin vào database
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				try {
					// Upload file lên Firebase Storage
					String fileName = iFirebaseImageService.save(file);
					String imageUrl = iFirebaseImageService.getImageUrl(fileName);
					
					// Tạo đối tượng ProductImage và lưu vào database
					ProductImage productImage = new ProductImage();
					productImage.setImageUrl(imageUrl);
					productImage.setProductId(product.getId());
					
					// Lưu vào database
					productImageService.create(productImage);
					
				} catch (Exception e) {
					// logger.error("Lỗi khi upload file: " + e.getMessage(), e);
					System.err.println("Lỗi khi upload file: " + e.getMessage());
				}
			}
		}
		
		// Trả về kết quả
		response.setStatus(HttpStatus.OK);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Đặt một ảnh làm ảnh chính cho sản phẩm
	 * @param productId ID của sản phẩm
	 * @param imageId ID của ảnh cần đặt làm ảnh chính
	 * @return Thông tin ảnh đã được đặt làm ảnh chính
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("/{productId}/images/{imageId}/set-primary")
	public ResponseEntity<BaseResponse> setImageAsPrimary(
			@PathVariable("productId") int productId,
			@PathVariable("imageId") int imageId) throws Exception {
		
		BaseResponse response = new BaseResponse();
		
		// Kiểm tra sản phẩm tồn tại
		Product product = productService.findOne(productId);
		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy sản phẩm");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Lấy ảnh cần đặt làm ảnh chính
		ProductImage image = productImageService.findOne(imageId);
		if (image == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy ảnh");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra ảnh có thuộc sản phẩm không
		if (image.getProductId() != productId) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Ảnh không thuộc sản phẩm này");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Reset tất cả ảnh của sản phẩm này về không phải ảnh chính
		List<ProductImage> productImages = productImageService.findByProductId(productId);
		for (ProductImage img : productImages) {
			img.setPrimary(false);
			productImageService.update(img);
		}
		
		// Đặt ảnh được chọn là ảnh chính
		image.setPrimary(true);
		productImageService.update(image);
		
		// Trả về kết quả
		response.setStatus(HttpStatus.OK);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Xóa một ảnh của sản phẩm
	 * @param productId ID của sản phẩm
	 * @param imageId ID của ảnh cần xóa
	 * @return Kết quả xóa ảnh
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("/{productId}/images/{imageId}")
	public ResponseEntity<BaseResponse> deleteProductImage(
			@PathVariable("productId") int productId,
			@PathVariable("imageId") int imageId) throws Exception {
		
		BaseResponse response = new BaseResponse();
		
		// Kiểm tra sản phẩm tồn tại
		Product product = productService.findOne(productId);
		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy sản phẩm");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Lấy ảnh cần xóa
		ProductImage image = productImageService.findOne(imageId);
		if (image == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không tìm thấy ảnh");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Kiểm tra ảnh có thuộc sản phẩm không
		if (image.getProductId() != productId) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Ảnh không thuộc sản phẩm này");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		// Xóa file ảnh từ Firebase Storage (nếu có)
		try {
			String fileName = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
			iFirebaseImageService.delete(fileName);
		} catch (Exception e) {
			// Ghi log lỗi nhưng vẫn tiếp tục xóa record trong database
			System.err.println("Lỗi khi xóa file từ Firebase: " + e.getMessage());
		}
		
		
		image.setStatus(0);
		productImageService.update(image);
		
		// Trả về kết quả
		response.setData("Xóa ảnh thành công");
		response.setStatus(HttpStatus.OK);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
