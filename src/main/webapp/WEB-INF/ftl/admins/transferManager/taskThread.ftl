<#assign contextPath=request.contextPath />

<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html>
  <head>
    <title>menu</title>
   <base id="base" href="${contextPath}">  
   <#include "/common/common.ftl">
   <script type="text/javascript"  charset="UTF-8">
   var searchUrl = "${contextPath}/transfer/thread/list/data";
   var updateUrl = "${contextPath}/departments/update.do";
   var insertUrl = "${contextPath}/departments/insert.do";
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
                   		{field:'id',title:'编号',width:100,align:'center'},
                   		{field:'mainTaskName',title:'主任务名称',width:400,align:'center'},
                   		{field:'taskName',title:'任务名称',width:400,align:'center'},
                   		{field:'name',title:'名称',width:200,align:'center'},
                   		{field:'tableName',title:'表名',width:100,align:'center'},
                   		{field:'startRecordId',title:'开始记录id',width:100,align:'center'},
						{field:'stopRecordId',title:'结束记录id',width:100,align:'center'},
						{field:'recordCount',title:'记录总数',width:100,align:'center'},
						{field:'successCount',title:'成功记录数量',width:100,align:'center'},
						{field:'failCount',title:'失败记录数量',width:100,align:'center'},
						{field:'status',title:'状态',width:100,align:'center'},
						{field:'startTime',title:'开始时间',width:100,align:'center'},
						{field:'stopTime',title:'结束时间',width:100,align:'center'},
						{field:'runCount',title:'运行次数',width:100,align:'center'}
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

        $.ajax({
            type: "GET",
            url: "${contextPath}/transfer/mainTask/all",
            data: {},
            dataType: "json",
            success: function(datas){
                for(var i=0;i<datas.length;i++){
                    var data=datas[i];
                    $("#mainTaskId").append("<option value='"+data.id+"'>"+data.name+"</option>")
                }

                $.ajax({
                    type: "GET",
                    url: "${contextPath}/transfer/task/all",
                    data: {mainTaskId:$("#mainTaskId").val()},
                    dataType: "json",
                    success: function(datas){
                        for(var i=0;i<datas.length;i++){
                            var data=datas[i];
                            $("#taskId").append("<option value='"+data.id+"'>"+data.name+"</option>")
                        }


                    }
                });
            }
        });

        $('#mainTaskId').change(function(){
            $.ajax({
                type: "GET",
                url: "${contextPath}/transfer/task/all",
                data: {mainTaskId:$("#mainTaskId").val()},
                dataType: "json",
                success: function(datas){
                    $("#taskId").empty();
                    for(var i=0;i<datas.length;i++){
                        var data=datas[i];
                        $("#taskId").append("<option value='"+data.id+"'>"+data.name+"</option>")
                    }
                }
            });
        })
	});
	</script>
  </head>
  
  <body class="easyui-layout" >
	<div  region="center" >
		<div id='dataList'>
			<div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">

		</div>
		<div>
			<form  id='searchForm' action="" method="post">
				主任务名称:
				<select id="mainTaskId" name="mainTaskId">

				</select>
				任务名称:
				<select id="taskId" name="taskId" style="width: 400px;">

				</select></br>
                名称:
                <input type="text" id="name" name="name"/>
                开始时间:
                <input id="startTimeStr" name="startTimeStr" type="text" class="easyui-datebox" required="required" style="width: 200px;">
                结束时间:
                <input id="stopTimeStr" name="stopTimeStr" type="text" class="easyui-datebox" required="required" style="width: 200px;">
                状态:
                <select name="status">
					<option value="0">全部</option>
					<option value="1>新建</option>
                    <option value="2">运行中</option>
                    <option value="3">成功</option>
                    <option value="4">失败</option>
                    <option value="5">重新运行中</option>
                    <option value="6">重新运行失败</option>
                    <option value="7">重新运行成功</option>
				</select>
                表名:
                <input type="text" id="tableName" name="tableName"/>
				<input type="button" onclick="loadList(1);" value="查询"/>
			</form>
		</div>
	</div>
		</div>
	</div>
	

  </body>
</html>
