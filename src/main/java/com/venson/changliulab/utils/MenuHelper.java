//package com.venson.changliulabstandalone.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson2.JSON;
//import com.venson.changliulabstandalone.entity.dto.AdminPermissionDTO;
//import org.springframework.util.ObjectUtils;
//
//import java.util.*;
//
///**
// * <p>
// * 根据权限数据构建登录用户左侧菜单数据
// * </p>
// *
// * @author qy
// * @since 2019-11-11
// */
//public class MenuHelper {
//
//    /**
//     * 构建菜单
//     */
//
//    private static JSONObject getSubPermissionJSON(AdminPermissionDTO permission, HashMap<Long, LinkedHashSet<AdminPermissionDTO>> pidPermissionMap) {
//        Long id = permission.getId();
//        JSONObject temp= new JSONObject();
//        JSONObject meta = new JSONObject();
//        temp.put("path",permission.getPath());
//        temp.put("component",permission.getComponent());
//        temp.put("name", "name_"+permission.getId());
//        if(permission.getPid()==1){
//            temp.put("hidden",false);
//            temp.put("redirect", "noredirect");
//            meta.put("icon",permission.getIcon());
//        }else{
//            temp.put("hidden", true);
//        }
//
//        List<JSONObject> childrenList = new ArrayList<>();
//        meta.put("title",permission.getName());
//        temp.put("meta",meta);
//        if(!pidPermissionMap.containsKey(id) && !ObjectUtils.isEmpty(permission.getPath())){
//            return temp;
//        }
//        if(pidPermissionMap.containsKey(id)){
//            LinkedHashSet<AdminPermissionDTO> set = pidPermissionMap.get(id);
//            for (AdminPermissionDTO p:
//                    set) {
//                JSONObject subPermissionJSON = getSubPermissionJSON(p, pidPermissionMap);
//                List children = subPermissionJSON.getObject("children", List.class);
//                if(!ObjectUtils.isEmpty(children)){
//                    List<JSONObject> subChildrenList = JSON.parseArray(JSON.toJSONString(children), JSONObject.class);
//                    childrenList.addAll(subChildrenList);
//                    subPermissionJSON.remove("children");
//                }
//                childrenList.add(subPermissionJSON);
//            }
//            temp.put("children",childrenList);
//        }
//        return temp;
//    }
//
//
//    public static List<JSONObject> buildNew(List<AdminPermissionDTO> permissionList) {
//        HashMap<Long, LinkedHashSet<AdminPermissionDTO>> pidPermissionMap = new HashMap<>();
//        for (AdminPermissionDTO permission :
//                permissionList) {
//            MultiMapUtils.put(permission.getPid(), permission, pidPermissionMap);
//
//        }
//        List<AdminPermissionDTO> rootPermission = pidPermissionMap.get(1L).stream().toList();
//        List<JSONObject> menus = new ArrayList<>();
//
//        for (AdminPermissionDTO permission :
//                rootPermission) {
//            JSONObject subMenu = getSubPermissionJSON(permission,pidPermissionMap);
//            menus.add(subMenu);
//        }
//        return menus;
//
//    }
////    @Deprecated
//    public static List<JSONObject> build(List<AdminPermissionDTO> treeNodes) {
//        List<JSONObject> menus = new ArrayList<>();
//        if(treeNodes.size() == 1) {
//            AdminPermissionDTO topNode = treeNodes.get(0);
//            //左侧一级菜单
//            List<AdminPermissionDTO> oneMenuList = topNode.getChildren();
//            for(AdminPermissionDTO one :oneMenuList) {
//                JSONObject oneMenu = new JSONObject();
//                oneMenu.put("path", one.getPath());
//                oneMenu.put("component", one.getComponent());
//                oneMenu.put("redirect", "noredirect");
//                oneMenu.put("name", "name_"+one.getId());
//                oneMenu.put("hidden", false);
//
//                JSONObject oneMeta = new JSONObject();
//                oneMeta.put("title", one.getName());
//                oneMeta.put("icon", one.getIcon());
//                oneMenu.put("meta", oneMeta);
//
//                List<JSONObject> children = new ArrayList<>();
//                List<AdminPermissionDTO> twoMenuList = one.getChildren();
//                for(AdminPermissionDTO two :twoMenuList) {
//                    JSONObject twoMenu = new JSONObject();
//                    twoMenu.put("path", two.getPath());
//                    twoMenu.put("component", two.getComponent());
//                    twoMenu.put("name", "name_"+two.getId());
//                    twoMenu.put("hidden", false);
//
//                    JSONObject twoMeta = new JSONObject();
//                    twoMeta.put("title", two.getName());
//                    twoMenu.put("meta", twoMeta);
//
//                    children.add(twoMenu);
//
//                    List<AdminPermissionDTO> threeMenuList = two.getChildren();
//                    for(AdminPermissionDTO three :threeMenuList) {
//                        if(ObjectUtils.isEmpty(three.getPath())) continue;
//
//                        JSONObject threeMenu = new JSONObject();
//                        threeMenu.put("path", three.getPath());
//                        threeMenu.put("component", three.getComponent());
//                        threeMenu.put("name", "name_"+three.getId());
//                        threeMenu.put("hidden", true);
//
//                        JSONObject threeMeta = new JSONObject();
//                        threeMeta.put("title", three.getName());
//                        threeMenu.put("meta", threeMeta);
//
//                        children.add(threeMenu);
//                    }
//                }
//                oneMenu.put("children", children);
//                menus.add(oneMenu);
//            }
//        }
//        return menus;
//    }
//}
