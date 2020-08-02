package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.StorageDao;
import com.atguigu.springcloud.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageDao storageDao;

    // 扣减库存
    @Override
    public void decrease(Long productId, Integer count) {

        log.info("------->storage-service 中扣减库存开始");
        storageDao.decrease(productId, count);
        log.info("------->storage-service中 扣减库存结束");
    }
}