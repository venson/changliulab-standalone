package com.venson.changliulabstandalone.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulabstandalone.constant.AdminCacheConst;
import com.venson.changliulabstandalone.entity.*;
import com.venson.changliulabstandalone.entity.dto.AdminPermissionDTO;
import com.venson.changliulabstandalone.entity.dto.MenuDTO;
import com.venson.changliulabstandalone.entity.pojo.AdminPermission;
import com.venson.changliulabstandalone.entity.pojo.AdminRolePermission;
import com.venson.changliulabstandalone.entity.pojo.AdminUser;
import com.venson.changliulabstandalone.entity.pojo.AdminUserRole;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.MenuHelper;
import com.venson.changliulabstandalone.mapper.AdminPermissionMapper;
import com.venson.changliulabstandalone.service.admin.AdminPermissionService;
import com.venson.changliulabstandalone.service.admin.AdminRolePermissionService;
import com.venson.changliulabstandalone.service.admin.AdminUserRoleService;
import com.venson.changliulabstandalone.service.admin.AdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.utils.MultiMapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public List<JSONObject> selectPermissionByUserId(Long userId) {
        List<AdminPermission> selectAdminPermissionList;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectAdminPermissionList = baseMapper.selectList(null);
        } else {
            selectAdminPermissionList = baseMapper.selectPermissionByUserId(userId);
        }
//        List<Permission> permissionList = PermissionHelper.build(finalPermissionList);
        List<AdminPermissionDTO> dtolist = buildTreeListByPermissionList(selectAdminPermissionList);
//        return MenuHelper.buildNew(selectAdminPermissionList);
        return MenuHelper.build(dtolist);
    }

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

    /**
     * a recursive helper method to get children permissionDTO
     * @param parentDto the parent permissionDTO
     * @param pidPermissionDTOMap the map use pid(parentId) as key , LinkedHashSet of permissionDTO as value
     * @return children permissionDTO list
     */
    private List<AdminPermissionDTO> getChildrenPermissionDTO(AdminPermissionDTO parentDto,
                                                                     Map<Long,LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOMap) {
        LinkedHashSet<AdminPermissionDTO> set = pidPermissionDTOMap.get(parentDto.getId());
        if(ObjectUtils.isEmpty(set)){
            return null;
        }
        List<AdminPermissionDTO> dtoList = set.stream().toList();

        for (AdminPermissionDTO it : dtoList) {
                int level =parentDto.getLevel() + 1;
                it.setLevel(level);
                it.setChildren(getChildrenPermissionDTO(it,pidPermissionDTOMap));
        }
        return dtoList;
    }


    //========================递归查询所有菜单================================================
    @Override
    public List<AdminPermissionDTO> doGetAllPermissionTree() {
        List<AdminPermission> permissionList = getAllPermissions(true);
        //1 查询菜单表所有数据
        //2 把查询所有菜单list集合按照要求进行封装
        return buildTreeListByPermissionList(permissionList);
    }

    public List<AdminPermissionDTO> buildTreeListByPermissionList(List<AdminPermission> permissionList) {

        HashMap<Long, LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOList = createBasePidPermissionDTOMap(permissionList);
        LinkedHashSet<AdminPermissionDTO> set = MultiMapUtils.get(1L, pidPermissionDTOList);
        List<AdminPermissionDTO> treeList = set.stream().toList();
        for (AdminPermissionDTO dto :
                treeList) {
            dto.setChildren(getChildrenPermissionDTO(dto, pidPermissionDTOList));
        }
        return treeList;
    }

    private HashMap<Long, LinkedHashSet<AdminPermissionDTO>> createBasePidPermissionDTOMap(List<AdminPermission> permissionList) {
        HashMap<Long,LinkedHashSet<AdminPermissionDTO>> pidPermissionDTOList = new HashMap<>();

        for (AdminPermission permission: permissionList) {
            AdminPermissionDTO dto = new AdminPermissionDTO();
            BeanUtils.copyProperties(permission,dto);
            if (1 ==permission.getPid()) {
                dto.setLevel(1);
            }
            MultiMapUtils.put(permission.getPid(),dto,pidPermissionDTOList);
        }
        return pidPermissionDTOList;
    }


    //============递归删除菜单==================================
    @Override
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public void doRemovePermissionById(Long id) {
        //1 创建list集合，用于封装所有删除菜单id值
        List<Long> idList = new ArrayList<>();
        //2 向idList集合设置删除菜单id
        this.selectPermissionChildById(id,idList);
        //把当前id封装到list里面
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
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
    public void saveRolePermissionRelationShipLab(Long roleId,Long[] permissionIds) {
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
        permission.setIcon(adminPermission.getIcon());
        permission.setPermissionValue(adminPermission.getPermissionValue());
        permission.setComponent(adminPermission.getComponent());
        permission.setName(adminPermission.getName());
        permission.setStatus(adminPermission.getStatus());
        permission.setPath(adminPermission.getPath());
        baseMapper.updateById(permission);
    }

    @Override
    public List<MenuDTO> doGetMenus(Long id) {

        List<AdminPermission> allPermissions = getAllPermissions(false);
//        HashMap<Long, AdminPermission> allPermissionsMap = new HashMap<>();
        HashMap<Long, MenuDTO> menuMap = new HashMap<>();
        Map<Long, AdminPermission> allPermissionsMap = allPermissions.stream().collect(Collectors.toMap(AdminPermission::getId, Function.identity()));
//        allPermissions.forEach(o->allPermissionsMap.put(o.getId(),o));
        List<Long> permissionIds;
        if(id!=1){
            LambdaQueryWrapper<AdminUserRole> urWrapper = new LambdaQueryWrapper<>();
            // get role ids by user id.
            urWrapper.eq(AdminUserRole::getUserId,id).select(AdminUserRole::getRoleId);
            List<Long> roleIds = userRoleService.listObjs(urWrapper, (obj) -> Long.valueOf(obj.toString()));
            LambdaQueryWrapper<AdminRolePermission> rpWrapper = new LambdaQueryWrapper<>();
            Assert.notEmpty(roleIds,"Invalid user");
            rpWrapper.in(AdminRolePermission::getRoleId,roleIds).select(AdminRolePermission::getPermissionId);
            permissionIds = adminRolePermissionService.listObjs(rpWrapper, (obj) -> Long.valueOf(obj.toString()));
        }else{
            LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.ne(AdminPermission::getPid,0).select(AdminPermission::getId);
            permissionIds = listObjs(wrapper, (obj) -> Long.valueOf(obj.toString()));
        }
        permissionIds.forEach(o->createMenu(allPermissionsMap,menuMap,o));
        return new ArrayList<>(menuMap.values());
    }

    @Override
    public List<Long> doGetPermissionsIdsByRoleId(Long roleId) {

        LambdaQueryWrapper<AdminRolePermission> rpWrapper = new LambdaQueryWrapper<>();
        rpWrapper.eq(AdminRolePermission::getRoleId,roleId)
                .select(AdminRolePermission::getPermissionId);
        return adminRolePermissionService.listObjs(rpWrapper, (obj) -> Long.valueOf(obj.toString()));
    }

    @Override
    public List<Long> getIgnorePermissionIds() {
        LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(AdminPermission::getType,2).select(AdminPermission::getId);
        return listObjs(wrapper,o->Long.valueOf(o.toString()));
    }

    @Override
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public void addPermission(AdminPermissionDTO permission) {
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(permission,adminPermission);
        if(permission.getLevel()==3){
            adminPermission.setType(2);
        }else{
            adminPermission.setType(1);
        }
        baseMapper.insert(adminPermission);
    }

    @Override
    public List<Long> getMenuPermissionIdsByUserId(Long id) {
        if(id!=1){
            LambdaQueryWrapper<AdminUserRole> urWrapper = new LambdaQueryWrapper<>();
            // get role ids by user id.
            urWrapper.eq(AdminUserRole::getUserId,id).select(AdminUserRole::getRoleId);
            List<Long> roleIds = userRoleService.listObjs(urWrapper, (obj) -> Long.valueOf(obj.toString()));
            LambdaQueryWrapper<AdminRolePermission> rpWrapper = new LambdaQueryWrapper<>();
            Assert.notEmpty(roleIds,"Invalid User");
            rpWrapper.in(AdminRolePermission::getRoleId,roleIds).select(AdminRolePermission::getPermissionId);
            return adminRolePermissionService.listObjs(rpWrapper, (obj) -> Long.valueOf(obj.toString()));
        }else{
            LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.ne(AdminPermission::getPid,0).select(AdminPermission::getId);
            return listObjs(wrapper, (obj) -> Long.valueOf(obj.toString()));
        }
    }

    private void createMenu(Map<Long, AdminPermission> map,Map<Long, MenuDTO> menuMap, Long id){
        AdminPermission permission = map.get(id);
        if(permission==null){
            throw new CustomizedException(20001,"Permission Structure crypt");
        }
        if(permission.getPid()==1 && menuMap.containsKey(id)) return;
        MenuDTO menu = new MenuDTO();
        menu.setPath(permission.getPath());
        menu.setName("name_"+ permission.getId());
        menu.setComponent(permission.getComponent());
        menu.setMeta(new MenuMeta(permission.getName(), permission.getType()==2, permission.getIcon()));
        if(permission.getPid()==1){
            menuMap.put(permission.getId(),menu);
        } else if (permission.getType() == 1) {
            createMenu(map,menuMap, permission.getPid());
            menuMap.get(permission.getPid()).getChildren().add(menu);
        }else if(permission.getType()==2){
//            AdminPermission parentPermission = map.get(permission.getPid());
            Long grandParent = map.get(permission.getPid()).getPid();
            createMenu(map,menuMap,grandParent);

            menuMap.get(grandParent).getChildren().add(menu);
        }
    }
//    private Long getGrandParent(Map<Long, AdminPermission> map, Long id){
//        return map.get(map.get(id).getPid()).getPid();
//    }
    @Override
    public List<AdminPermission> getAllPermissions(boolean allPermission){
            LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(AdminPermission::getId)
                    .ne(AdminPermission::getType, 0)
                    .ne(!allPermission, AdminPermission::getType, 2)
                    .select(AdminPermission::getId, AdminPermission::getName, AdminPermission::getPath,
                            AdminPermission::getPid, AdminPermission::getComponent, AdminPermission::getPermissionValue,
                            AdminPermission::getType, AdminPermission::getIcon);
        return baseMapper.selectList(wrapper);
    }
}
