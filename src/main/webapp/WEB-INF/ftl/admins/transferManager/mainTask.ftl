<#assign contextPath=request.contextPath />

<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html>
  <head>
    <title>menu</title>
   <base id="base" href="${contextPath}">  
   <#include "/common/common.ftl">
   <script type="text/javascript"  charset="UTF-8">
   var searchUrl = "${contextPath}/transfer/mainTask/list/data";
   var updateUrl = "${contextPath}/departments/update.do";
   var insertUrl = "${contextPath}/transfer/mainTask/add";
   var deleteUrl = "${contextPath}/transfer/mainTask/delete";
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
                   		{field:'id',title:'主任务编号',width:100,align:'center'},
                   		{field:'name',title:'名称',width:400,align:'center'},
                   		{field:'fromName',title:'来源',width:500,align:'center'},
                   		{field:'toName',title:'目标',width:500,align:'center'},
						{field:'fromSource',title:'',width:1,align:'center',hidden:true},
						{field:'toSource',title:'',width:1,align:'center',hidden:true},
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
	    });

	    $.ajax({
            type: "GET",
            url: "${contextPath}/datasource/query/type",
            data: {type:1},
            dataType: "json",
            success: function(datas){
				for(var i=0;i<datas.length;i++){
				    var data=datas[i];
				    $("#fromSource").append("<option value='"+data.id+"'>"+data.name+"</option>")
				}
            }
		});
        $.ajax({
            type: "GET",
            url: "${contextPath}/datasource/query/type",
            data: {type:2},
            dataType: "json",
            success: function(datas){
                for(var i=0;i<datas.length;i++){
                    var data=datas[i];
                    $("#toSource").append("<option value='"+data.id+"'>"+data.name+"</option>")
                }
            }
        });
	});

   function createTask(jsonParam) {
       var rows = $('#dataList').datagrid("getSelections");
       if (rows.length != 1) {
           $.messager.alert('提示框', '请选择一个主任务', 'warning');
           return;
       }
       jsonParam=jsonParam||{};
       $('#createTaskForm')[0].reset();
       $('#createTaskWindow').removeAttr("disabled","disabled");
       $('#createTaskWindow input').removeAttr("readonly");

       jsonParam.title=isEmpty(jsonParam.title)?$('#createTaskWindow').attr('title'):jsonParam.title;

       initDlg('#createTaskWindow').dialog({title:jsonParam.title,buttons:[{
               text:'迁移规模',
               iconCls:'icon-ok',
               handler:function(){
                   $.ajax({
                       type: "POST",
                       url: "${contextPath}/transfer/task/countRecord",
                       data: {mainTaskId:rows[0].id,recordStartTime:$("#recordStartTime").val(),recordEndTime:$("#recordEndTime").val()},
                       dataType: "json",
                       success: function(data){
                           showBox("提示信息", "一共需要迁移"+data+"条数据", 'info');
                       }
                   });

               }
           },{
               text:'创建',
               iconCls:'icon-ok',
               handler:function(){
                   $.ajax({
                       type: "POST",
                       url: "${contextPath}/transfer/task/create",
                       data: {mainTaskId:rows[0].id,name:$("#craateTaskName").val(),recordStartTime:$("#recordStartTime").val(),recordEndTime:$("#recordEndTime").val()},
                       dataType: "json",
                       success: function(data){
                           $('#createTaskForm').form('clear'); // 清空form
                           $('#dataList').datagrid('clearSelections');//清空选择
                           $('#createTaskWindow').dialog('close');
                           if (data.result=='ok') {
                               showBox("提示信息", "创建成功", 'info');
                           } else {
                               showError(data);
                           }
                       }
                   });

               }
           },{
               text:'取消',
               iconCls:'icon-cancel',
               handler:function(){
                   //取消前处理
                   if(typeof jsonParam.clearHandler  === "function")
                       jsonParam.clearHandler();
                   else
                       $('#createTaskWindow').dialog('close');
               }
           }]});
       $('#createTaskWindow').dialog('open');
      }
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
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="createTask({title:'创建任务'});">创建任务</a>
		</div>
		<div>
			<form  id='searchForm' action="" method="post">
				名称:
				<input type="text" id="name" name="name"/>
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
							<td>任务名称:</td>
							<td><input type="text" id="name" name="name" style="width:420px"/></td>
						</tr>
						<tr>
							<td>来源:</td>
							<td>
								<select id="fromSource" name="fromSource" style="width:420px">

								</select>
							</td>
						</tr>
						<tr>
							<td>目标:</td>
                            <td>
								<select id="toSource" name="toSource" style="width:420px">

								</select>
							</td>
						</tr>
						
				</table>
			</form>
		</div>

	</div>
  <div style="visibility:hidden" >
      <div id="createTaskWindow"  title="创建任务" style="width:600px;height:350px;padding:10px">
          <form id='createTaskForm' action="" method="post">
              <table>
                  <tr>
                      <td>任务名称:</td>
                      <td><input type="text" id="craateTaskName" name="name" style="width:420px"/></td>
                  </tr>
                  <tr>
                      <td>开始时间:</td>
                      <td>
                          <input id="recordStartTime" type="text" class="easyui-datebox" required="required">
                      </td>
                  </tr>
                  <tr>
                      <td>结束时间:</td>
                      <td>
                          <input id="recordEndTime" type="text" class="easyui-datebox" required="required">
                      </td>
                  </tr>

              </table>
          </form>
      </div>
  </div>
	
  </body>
</html>
