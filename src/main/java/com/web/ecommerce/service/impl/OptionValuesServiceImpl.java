package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.OptionValuesDao;
import com.web.ecommerce.entity.OptionValues;
import com.web.ecommerce.service.OptionValuesService;

@Service("OptionValuesService")
@Transactional(rollbackFor = Error.class)
public class OptionValuesServiceImpl extends BaseServiceImpl<OptionValues, Integer> implements OptionValuesService {
    @Autowired
    private OptionValuesDao optionValuesDao;

    @Override
    public OptionValues findByName(String name) {
        return optionValuesDao.findByName(name);
    }
}
