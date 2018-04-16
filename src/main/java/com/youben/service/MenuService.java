package com.youben.service;


import com.youben.base.GenericService;
import com.youben.entity.Menu;

import java.util.List;

/*
*hxp(hxpwangyi@126.com)
*2017年9月7日
*
*/
public interface MenuService extends GenericService<Menu> {
	List<Menu> getMenuTopLever();
	List<Menu> getMenuChildren(int pid);
	boolean updateMenu(Menu menu);
}	
