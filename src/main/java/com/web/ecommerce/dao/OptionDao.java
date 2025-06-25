package com.web.ecommerce.dao;

import com.web.ecommerce.entity.Option;
import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface OptionDao extends BaseDao<Option, Integer> {
    Option findByName(String name);

    StoreProcedureListResult<Option> spGListOption(String keySearch,int status,Pagination pagination) throws Exception;
}