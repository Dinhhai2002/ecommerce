package com.web.ecommerce.service;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Option;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface OptionService extends BaseService<Option, Integer> {
    Option findByName(String name);

    StoreProcedureListResult<Option> spGListOption(String keySearch,int status,Pagination pagination) throws Exception;
}
