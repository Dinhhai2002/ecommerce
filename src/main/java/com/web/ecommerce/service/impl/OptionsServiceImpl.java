package com.web.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.dao.OptionsDao;
import com.web.ecommerce.entity.Options;
import com.web.ecommerce.service.OptionsService;

@Service("OptionsService")
@Transactional(rollbackFor = Error.class)
public class OptionsServiceImpl extends BaseServiceImpl<Options, Integer> implements OptionsService {
    @Autowired
    private OptionsDao optionsDao;

    @Override
    public Options findByName(String name) {
        return optionsDao.findByName(name);
    }
}
