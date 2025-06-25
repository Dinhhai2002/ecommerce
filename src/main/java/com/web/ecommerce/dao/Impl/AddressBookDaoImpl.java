package com.web.ecommerce.dao.impl;

import java.util.List;

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
import com.web.ecommerce.dao.AddressBookDao;
import com.web.ecommerce.entity.AddressBook;
import com.web.ecommerce.model.StoreProcedureListResult;

@Repository("AddressBookDao")
@Transactional
public class AddressBookDaoImpl extends BaseDaoImpl<AddressBook, Integer> implements AddressBookDao {
    public AddressBookDaoImpl() {
        super(AddressBook.class);
    }

    @Override
    public AddressBook findByName(String name) {
        CriteriaBuilder builder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<AddressBook> query = builder.createQuery(AddressBook.class);
        Root<AddressBook> root = query.from(AddressBook.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

	@Override
	public StoreProcedureListResult<AddressBook> spGListAddressBook(int userId, String keySearch, int status,
			Pagination pagination) throws Exception {
		StoredProcedureQuery query = this.getSession()
	            .createStoredProcedureQuery("sp_g_list_addressbook", AddressBook.class)
	            .registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
	            .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
	            .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

	        query.setParameter("userId", userId);
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

	@Override
	public void setDefaultAddress(int userId, int addressId) throws Exception {
		  String hql1 = "UPDATE AddressBook SET isDefault = 0 WHERE userId = :userId";
	        this.getSession().createQuery(hql1)
	            .setParameter("userId", userId)
	            .executeUpdate();

	        // Set the selected address as default
	        String hql2 = "UPDATE AddressBook SET isDefault = 1 WHERE id = :addressId AND userId = :userId";
	        this.getSession().createQuery(hql2)
	            .setParameter("addressId", addressId)
	            .setParameter("userId", userId)
	            .executeUpdate();
	}

	@Override
	public List<AddressBook> findByUserId(int userId) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<AddressBook> query = builder.createQuery(AddressBook.class);
        Root<AddressBook> root = query.from(AddressBook.class);
        query.where(builder.equal(root.get("userId"), userId));
        return this.getSession().createQuery(query).getResultList();
	}
}
