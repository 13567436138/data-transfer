package com.youben.service;

import com.youben.base.GenericService;
import com.youben.entity.Datasource;

import java.util.List;

public interface DatasourceService extends GenericService<Datasource> {
    List<Datasource> queryByType(int type);
}
