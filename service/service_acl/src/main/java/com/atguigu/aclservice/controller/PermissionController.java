package com.atguigu.aclservice.controller;


import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 菜单管理
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/permission")
//@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("查询所有菜单")
    @GetMapping
    public R indexAllPermission() {
        List<Permission> list = permissionService.queryAllMenu();
        return R.ok().data("children",list);
    }

    @ApiOperation("递归删除菜单")
    @DeleteMapping("remove/{id}")
    public R remove(@ApiParam("要删除的菜单id") @PathVariable String id) {
        permissionService.removeChildById(id);
        return R.ok();
    }

    @ApiOperation("给角色分配权限")
    @PostMapping("/doAssign")
    public R doAssign(@ApiParam("角色id") @RequestParam("roleId") String roleId,
                      @ApiParam("权限id") @RequestParam("permissionId") String[] permissionId) {
        permissionService.saveRolePermissionRealtionShip(roleId,permissionId);
        return R.ok();
    }
    /*
    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public R doAssign(String roleId,String[] permissionId) {
        permissionService.saveRolePermissionRealtionShipGuli(roleId,permissionId);
        return R.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public R toAssign(@PathVariable String roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return R.ok().data("children", list);
    }



    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public R save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public R updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }*/

}

