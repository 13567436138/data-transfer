package com.youben.utils;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Date;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-18
 * Time: 18:58
 */
public class Binder {

    /**
     * @param pstmt
     * @param args
     */
    public static void bindParameters(PreparedStatement pstmt, Object... args) {
        if (args == null || args.length == 0) {
            return;
        }
        int i = 1;
        for (Object arg : args) {
            try {
                if (arg == null) {
                    pstmt.setNull(i++, Types.VARCHAR);
                    continue;
                }

                if (arg.getClass().equals(Date.class)) {
                    Date d = (Date)arg;
                    arg = new java.sql.Date(d.getTime());
                }
                TypeUtil.MethodPair methodPair = TypeUtil.getMethodPair(arg.getClass());
                if (methodPair == null) {
                    throw new Exception("未找到方法:" + arg.getClass());
                }
                Method setter = methodPair.getSetter();
                setter.invoke(pstmt, i++, arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

