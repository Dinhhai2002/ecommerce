package com.web.ecommerce.dao;

import com.web.ecommerce.entity.OptionValues;
import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface OptionValuesDao extends BaseDao<OptionValues, Integer> {
    OptionValues findByName(String name);

    StoreProcedureListResult<OptionValues> spGListOptionValues(String keySearch,int status,Pagination pagination) throws Exception;
}