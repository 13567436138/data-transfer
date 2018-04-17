package com.youben.constant;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 14:02
 */
public class DataTransferConst {
    public static  final int DATA_SOURCE_TYPE_FROM=1; //源数据源
    public static  final int DATA_SOURCE_TYPE_TO=2; //目标数据源

    public static final int TASK_STATUS_NEW=1;//1新建，2启动运行中，3成功，4重新启动运行中，5重新启动成功，6失败，7重新启动失败
    public static final int TASK_STATUS_RUN=2;
    public static final int TASK_STATUS_SUCCESS=3;
    public static final int TASK_STATUS_RERUN=4;
    public static final int TASK_STATUS_RERUN_SUCCESS=5;
    public static final int TASK_STATUS_FAIL=6;
    public static final int TASK_STATUS_RERUN_FAIL=7;

}
