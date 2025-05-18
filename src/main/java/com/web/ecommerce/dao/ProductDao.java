package com.web.ecommerce.dao;

import java.math.BigDecimal;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.model.ProductFillterDto;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface ProductDao extends BaseDao<Product, Integer> {
    Product findByName(String name);
   
    StoreProcedureListResult<ProductFillterDto> filterProductsStore(int productId, BigDecimal priceFrom,
			BigDecimal priceTo, String categoryIds, String options, String sort,int isShowParent, String keySearch,
			int status, Pagination pagination) throws Exception;
    
}