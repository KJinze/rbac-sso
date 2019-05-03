package com.diqie.rbac.sso.server.service.impl;

import com.diqie.rbac.sso.domain.RbacUser;
import com.diqie.rbac.sso.server.mapper.RbacUserMapper;
import com.diqie.rbac.sso.server.service.RbacUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RbacUserServiceImpl implements RbacUserService {

    private static Logger logger = LoggerFactory.getLogger(RbacUserServiceImpl.class);

    @Autowired
    private RbacUserMapper rbacUserMapper;

    public void saveRbacUser(RbacUser rbacUser) {
        rbacUserMapper.insertRbacUser(rbacUser);
    }

    public void updateRbacUser(RbacUser rbacUser) {
        rbacUserMapper.updateRbacUser(rbacUser);
    }

    public void deleteRbacUserById(String id) {
        rbacUserMapper.deleteRbacUser(id);
    }

    public RbacUser getRbacUserById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return rbacUserMapper.getRbacUserById(id);
    }

    @Override
    public RbacUser getRbacUserByUserName(String userName) {
        return rbacUserMapper.getRbacUserByUserName(userName);
    }

    public List<RbacUser> listRbacUser(RbacUser rbacUser) {
        List<RbacUser> list = rbacUserMapper.listRbacUser(rbacUser);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

