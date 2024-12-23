-- ----------------------------
-- 1、仓库库存表
-- ----------------------------
drop table if exists sys_stroe;
create table sys_stroe (
  id           bigint(20)      not null auto_increment    comment '库存id',
  name         varchar(255)     default null                 comment '物料名称',
  code         varchar(50)     default null                 comment '物品编码',
  specification         varchar(255)     default null                 comment '规格型号',
  quantity         int          default 0                  comment '数量',
  safety_stock         int          default 0                  comment '安全库存量',
  in_data            datetime     default null               comment '入库日期',
  in_user_name        varchar(10)     default null                 comment '入库人姓名',
  in_user_id        bigint(20)     default null                 comment '入库人id',
  out_data            datetime     default null               comment '出库日期',
  out_user_name        varchar(10)     default null                 comment '出库人姓名',
  out_user_id        bigint(20)     default null                 comment '出库人id',
  supplier_id        bigint(20)     default null                 comment '供应商id',
  supplier_name        varchar(255)     default null                 comment '供应商名称',
  status             char(1)         default '0'                comment '库存状态(0正常 1停用)',
  remarks             varchar(255)     default null               comment '备注',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=200 comment = '库存表';
-- 菜单 SQL
INSERT INTO `mes`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `is_refresh`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2006, '工厂管理', 0, 5, '#', 'menuItem', 'M', '0', '1', NULL, 'fa fa-cubes', 'admin', '2024-12-21 13:21:47', '', NULL, '');
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('库存', '3', '1', '/factory/stroe', 'C', '0', 'factory:stroe:view', '#', 'admin', sysdate(), '', null, '库存菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('库存查询', @parentId, '1',  '#',  'F', '0', 'factory:stroe:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('库存新增', @parentId, '2',  '#',  'F', '0', 'factory:stroe:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('库存修改', @parentId, '3',  '#',  'F', '0', 'factory:stroe:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('库存删除', @parentId, '4',  '#',  'F', '0', 'factory:stroe:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('库存导出', @parentId, '5',  '#',  'F', '0', 'factory:stroe:export',       '#', 'admin', sysdate(), '', null, '');
