package com.diqie.rbac.sso.server.mapper;

import com.diqie.rbac.sso.domain.RbacUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RbacUserMapper {

    void insertRbacUser(RbacUser rbacUser);

    void updateRbacUser(RbacUser rbacUser);

    void deleteRbacUser(@Param("id") String id);

    List<RbacUser> listRbacUser(RbacUser rbacUser);

    RbacUser getRbacUserById(@Param("id") String id);

    RbacUser getRbacUserByUserName(@Param("userName") String userName);
}
