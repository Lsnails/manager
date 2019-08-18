package com.base.common.aspect;


import com.base.common.exception.RRException;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.common.annotation.DataFilter;
import com.base.common.utils.Constant;
import com.base.modules.sys.entity.SysUserEntity;
import com.base.modules.sys.service.SysDeptService;
import com.base.modules.sys.service.SysRoleDeptService;
import com.base.modules.sys.service.SysUserRoleService;
import com.base.modules.sys.shiro.ShiroUtils;

import java.util.*;

/**
 * 数据过滤，切面处理类
 *
 * @since 3.0.0 2017-09-17
 */
/**
 *  2 注解说明
	2.1 @Aspect
	
	作用是把当前类标识为一个切面供容器读取
	
	2.2 @Before
	标识一个前置增强方法，相当于BeforeAdvice的功能，相似功能的还有
	
	2.3 @AfterReturning
	
	后置增强，相当于AfterReturningAdvice，方法正常退出时执行
	
	2.4 @AfterThrowing
	
	异常抛出增强，相当于ThrowsAdvice
	
	2.5 @After
	
	final增强，不管是抛出异常或者正常退出都会执行
	
	2.6 @Around
	
	环绕增强，相当于MethodInterceptor
	
	2.7 @DeclareParents
	
	引介增强，相当于IntroductionInterceptor
 * @author huanw
 *
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Pointcut("@annotation(com.base.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if(params != null && params instanceof Map){
            SysUserEntity user = ShiroUtils.getUserEntity();

            //如果不是超级管理员，则进行数据过滤
            if(user.getUserId() != Constant.SUPER_ADMIN){
                Map map = (Map)params;
                map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
            }

            return ;
        }

        throw new RRException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUserEntity user, JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }

        //部门ID列表
        Set<Long> deptIdList = new HashSet<>();

        //用户角色对应的部门ID列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if(roleIdList.size() > 0){
            List<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
            deptIdList.addAll(userDeptIdList);
        }

        //用户子部门ID列表
        if(dataFilter.subDept()){
            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            deptIdList.addAll(subDeptIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if(deptIdList.size() > 0){
            sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }

        //没有本部门数据权限，也能查询本人数据
        if(dataFilter.user()){
            if(deptIdList.size() > 0){
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getUserId());
        }

        sqlFilter.append(")");

        return sqlFilter.toString();
    }
}
