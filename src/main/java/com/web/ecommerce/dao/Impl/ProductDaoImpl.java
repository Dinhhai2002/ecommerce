package com.web.ecommerce.dao.impl;

import java.math.BigDecimal;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.web.ecommerce.common.enums.StoreProcedureStatusCodeEnum;
import com.web.ecommerce.common.exception.TechresHttpException;
import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.ProductDao;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.model.ProductFillterDto;
import com.web.ecommerce.model.StoreProcedureListResult;

@Repository("ProductDao")
@Transactional
public class ProductDaoImpl extends BaseDaoImpl<Product, Integer> implements ProductDao {
	public ProductDaoImpl() {
		super(Product.class);
	}

	@Override
	public Product findByName(String name) {
		CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);
		query.where(builder.equal(root.get("name"), name));
		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public StoreProcedureListResult<ProductFillterDto> filterProductsStore(int productId, BigDecimal priceFrom,
			BigDecimal priceTo, String categoryIds, String options, String sort,int isShowParent, String keySearch,
			int status, Pagination pagination) throws Exception {
		StoredProcedureQuery query = this.getSession()
				.createStoredProcedureQuery("sp_g_filter_products", ProductFillterDto.class)
				.registerStoredProcedureParameter("productId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("priceFrom", BigDecimal.class, ParameterMode.IN)
				.registerStoredProcedureParameter("priceTo", BigDecimal.class, ParameterMode.IN)
				.registerStoredProcedureParameter("categoryIds", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("options", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("sort", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("isShowParent", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("productId", productId);
		query.setParameter("priceFrom", priceFrom);
		query.setParameter("priceTo", priceTo);
		query.setParameter("categoryIds", categoryIds);
		query.setParameter("options", options);
		query.setParameter("sort", sort);
		query.setParameter("isShowParent", isShowParent);
		query.setParameter("keySearch", keySearch);
		query.setParameter("status", status);
		query.setParameter("_limit", pagination.getLimit());
		query.setParameter("_offset", pagination.getOffset());

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			int totalRecord = (int) query.getOutputParameterValue("total_record");
			return new StoreProcedureListResult<>(statusCode, messageError, totalRecord, query.getResultList());
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}
}
