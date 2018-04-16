package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.Menu;

import java.util.List;

/*
*hxp(hxpwangyi@126.com)
*2017年9月7日
*
*/
@MyBatisDao
public interface MenuMapper extends GenericMapper<Menu> {
	List<Menu> getMenuTopLever();
	List<Menu> getMenuChildren(int pid);
	void updateMenu(Menu menu);
}
