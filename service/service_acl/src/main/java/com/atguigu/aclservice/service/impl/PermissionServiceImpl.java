package com.atguigu.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.helper.MemuHelper;
import com.atguigu.aclservice.helper.PermissionHelper;
import com.atguigu.aclservice.mapper.PermissionMapper;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RolePermissionService;
import com.atguigu.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;

    @Override
    public List<Permission> queryAllMenu() {

        //查询全部菜单数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        //把查询出的所有菜单list集合按要求进行封装
        List<Permission> resultList = buildPermission(permissionList);
        return resultList;
    }

    //把查询出的所有菜单list集合按要求进行封装的方法
    public static List<Permission> buildPermission(List<Permission> permissionList) {

        //用于最终封装数据的list集合
        List<Permission> finalNode = new ArrayList<>();
        
        //遍历所有菜单,得到pid等于0的，即为入口顶层菜单，设置level为1
        for (Permission permissionNode : permissionList) {
            if ("0".equals(permissionNode.getPid())) {
                permissionNode.setLevel(1);
                //根据顶层菜单继续进行子查询，封装到finalNode集合里
                finalNode.add(selectChildren(permissionNode,permissionList));
            }
        }
        return finalNode;
    }

    //根据顶层菜单继续进行子查询，封装到finalNode集合里
    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        //一级菜单里面放二级，二级放三级......先初始化
        permissionNode.setChildren(new ArrayList<Permission>());
        for (Permission it : permissionList) {
            if (permissionNode.getId().equals(it.getPid())) {
                //把父菜单的level等级+1赋值给当前菜单的level
                int level = permissionNode.getLevel()+1;
                it.setLevel(level);
                if (permissionNode.getChildren() == null) {
                    //如果children为空，进行初始化，避免null值
                    permissionNode.setChildren(new ArrayList<Permission>());
                }
                //把查询的子菜单放入到父菜单中
                permissionNode.getChildren().add(selectChildren(it,permissionList));
            }
        }
        return permissionNode;
    }

    //递归删除菜单及子菜单
    @Override
    public void removeChildById(String id) {

        //创建一个用于保存要被删除菜单的id集合
        List<String> idList = new ArrayList<>();
        this.selectPermissionChildById(id,idList);
        idList.add(id);
        //根据id集合批量删除菜单
        baseMapper.deleteBatchIds(idList);
    }

    //根据要删除菜单的id查询出全部子菜单，放入list集合中
    private void selectPermissionChildById(String id, List<String> idList) {
        //第一种需要查询全部,if判断pid关联,效率太低
        /*QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        for (Permission item : permissionList) {
            if (id.equals(item.getPid())) {
                idList.add(item.getId());
                this.selectPermissionChildById(item.getId(),idList);
            }
        }*/

        //第二种不需要if判断,直接wrapper条件过滤pid关联,效率高
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        //设置查询条件为pid等于传入的id,这样遍历的时候数据少，会快很多
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        for (Permission item : permissionList) {
            idList.add(item.getId());
            this.selectPermissionChildById(item.getId(),idList);
        }
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {

        //创建用于封装角色权限表数据的list
        List<RolePermission> rolePermissionList = new ArrayList<>();

        //遍历权限，将角色id和权限id对应添加到list集合中
        for (String perId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            rolePermissionList.add(rolePermission);
        }
        //调用rolePermissionService的批量保存方法，保存数据到角色权限表中
        rolePermissionService.saveBatch(rolePermissionList);
    }


    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (int i = 0; i < allPermissionList.size(); i++) {
            Permission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                RolePermission rolePermission = rolePermissionList.get(m);
                if(rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }


        List<Permission> permissionList = bulid(allPermissionList);
        return permissionList;
    }

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    private static List<Permission> bulid(List<Permission> treeNodes) {
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }
}
