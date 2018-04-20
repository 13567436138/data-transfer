package com.youben.service;

import com.youben.base.GenericService;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.MainTaskInfo;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:04
 */
public interface MainTaskInfoService extends GenericService<MainTaskInfo> {
    void updateBymainTaskId(int mainTaskId);
}
