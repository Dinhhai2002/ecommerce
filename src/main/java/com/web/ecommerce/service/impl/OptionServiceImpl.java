package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.OptionDao;
import com.web.ecommerce.entity.Option;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.service.OptionService;

@Service("OptionService")
@Transactional(rollbackFor = Error.class)
public class OptionServiceImpl extends BaseServiceImpl<Option, Integer> implements OptionService {
    @Autowired
    private OptionDao optionDao;

    @Override
    public Option findByName(String name) {
        return optionDao.findByName(name);
    }

    @Override
    public StoreProcedureListResult<Option> spGListOption(String keySearch, int status, Pagination pagination)
			throws Exception {
		return optionDao.spGListOption(keySearch, status, pagination);
	}
}
