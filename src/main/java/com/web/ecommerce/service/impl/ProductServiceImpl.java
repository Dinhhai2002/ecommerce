package com.web.ecommerce.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.ProductDao;
import com.web.ecommerce.entity.Campaign;
import com.web.ecommerce.entity.CampaignCategory;
import com.web.ecommerce.entity.CampaignProduct;
import com.web.ecommerce.entity.Category;
import com.web.ecommerce.entity.Option;
import com.web.ecommerce.entity.OptionValues;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.entity.ProductImage;
import com.web.ecommerce.entity.ProductOptions;
import com.web.ecommerce.exception.ProductNotFoundException;
import com.web.ecommerce.model.ProductFillterDto;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.response.CampaignResponse;
import com.web.ecommerce.response.ProductOptionsResponse;
import com.web.ecommerce.service.CampaignCategoryService;
import com.web.ecommerce.service.CampaignProductService;
import com.web.ecommerce.service.CampaignService;
import com.web.ecommerce.service.CategoryService;
import com.web.ecommerce.service.OptionService;
import com.web.ecommerce.service.OptionValuesService;
import com.web.ecommerce.service.ProductImageService;
import com.web.ecommerce.service.ProductOptionsService;
import com.web.ecommerce.service.ProductService;

@Service("ProductService")
@Transactional(rollbackFor = Error.class)
public class ProductServiceImpl extends BaseServiceImpl<Product, Integer> implements ProductService {
	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductOptionsService productOptionsService;

	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CampaignProductService campaignProductService;

	@Autowired
	private CampaignCategoryService campaignCategoryService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private OptionValuesService optionValuesService;

	@Override
	public Product findByName(String name) {
		return productDao.findByName(name);
	}

	@Override
	public List<ProductOptions> findProductOptionsById(Integer productId) {
		Product product = productDao.findOne(productId);
		if (product == null) {
			throw new ProductNotFoundException(productId);
		}
		return productOptionsService.findByProductId(productId);
	}

	@Override
	public List<ProductOptionsResponse> findProductOptionsWithDetailsById(Integer productId) {
		List<ProductOptions> options = findProductOptionsById(productId);

		// Collect all optionIds and optionValueIds
		List<Integer> optionIds = options.stream().map(ProductOptions::getOptionId).filter(id -> id != null)
				.collect(Collectors.toList());

		List<Integer> optionValueIds = options.stream().map(ProductOptions::getOptionValueId).filter(id -> id != null)
				.collect(Collectors.toList());

		// Fetch all options and optionValues in batch
		List<Option> allOptions = optionIds.isEmpty() ? new ArrayList<>() : optionService.findByIds(optionIds);
		List<OptionValues> allOptionValues = optionValueIds.isEmpty() ? new ArrayList<>()
				: optionValuesService.findByIds(optionValueIds);

		// Create maps for quick lookup
		Map<Integer, Option> optionMap = new HashMap<>();
		for (Option option : allOptions) {
			optionMap.put(option.getId(), option);
		}

		Map<Integer, OptionValues> optionValueMap = new HashMap<>();
		for (OptionValues optionValue : allOptionValues) {
			optionValueMap.put(optionValue.getId(), optionValue);
		}

		// Create response objects with detailed information
		List<ProductOptionsResponse> responses = new ArrayList<>();
		for (ProductOptions option : options) {
			ProductOptionsResponse response = new ProductOptionsResponse(option);

			if (option.getOptionId() != null && optionMap.containsKey(option.getOptionId())) {
				Option optionObj = optionMap.get(option.getOptionId());
				response.setOptionName(optionObj.getName());
			}

			if (option.getOptionValueId() != null && optionValueMap.containsKey(option.getOptionValueId())) {
				OptionValues optionValueObj = optionValueMap.get(option.getOptionValueId());
				response.setOptionValueName(optionValueObj.getValue());
			}

			responses.add(response);
		}

		return responses;
	}

	@Override
	public List<ProductImage> findProductImagesById(Integer productId) {
		Product product = productDao.findOne(productId);
		if (product == null) {
			throw new ProductNotFoundException(productId);
		}
		return productImageService.findByProductId(productId);
	}

	@Override
	public Category findProductCategoryById(Integer productId) {
		Product product = productDao.findOne(productId);
		if (product == null) {
			throw new ProductNotFoundException(productId);
		}
		if (product.getCategoryId() == null) {
			return null;
		}
		return categoryService.findOne(product.getCategoryId());
	}

	@Override
	public List<CampaignResponse> findProductCampaignsById(Integer productId) {
		Product product = productDao.findOne(productId);
		if (product == null) {
			throw new ProductNotFoundException(productId);
		}

		List<CampaignResponse> campaignResponses = new ArrayList<>();

		// Find direct product campaigns
		List<CampaignProduct> campaignProducts = campaignProductService.findByProductId(productId);

		if (!campaignProducts.isEmpty()) {
			// Extract campaign IDs
			List<Integer> campaignIds = campaignProducts.stream().map(CampaignProduct::getCampaignId)
					.collect(Collectors.toList());

			// Get active campaigns in batch
			List<Campaign> activeCampaigns = campaignService.findActiveByIds(campaignIds);

			// Create map for quick lookup
			Map<Integer, Campaign> campaignMap = new HashMap<>();
			for (Campaign campaign : activeCampaigns) {
				campaignMap.put(campaign.getId(), campaign);
			}

			// Create campaign responses
			for (CampaignProduct campaignProduct : campaignProducts) {
				Campaign campaign = campaignMap.get(campaignProduct.getCampaignId());
				if (campaign != null) {
					campaignResponses.add(new CampaignResponse(campaign, campaignProduct));
				}
			}
		}

		// If no direct product campaigns, check for category campaigns
		if (campaignResponses.isEmpty() && product.getCategoryId() != null) {
			List<CampaignCategory> campaignCategories = campaignCategoryService
					.findByCategoryId(product.getCategoryId());

			if (!campaignCategories.isEmpty()) {
				// Extract campaign IDs
				List<Integer> campaignIds = campaignCategories.stream().map(CampaignCategory::getCampaignId)
						.collect(Collectors.toList());

				// Get active campaigns in batch
				List<Campaign> activeCampaigns = campaignService.findActiveByIds(campaignIds);

				// Create map for quick lookup
				Map<Integer, Campaign> campaignMap = new HashMap<>();
				for (Campaign campaign : activeCampaigns) {
					campaignMap.put(campaign.getId(), campaign);
				}

				// Create campaign responses
				for (CampaignCategory campaignCategory : campaignCategories) {
					Campaign campaign = campaignMap.get(campaignCategory.getCampaignId());
					if (campaign != null) {
						campaignResponses.add(new CampaignResponse(campaign, campaignCategory));
					}
				}
			}
		}

		return campaignResponses;
	}

	@Override
	public StoreProcedureListResult<ProductFillterDto> filterProductsStore(int productId, BigDecimal priceFrom,
			BigDecimal priceTo, String categoryIds, String options, String sort, int isShowParent, String keySearch,
			int status, Pagination pagination) throws Exception {
		return productDao.filterProductsStore(productId, priceFrom, priceTo, categoryIds, options, sort, isShowParent,
				keySearch, status, pagination);
	}

}