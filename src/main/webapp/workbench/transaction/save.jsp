<%@ page import="org.apache.ibatis.session.SqlSession" %>
<%@ page import="com.bjpowernode.crm.settings.domain.User" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";

    Map<String,String>pMap = (Map) application.getAttribute("pMap");
    Set<String> set = pMap.keySet();
%>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery\bs_typeahead\bootstrap3-typeahead.min.js"></script>

	<script type="text/javascript">
		//var json = {"01资质审查"：10,"02需求分析":25...}
        var json = {
            <%
                for (String key:set){
                    String value = pMap.get(key);
            %>

                    "<%=key%>" : <%=value%>,
            //这是一个前端手写的js，两个大括号就代表是json。最后的逗号js会自动帮我们屏蔽掉，不用再去判断了

            <%
                }

            %>

        };
        //[object object]
        //alert(json)

        /*
		*
		* 	关于阶段和可能性
		* 		是一种一一对应的关系
		* 		一个阶段对应一个可能性
		* 		我们现在可以将阶段和可能性想象成一种键值对之间的关系
		* 		以阶段为key，通过选中的阶段，触发可能性value
		*
		* 		Stage			possibility
		* 		key				value
		* 		01资质审查		10
		* 		02需求分析		25
		* 		03价值建议		40
		* 		...				...
		* 		...				...
		* 		07成交			100
		* 		08..			0
		* 		09..			0
		*
		* 		对于以上的数据，通过观察的到的结论
		* 			(1)数据量不是很大
		* 			(2)这是一种键值对的对应关系
		*
		* 		  如果同时满足以上两种结论，那么我们将这样的数据保存到数据库表中就没什么意义
		* 		  如果遇到这种情况，我们需要用到properties属性文件来进行保存
		* 		  stage2Possibility.properties
		* 		  01资质审查=10
		* 		  02需求分析=20
		* 		  ...
		*
		*
		* 		  stage2Possibility.properties这个文件表示的是阶段和键值对之间的对应关系
		* 		  将来，我们通过stage，以及对应关系，来取得可能性这个值
		* 		  这种需求在交易模块中需要大量的使用到
		*
		* 		  所以我们就需要将该文件解析在服务器的缓存中
		* 	      application.setAttribute(stage2Possibility.properties文件内容)
		*
		* */

		$(function () {
			//日历控件1向下显示
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "botom-left"
			});

			//日历控件2向上显示
			$(".time2").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "top-left"
			});

			//自动补全插件关键代码
			$("#create-customerName").typeahead({
				source: function (query, process) {
					$.get(
							"workbench/tran/getCustomerName.do",
							{ "name" : query },
							function (data) {
								//alert(data);
								/*
								* 	data
								* 		[{客户名称1}，{2}，{3}]
								*
								* */
								process(data);
							},
							"json"
					);
				},
				delay: 1500
			});

			//获得用户列表动态填充所有者下拉框、默认选中当前用户
			/*$.ajax({
				url:"workbench/tran/getUserList.do",
				type:"post",
				dataType:"json",
				success(result){
					var html = '<option></option>';

					$.each(result,function (i,n) {
					  html+='<option value="'+n.id+'">'+n.name+'</option>';
					})

					$("#create-transactionOwner").html(html);
					$("#create-transactionOwner").val("${sessionScope.get("user").getId()}");
				}
			})*/

			//处理市场活动源的放大镜，打开模态窗口
			$("#activitySrc").click(function () {
				$("#aname").val("");

				$.ajax({
					url:"workbench/tran/getActivities.do",
					type:"get",
					dataType:"json",
					success(result){
					var html = '';
					$.each(result,function (i,n) {
						html+='<tr>';
						html+='<td><input type="radio" name="activitySource" value="'+n.id+'"/></td>';
						html+='<td id="a'+n.id+'">'+n.name+'</td>';
						html+='<td>'+n.startDate+'</td>';
						html+='<td>'+n.endDate+'</td>';
						html+='<td>'+n.owner+'</td>';
						html+='</tr>';
					})
						$("#tBody2").html(html);
					}

				})
				$("#findMarketActivity").modal("show");
			})

			//处理市场活动源模态窗口中的提交按钮
			$("#submitActivityBtn").click(function () {

				var id = $(":radio[name=activitySource]:checked").val();
				$("#activityId").val(id);
				$("#create-activitySrc").val($("#a"+id).text());
				$("#findMarketActivity").modal("hide");

			})

			//处理市场活动模态窗口中的搜索栏
			$("#aname").keydown(function (event) {
				if(event.keyCode==13){
					$.ajax({
						url:"workbench/tran/getActivityListByName.do",
						type:"get",
						data:{
							aname:$.trim($("#aname").val())
						},
						dataType:"json",
						success(result){
							var html = '';
							$.each(result,function (i,n) {
								html+='<tr>';
								html+='<td><input type="radio" name="activitySource" value="'+n.id+'"/></td>';
								html+='<td id="a'+n.id+'">'+n.name+'</td>';
								html+='<td>'+n.startDate+'</td>';
								html+='<td>'+n.endDate+'</td>';
								html+='<td>'+n.owner+'</td>';
								html+='</tr>';
							})
							$("#tBody2").html(html);
						}
					})
					return false;
				}

			})


			//处理联系人名称的放大镜，打开模态窗口
			$("#contactName").click(function () {
				$("#cname").val("");
				$.ajax({
					url:"workbench/tran/getAllContacts.do",
					type:"post",
					dataType:"json",
					success(result){
						var html = '';
						$.each(result,function (i,n) {
						    html+='<tr>';
							html+='<td><input type="radio" name="contact" value="'+n.id+'"/></td>';
							html+='<td id="c'+n.id+'">'+n.fullname+'</td>';
							html+='<td>'+n.email+'</td>';
							html+='<td>'+n.mphone+'</td>';
						})
						$("#tBody3").html(html);
					}
				})
				$("#findContacts").modal("show");
			})

			//处理查找联系人模态窗口中的提交按钮
			$("#submitContactBtn").click(function () {
				var id = $(":radio[name=contact]:checked").val();
				$("#contactId").val(id);
				$("#create-contactsName").val($("#c"+id).text());
				$("#findContacts").modal("hide");
			})

			//处理查找联系人中的搜索框
			$("#cname").keydown(function (event) {
				if(event.keyCode==13){
					$.ajax({
						url:"workbench/tran/getContactsByName.do",
						type:"get",
						data: {
							cname:$.trim($("#cname").val())
						},
						dataType:"json",
						success(result){
							var html = '';
							$.each(result,function (i,n) {
								html+='<tr>';
								html+='<td><input type="radio" name="contact" value="'+n.id+'"/></td>';
								html+='<td id="c'+n.id+'">'+n.fullname+'</td>';
								html+='<td>'+n.email+'</td>';
								html+='<td>'+n.mphone+'</td>';
							})
							$("#tBody3").html(html);
						}
					})
					return false;
				}
			})

            //处理阶段下拉框更新可能性事件
            $("#create-transactionStage").change(function () {

                //取得选中的阶段，this表示这个Jquery对象的dom对象
                var stage = this.value;//$("#create-transactionStage").val();
                /*
                *   目标：填写可能性
                *   阶段有了stage
                *   阶段和可能性之间的对应关系pMap，但是pMap是java语言中的键值对关系（java中的map对象）
                *   我们首先得将pMap转换为js中的键值对关系json（js中只有这一种键值对形式）
                *
                *   我们要做的是将pMap
                *       pMap.get("01资质审查",10);
                *       pMap.get("02需求分析",25);
                *       ...
                *       转换为
                *
                *       var json = {"01资质审查"：10,"02需求分析":25...}
                *
                *       以上我们已经将json处理好了
                *
                *       接下来取可能性
                *
                * */

                /*
                *
                *   我们现在以json.key的形式不能取得value
                *   因为今天的stage是一个可变的变量
                *   我们要使用的取值方式为
                *   json[key]
                *
                * */
                $("#create-possibility").val(json[stage]+"%");


            })

			//为保存按钮绑定事件，执行交易添加操作
			$("#saveBtn").click(function () {
				var flag = true;

				if($.trim($("#create-transactionOwner").val())==''){
					$("#OwnerMsg").text("所有者不能为空");
					flag = false;
				}

				if($.trim($("#create-transactionName").val())==''){
					$("#NameMsg").text("交易名称不能为空");
					flag = false;
				}

				if($.trim($("#create-customerName").val())==''){
					$("#CustomerNameMsg").text("客户名称不能为空");
					flag = false;
				}


				if($.trim($("#create-expectedClosingDate").val())==''){
					$("#ExpectedDateMsg").text("预计成交日期不能为空");
					flag = false;
				}

				if($.trim($("#create-transactionStage").val())==''){
					$("#StageMsg").text("阶段不能为空");
					flag = false;
				}

				if(flag){
					$("#TranForm").submit();
				}else{
					return;
				}

			})

			//消除红字
			$("#create-transactionOwner").focus(function () {
				$("#OwnerMsg").text('');
			})
			/*
			* 		还有其他的四个红字没有完善，有时间完善
			*
			*
			* */
		})
	</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" id="aname" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="tBody2">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" id="cname"  placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="tBody3">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitContactBtn">提交</button>
				</div>
			</div>
		</div>
	</div>
	

	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;" id="TranForm" action="workbench/tran/save.do" method="post">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner" name="owner">
					<option></option>
					<c:forEach items="${requestScope.uList}" var="u">
						<option value="${u.id}" ${sessionScope.user.id eq u.id ? "selected" : ""}>${u.name}</option>
					</c:forEach>
					<%--<option>zhangsan</option>
				  <option>lisi</option>
				  <option>wangwu</option>--%>
				</select>
				<span id="OwnerMsg" style="color: red"></span>		<%--错误消息提示--%>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney" name="money" autocomplete="off">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName" name="name" autocomplete="off">
				<span id="NameMsg" style="color: red"></span>		<%--错误消息提示--%>
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-expectedClosingDate" name="expectedDate" readonly>
				<span id="ExpectedDateMsg" style="color: red"></span>		<%--错误消息提示--%>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建" name="customerName" autocomplete="off">
				<span id="CustomerNameMsg" style="color: red"></span>		<%--错误消息提示--%>
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage" name="stage">
			  	<option></option>
				  <c:forEach items="${applicationScope.stage}" var="a">
					  <option value="${a.value}">${a.text}</option>
				  </c:forEach>
			  	<%--<option>资质审查</option>
			  	<option>需求分析</option>
			  	<option>价值建议</option>
			  	<option>确定决策者</option>
			  	<option>提案/报价</option>
			  	<option>谈判/复审</option>
			  	<option>成交</option>
			  	<option>丢失的线索</option>
			  	<option>因竞争丢失关闭</option>--%>
			  </select>
				<span id="StageMsg" style="color: red"></span>		<%--错误消息提示--%>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType" name="type">
				  <option></option>
					<c:forEach items="${applicationScope.transactionType}" var="a">
						<option value="${a.value}">${a.text}</option>
					</c:forEach>
				  <%--<option>已有业务</option>
				  <option>新业务</option>--%>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" disabled >
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource" name="source">
				  <option></option>
					<c:forEach items="${applicationScope.source}" var="a">
						<option value="${a.value}">${a.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="activitySrc"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-activitySrc" readonly>
				<input type="hidden" id="activityId" name="activityId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="contactName"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName" readonly>
				<input type="hidden" name="contactId" id="contactId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time2" id="create-nextContactTime" name="nextContactTime" readonly>
			</div>
		</div>
		
	</form>
</body>
</html>