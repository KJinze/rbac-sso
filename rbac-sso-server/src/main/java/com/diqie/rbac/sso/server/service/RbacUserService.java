package com.diqie.rbac.sso.server.service;

import com.diqie.rbac.sso.domain.RbacUser;

import java.util.List;

public interface RbacUserService {
    void saveRbacUser(RbacUser rbacUser);

    void updateRbacUser(RbacUser rbacUser);

    void deleteRbacUserById(String id);

    RbacUser getRbacUserById(String id);

    RbacUser getRbacUserByUserName(String userName);

    List<RbacUser> listRbacUser(RbacUser rbacUser);
}
