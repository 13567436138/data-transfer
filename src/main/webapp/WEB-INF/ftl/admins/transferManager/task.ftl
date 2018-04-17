<#assign contextPath=request.contextPath />

<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html>
  <head>
    <title>menu</title>
   <base id="base" href="${contextPath}">  
   <#include "/common/common.ftl">
   <script type="text/javascript"  charset="UTF-8">
   var searchUrl = "${contextPath}/transfer/task/list/data";
   var updateUrl = "${contextPath}/departments/update.do";
   var insertUrl = "${contextPath}/transfer/mainTask/add";
   var deleteUrl = "${contextPath}/departments/delete.do";
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
                   		{field:'id',title:'任务编号',width:100,align:'center'},
                   		{field:'mainTaskName',title:'主任务名称',width:400,align:'center'},
                   		{field:'recordCount',title:'记录数',width:150,align:'center'},
                   		{field:'successCount',title:'成功记录数',width:150,align:'center'},
						{field:'failCount',title:'失败记录数',width:150,align:'center'},
						{field:'startTime',title:'开始时间',width:150,align:'center'},
                		{field:'stopTime',title:'结束时间',width:150,align:'center'},
                		{field:'recordModifyTimeBegin',title:'记录开始时间',width:150,align:'center'},
                		{field:'recordModifyTimeEnd',title:'记录结束时间',width:150,align:'center'},
                		{field:'status',title:'状态',width:150,align:'center'}
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
	</script>
  </head>
  
  <body class="easyui-layout" >
	<div  region="center" >
		<div id='dataList'>
			<div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="enableTask({title:'启动'})">启动任务</a>|
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="reenableTask({title:'重新启动'});">重新启动任务</a>|
		</div>
		<div>
			<form  id='searchForm' action="" method="post">
				主任务名称:
				<input type="text" id="mainTaskName" name="mainTaskName"/>
                任务开始时间:
                <input id="startTimeStr" name="startTimeStr" type="text" class="easyui-datebox" required="required" style="width: 200px;">
                任务结束时间:
                <input id="stopTimeStr" name="stopTimeStr" type="text" class="easyui-datebox" required="required"></br>
                记录开始时间:
                <input id="recordModifyTimeBeginStr" name="recordModifyTimeBeginStr" type="text" class="easyui-datebox" required="required">
                记录结束时间:
                <input id="recordModifyTimeEndStr" name="recordModifyTimeEndStr" type="text" class="easyui-datebox" required="required">
                任务状态:
                <select name="status">
					<option value="0">全部</option>
					<option value="1">新建</option>
                    <option value="2">启动运行中</option>
                    <option value="3">成功</option>
                    <option value="4">重新启动运行中</option>
                    <option value="5">重新启动成功</option>
                    <option value="6">失败</option>
                    <option value="7">重新启动失败</option>
				</select>
				<input type="button" onclick="loadList(1);" value="查询"/>
			</form>
		</div>
	</div>
		</div>
	</div>
	

	
  </body>
</html>
