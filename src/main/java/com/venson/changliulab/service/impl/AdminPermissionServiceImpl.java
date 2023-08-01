package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulab.constant.AdminCacheConst;
import com.venson.changliulab.entity.dto.AdminPermissionDTO;
import com.venson.changliulab.entity.dto.AdminPermissionTree;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.pojo.AdminPermission;
import com.venson.changliulab.entity.pojo.AdminRolePermission;
import com.venson.changliulab.entity.pojo.AdminUser;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.exception.CustomizedException;
import com.venson.changliulab.utils.*;
import com.venson.changliulab.mapper.AdminPermissionMapper;
import com.venson.changliulab.service.admin.AdminPermissionService;
import com.venson.changliulab.service.admin.AdminRolePermissionService;
import com.venson.changliulab.service.admin.AdminUserRoleService;
import com.venson.changliulab.service.admin.AdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
@Slf4j
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermission> implements AdminPermissionService {

    private final AdminRolePermissionService adminRolePermissionService;

    private final AdminUserService adminUserService;

    private final AdminUserRoleService userRoleService;


    public AdminPermissionServiceImpl(AdminRolePermissionService adminRolePermissionService, AdminUserService adminUserService, AdminUserRoleService userRoleService) {
        this.adminRolePermissionService = adminRolePermissionService;
        this.adminUserService = adminUserService;
        this.userRoleService = userRoleService;
    }


    //根据角色获取菜单
//    @Override
//    public List<AdminPermissionDTO> doGetPermissionsByRoleId(Long roleId) {
//        List<AdminPermission> allPermissions = getAllPermissions();
//        Assert.notNull(allPermissions);
//        List<AdminPermissionDTO> dtoList = new ArrayList<>();
//        //根据角色id获取角色权限
//        LambdaQueryWrapper<AdminRolePermission> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(AdminRolePermission::getRoleId,roleId).select(AdminRolePermission::getPermissionId);
//        List<AdminRolePermission> adminRolePermissionList = adminRolePermissionService.list(wrapper);
//        Set<Long> permissionIdSetByRoleId = adminRolePermissionList.stream().map(AdminRolePermission::getPermissionId).collect(Collectors.toSet());
//        //转换给角色id与角色权限对应Map对象
//        for (AdminPermission permission : allPermissions) {
//            AdminPermissionDTO dto = new AdminPermissionDTO();
//            BeanUtils.copyProperties(permission,dto);
//
//            if (permissionIdSetByRoleId.contains(permission.getId())) {
//                dto.setSelect(true);
//                }
//            dtoList.add(dto);
//        }
//        return buildTreeListByDTOList(dtoList);
//    }



    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(Long id) {
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            return baseMapper.selectAllPermissionValue();
        } else {
            return baseMapper.selectPermissionValueByUserId(id);
        }
    }

//    @Override
//    public List<JSONObject> selectPermissionByUserId(Long userId) {
//        List<AdminPermission> selectAdminPermissionList;
//        if(this.isSysAdmin(userId)) {
//            //如果是超级管理员，获取所有菜单
//            selectAdminPermissionList = baseMapper.selectList(null);
//        } else {
//            selectAdminPermissionList = baseMapper.selectPermissionByUserId(userId);
//        }
////        List<Permission> permissionList = PermissionHelper.build(finalPermissionList);
////        List<AdminPermissionDTO> dtolist = buildTreeListByPermissionList(selectAdminPermissionList);
////        return MenuHelper.buildNew(selectAdminPermissionList);
//        return MenuHelper.build(dtolist);
//    }

//    private Set<AdminPermission> getParentPermission(AdminPermission adminPermission, Map<Long, AdminPermission> allPermissionMap){
//        Set<AdminPermission> set = new HashSet<>();
//
//        set.add(adminPermission);
//        AdminPermission parentAdminPermission = allPermissionMap.get(adminPermission.getPid());
//        if(parentAdminPermission ==null){
//            return set;
//        }else{
//            set.add(parentAdminPermission);
//        }
//        if(parentAdminPermission.getId() ==1){
//            return set;
//        }else{
//            set.addAll(getParentPermission(parentAdminPermission,allPermissionMap));
//        }
//        return set;
//    }

    /**
     * 判断用户是否系统管理员
     */
    private boolean isSysAdmin(Long userId) {
        AdminUser user = adminUserService.getById(userId);

        return null != user && "admin".equals(user.getUsername());
    }

//    /**
//     *	递归获取子节点
//     * @param id id
//     * @param idList idList
//     */
//    private void selectChildListById(Long id, List<Long> idList) {
//        List<AdminPermission> childList = baseMapper.selectList(new QueryWrapper<AdminPermission>().eq("pid", id).select("id"));
//        childList.forEach(item -> {
//            idList.add(item.getId());
//            this.selectChildListById(item.getId(), idList);
//        });
//    }

//    private List<AdminPermissionDTO> buildTreeListByDTOList(List<AdminPermissionDTO> dtoList) {
//        HashMap<Long,LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOList = new HashMap<>();
//
//        for (AdminPermissionDTO dto: dtoList) {
//            if (1 ==dto.getPid()) {
//                dto.setLevel(1);
//            }
//            MultiMapUtils.put(dto.getPid(),dto,pidPermissionDTOList);
//        }
//        LinkedHashSet<AdminPermissionDTO> set = MultiMapUtils.get(0L, pidPermissionDTOList);
//        List<AdminPermissionDTO> treeList = set.stream().toList();
//        for (AdminPermissionDTO dto :
//                treeList) {
//            dto.setChildren(getChildrenPermissionDTO(dto, pidPermissionDTOList));
//        }
//        return treeList;
//    }

//    /**
//     * a recursive helper method to get children permissionDTO
//     * @param parentDto the parent permissionDTO
//     * @param pidPermissionDTOMap the map use pid(parentId) as key , LinkedHashSet of permissionDTO as value
//     * @return children permissionDTO list
//     */
//    private List<AdminPermissionDTO> getChildrenPermissionDTO(AdminPermissionDTO parentDto,
//                                                                     Map<Long,LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOMap) {
//        LinkedHashSet<AdminPermissionDTO> set = pidPermissionDTOMap.get(parentDto.getId());
//        if(ObjectUtils.isEmpty(set)){
//            return null;
//        }
//        List<AdminPermissionDTO> dtoList = set.stream().toList();
//
//        for (AdminPermissionDTO it : dtoList) {
//                int level =parentDto.getLevel() + 1;
//                it.setLevel(level);
//                it.setChildren(getChildrenPermissionDTO(it,pidPermissionDTOMap));
//        }
//        return dtoList;
//    }


    //========================递归查询所有菜单================================================
//    @Override
//    public List<AdminPermissionDTO> doGetAllPermissionTree() {
//        List<AdminPermission> permissionList = getAllPermissions(true);
//        //1 查询菜单表所有数据
//        //2 把查询所有菜单list集合按照要求进行封装
//        return buildTreeListByPermissionList(permissionList);
//    }

//    public List<AdminPermissionDTO> buildTreeListByPermissionList(List<AdminPermission> permissionList) {
//
//        HashMap<Long, LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOList = createBasePidPermissionDTOMap(permissionList);
//        LinkedHashSet<AdminPermissionDTO> set = MultiMapUtils.get(1L, pidPermissionDTOList);
//        List<AdminPermissionDTO> treeList = set.stream().toList();
//        for (AdminPermissionDTO dto :
//                treeList) {
//            dto.setChildren(getChildrenPermissionDTO(dto, pidPermissionDTOList));
//        }
//        return treeList;
//    }

//    private HashMap<Long, LinkedHashSet<AdminPermissionDTO>> createBasePidPermissionDTOMap(List<AdminPermission> permissionList) {
//        HashMap<Long,LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOList = new HashMap<>();
//
//        for (AdminPermission permission: permissionList) {
//            AdminPermissionDTO dto = new AdminPermissionDTO();
//            BeanUtils.copyProperties(permission,dto);
//            if (1 ==permission.getPid()) {
//                dto.setLevel(1);
//            }
//            MultiMapUtils.put(permission.getPid(),dto,pidPermissionDTOList);
//        }
//        return pidPermissionDTOList;
//    }


    //============递归删除菜单==================================
    @Override
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public AdminPermission doRemovePermissionById(Long id) {
        AdminPermission adminPermission = baseMapper.selectById(id);
        baseMapper.deleteById(adminPermission);
        return adminPermission;
    }

    //2 根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(Long id, List<Long> idList) {
        //查询菜单里面子菜单id
        QueryWrapper<AdminPermission>  wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<AdminPermission> childIdList = baseMapper.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.forEach(item -> {
            //封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectPermissionChildById(item.getId(),idList);
        });
    }

    //=========================给角色分配菜单=======================
    @Override
    @Deprecated
    public void saveRolePermissionRelationShipLab(Long roleId,List<Long> permissionIds) {
        //roleId角色id
        //permissionId菜单id 数组形式
        //1 创建list集合，用于封装添加数据
        List<AdminRolePermission> adminRolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for(Long perId : permissionIds) {
            if(ObjectUtils.isEmpty(perId)) continue;
            //RolePermission对象
            AdminRolePermission adminRolePermission = new AdminRolePermission();
            adminRolePermission.setRoleId(roleId);
            adminRolePermission.setPermissionId(perId);
            //封装到list集合
            adminRolePermissionList.add(adminRolePermission);
        }
        //添加到角色菜单关系表
        LambdaQueryWrapper<AdminRolePermission> wrapper = new QueryWrapper<AdminRolePermission>().lambda();
        wrapper.eq(AdminRolePermission::getRoleId, roleId);
        adminRolePermissionService.remove(wrapper);
        adminRolePermissionService.saveBatch(adminRolePermissionList);
    }

    @Override
    public Map<Long, AdminRolePermission> getPermissionIdsByRoleId(Long id) {
        List<AdminRolePermission> rolePermissions = getRolePermissionByRoleId(id);
        Map<Long,AdminRolePermission> rolePermissionMap = new HashMap<>();
        rolePermissions.forEach(o-> rolePermissionMap.put(o.getPermissionId(),o));

        return rolePermissionMap;
    }

    @Override
    public List<AdminRolePermission> getRolePermissionByRoleId(Long id) {
        LambdaQueryWrapper<AdminRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRolePermission::getRoleId,id).
                select(AdminRolePermission::getPermissionId,
                        AdminRolePermission::getId,
                        AdminRolePermission::getRoleId);
        return adminRolePermissionService.list(wrapper);

    }

    @Override
    @Transactional()
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public void doUpdatePermission(Long id, AdminPermission adminPermission) {
        AdminPermission permission = baseMapper.selectById(id);
        Assert.isTrue(Objects.equals(id,adminPermission.getId()), "Invalid data");
        checkValid(adminPermission.getCategory(),adminPermission.getAction(),id);
        BeanUtils.copyProperties(adminPermission,permission,"slug");
        String slug = permission.getCategory() + "." +permission.getAction();
        permission.setSlug(slug);
        baseMapper.updateById(permission);
    }


    @Override
    public List<Long> doGetPermissionsIdsByRoleId(Long roleId) {

        LambdaQueryWrapper<AdminRolePermission> rpWrapper = new LambdaQueryWrapper<>();
        rpWrapper.eq(AdminRolePermission::getRoleId,roleId)
                .select(AdminRolePermission::getPermissionId);
        return adminRolePermissionService.listObjs(rpWrapper, (obj) -> Long.valueOf(obj.toString()));
    }


    @Override
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    @Transactional
    public Long addPermission(AdminPermissionDTO permission) {
        checkValid(permission.getCategory(),permission.getAction(),null);
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(permission,adminPermission,"slug");

        String slug = adminPermission.getCategory() + "." +adminPermission.getAction();
        adminPermission.setSlug(slug);
        baseMapper.insert(adminPermission);
        return adminPermission.getId();
    }

//    @Override
//    public List<PermissionDTO> getPermissionsByUserId(Long id) {
//        List<Long> permissionIds;
//        if(id!=1){
//            LambdaQueryWrapper<AdminUserRole> urWrapper = new LambdaQueryWrapper<>();
//            // get role ids by user id.
//            urWrapper.eq(AdminUserRole::getUserId,id).select(AdminUserRole::getRoleId);
//            List<Long> roleIds = userRoleService.listObjs(urWrapper, (obj) -> Long.valueOf(obj.toString()));
//            LambdaQueryWrapper<AdminRolePermission> rpWrapper = new LambdaQueryWrapper<>();
//            Assert.notEmpty(roleIds,"Invalid User");
//            rpWrapper.in(AdminRolePermission::getRoleId,roleIds).select(AdminRolePermission::getPermissionId);
//            permissionIds = adminRolePermissionService.listObjs(rpWrapper, (obj) -> Long.valueOf(obj.toString()));
//        }else{
//            LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(AdminPermission::getEnable,true)
//                    .ne(AdminPermission::getAction, PermissionAction.INVALID)
//                    .select(AdminPermission::getId);
//            permissionIds = listObjs(wrapper, (obj) -> Long.valueOf(obj.toString()));
//        }
//
//    }

//    private Long getGrandParent(Map<Long, AdminPermission> map, Long id){
//        return map.get(map.get(id).getPid()).getPid();

    @Override
    public PageResponse<AdminPermission> getPage(PageQueryVo vo) {
        LambdaQueryWrapper<AdminPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.select(AdminPermission::getId,AdminPermission::getName, AdminPermission::getCategory,
                AdminPermission::getAction,
                AdminPermission::getSlug, AdminPermission::getDescription, AdminPermission::getEnable);
//        wrapper.orderBy(AdminPermission::getCategory);
        wrapper.orderByAsc(AdminPermission::getCategory);
        Page<AdminPermission> page = new Page<>(vo.page(), vo.perPage());
        baseMapper.selectPage(page,wrapper);
        return PageUtil.toBean(page);
    }

    @Override
    public List<AdminPermissionTree> doGetAllPermission() {
        LambdaQueryWrapper<AdminPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.select(AdminPermission::getId, AdminPermission::getName, AdminPermission::getCategory)
                .isNotNull(AdminPermission::getCategory)
                .eq(AdminPermission::getEnable,true);

        List<AdminPermission> permissions = baseMapper.selectList(wrapper);
        List<String> groups = permissions.stream().map(AdminPermission::getCategory).distinct().toList();
        HashMap<String, AdminPermissionTree>  rootMap= new HashMap<>();
        for (int i = 0; i < groups.size(); i++) {
            AdminPermissionTree temp = new AdminPermissionTree(groups.get(i), i);
             rootMap.put(groups.get(i),temp);
        }
        permissions.forEach(permission ->{
            List<AdminPermissionTree> children = rootMap.get(permission.getCategory()).getChildren();
            children.add(new AdminPermissionTree(permission.getName(),permission.getId(),null));
        });

        return rootMap.values().stream().toList();

    }
    private void checkValid(String category, PermissionAction action, Long id){
        LambdaQueryWrapper<AdminPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AdminPermission::getCategory, category).eq(AdminPermission::getAction, action)
                .ne(id!=null, AdminPermission::getId, id);
        if(baseMapper.exists(wrapper)){
            throw new CustomizedException(20001,"Duplicated Permission");
        }
    }
}
