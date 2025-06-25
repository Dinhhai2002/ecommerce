package com.web.ecommerce.service;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.OptionValues;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface OptionValuesService extends BaseService<OptionValues, Integer> {
    OptionValues findByName(String name);

    StoreProcedureListResult<OptionValues> spGListOptionValues(String keySearch,int status,Pagination pagination) throws Exception;
}
