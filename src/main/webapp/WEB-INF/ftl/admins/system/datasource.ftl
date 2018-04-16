<#assign contextPath=request.contextPath />

<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>menu</title>
    <base id="base" href="${contextPath}">
   <#include "/common/common.ftl">
    <script type="text/javascript"  charset="UTF-8">
        var searchUrl = "${contextPath}/menu/datasource/data";
        $(function() {
            $('#dataList').datagrid({
                title:'列表',
                iconCls:'icon-edit',//图标
                //width: 700,
                height: 'auto',
                nowrap: false,
                striped: true,
                border: true,
                collapsible:false,//是否可折叠的
                fit: true,//自动大小
                url:'#',
                remoteSort:false,
                singleSelect:true,//是否单选
                pagination:true,//分页控件
                rownumbers:true,//行号
                url:searchUrl,
                toolbar:'#tb',
                columns:[[
                    {field:'id',title:'数据源编号',width:100,align:'center'},
                    {field:'name',title:'名称',width:200,align:'center'},
                    {field:'type',title:'类型',width:100,align:'center',formatter:function(cellvalue, options, rowObject){
                            if(cellvalue==1){
                                return "from";
                            }else{
                                return "to";
                            }
                        }},
                    {field:'ip',title:'ip',width:200,align:'center'},
                    {field:'port',title:'端口',width:100,align:'center'},
                    {field:'databaseName',title:'数据库',width:200,align:'center'},
                    {field:'username',title:'用户名',width:100,align:'center'},
                    {field:'password',title:'密码',width:100,align:'center'}
                ]],

                onBeforeLoad: function (params) {
                    params.pageSize = params.rows
                    params.currentPage = params.page
                    delete params.rows
                    delete params.page
                }

            });

            //设置分页控件
            var p = $('#dataList').datagrid('getPager');
            $(p).pagination({
                pageSize: 10,//每页显示的记录条数，默认为10
                pageList: [10,20,30],//可以设置每页记录条数的列表
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            })
        });
    </script>
</head>

<body class="easyui-layout" >
<div  region="center" >
    <div id='dataList'>
        <div id="tb" style="padding:5px;height:auto">
            <div style="margin-bottom:5px">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddwindow({title:'新增'})">新增</a>|
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showUpdate({title:'修改',readonlyFields:['id']});">修改</a>|
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRowData({id:'id'});">删除</a>
            </div>
            <div>
                <form  id='searchForm' action="" method="post">
                    名称:
                    <input type="text" id="name" name="name"/>
                    ip:
                    <input type="text" id="ip" name="ip"/>
                    数据库:
                    <input type="text" id="databaseName" name="databaseName"/>
                    类型:
                    <select id="type" name="type">
                            <option value="0">所有</option>
                            <option value="1">来源</option>
                            <option value="2">目标</option>
                    </select>
                    <input type="button" onclick="loadList(1);" value="查询"/>
                </form>
            </div>
        </div>
    </div>
</div>

<div style="visibility:hidden" >
    <div id="addwindow"  title="添加" style="width:600px;height:350px;padding:10px">
        <form id='addForm' action="" method="post">
            <table>
                <tr>
                    <td>名称:</td>
                    <td><input type="text" id="name" name="name" style="width:120px"/></td>
                    <td>类型:</td>
                    <td>
                        <select name="type"  style="width:120px">
                            <option value="1">来源</option>
                            <option value="2">目标</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>ip:</td>
                    <td><input type="text" id="ip" name="ip" style="width:120px"/></td>
                    <td>端口:</td>
                    <td><input type="text" id="port" name="port" style="width:120px"/></td>
                </tr>
                <tr>
                    <td>用户名:</td>
                    <td><input type="text" id="username" name="username" style="width:120px"/></td>
                    <td>密码:</td>
                    <td><input type="text" id="密码" name="密码" style="width:120px"/></td>
                </tr>
                <tr>
                    <td>数据库:</td>
                    <td><input type="text" id="databaseName" name="databaseName" style="width:120px"/></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </form>
    </div>
</div>

</body>
</html>
