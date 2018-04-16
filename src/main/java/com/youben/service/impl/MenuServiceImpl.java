package com.youben.service.impl;

import java.util.List;

import com.youben.base.GenericServiceImpl;
import com.youben.entity.Menu;
import com.youben.mapper.MenuMapper;
import com.youben.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*
*hxp(hxpwangyi@126.com)	
*2017年9月7日
*
*/
@Service
public class MenuServiceImpl extends GenericServiceImpl<Menu> implements MenuService {

	private MenuMapper menuMapper;
	
	@Autowired(required=true)
	public MenuServiceImpl(MenuMapper menuMapper) {
		super(menuMapper);
		this.menuMapper=menuMapper;
	}

	@Override
	public List<Menu> getMenuTopLever() {
		return menuMapper.getMenuTopLever();
	}

	@Override
	public List<Menu> getMenuChildren(int pid) {
		return menuMapper.getMenuChildren(pid);
	}

	@Override
	public boolean updateMenu(Menu menu) {
		menuMapper.updateMenu(menu);
		return true;
	}


}
