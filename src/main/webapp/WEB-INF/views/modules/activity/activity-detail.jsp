<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<title>活动列表</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/clipboard.js"></script>
	<script src="${ctxStatic}/layer/layer.js"></script>
	<link rel="stylesheet" href="${ctxStatic}/page.my.css">
	<script src="${ctxStatic}/page.my.js"></script>

	<style>
		.activity-title{
			font-size: 17px;
			padding: 10px 20px;
			border-bottom: 1px solid #F2F2F2;
		}
		.activity-title a{
			display: inline-block;
			width: 20px;
			height: 20px;
			margin-right: 30px;
		}
		.activity-title i img{
			width: 100%;
		}
		h3{
			font-size: 16px;
			background-color: #F7F7F7;
			line-height: 3.5;
			padding: 0 3%;
			width: 90%;
			margin: 0 auto;
			/*border: 1px solid #eee;*/
			margin-top: 20px;
			margin-bottom: 10px;
			color: #8E8E8E;
			font-weight: 400;
		}
		.base-info,.join-shop,.market-type{
			list-style: none;
			width: 90%;
			margin: 0 auto;
			padding: 0 3%;


		}
		.base-info li{
			font-size: 15px;
			line-height: 2.5;
			position: relative;

		}
		.import-deco{
			display: inline-block;
			color: red;
			position: absolute;
			top: 0;
			left: -15px;
		}
		.market-type li{
			width: 33%;
			float: left;
			font-size: 15px;
			line-height: 2.5;
			position: relative;
		}
		.join-shop li{
			font-size: 15px;
			line-height: 2.5;
		}
		.order-lists{
			width: 96%;
			margin: 0 auto;

		}
		.order-lists ul{
			list-style: none;
		}
		.order-lists ul li{
			float: left;
			text-align: center;
			font-size: 14px;
		}
		.mycol-5{
			width: 5%;
		}
		.mycol-10{
			width: 10%;
		}
		.mycol-15{
			width: 15%;
		}
		.mycol-20{
			width: 20%;
		}
		.lists-title li{
			background-color: #F7F7F7;
			line-height: 3;
			text-align: center;
		}
		.order-lists p{
			background-color: #F7F7F7;
			line-height: 3;
			margin-top: 10px;
			border: 1px solid #F0F0F0;
			margin-bottom: 0;
			color: #AAAAAA;
		}
		.order-lists ul{
			list-style: none;
			margin: 0;
		}
		.order-list li{
			float: left;
			text-align: center;
			border-bottom: 1px solid rgb(228, 228, 228);
			border-left: 1px solid rgb(228, 228, 228);
			line-height: 3;
			height: 40px;
			box-sizing: border-box;
		}
		.list-right{
			float: right;
			margin-right: 20px;
		}
		.order-detail{
			color: #485891;
			font-style: normal;
		}
		.start-time,.receive-time{
			display: inline-block;
			margin-left: 40px;
		}
	</style>
</head>
<body>
<div class="wrap">
	<p class="activity-title"><a href="javascript:history.back(-1)"><img src="${ctxStatic}/images/prev-btn.png" alt=""></a>活动详情</p>
	<h3>基本类型</h3>
	<ul class="base-info">

	</ul>
	<h3>营销类型</h3>
	<ul class="market-type clearfix">

	</ul>
	<h3>参与店铺</h3>
	<ul class="join-shop">

	</ul>
	<input id="current_page" name="current_page" type="hidden" value="1"/>
	<input id="page_size" name="page_size" type="hidden" value="10"/>
	<input id="pageCount" type="hidden" value=""/>

	<h3>订单列表</h3>
	<div class="order-lists">
		<ul class="lists-title clearfix">
			<li class="mycol-10">订单状态</li>
			<li class="mycol-10">绑定状态</li>
			<li class="mycol-10">店铺平台</li>
			<li class="mycol-15">所属店铺</li>
			<li class="mycol-15">订单编号</li>
			<li class="mycol-10">买家姓名</li>
			<li class="mycol-10">收货手机号</li>
			<li class="mycol-10">活动金额</li>
			<li class="mycol-10">评价截图</li>
		</ul>
		<div class="lists-show">

		</div>
	</div>
    <div class="pagination">
        <ul>
        </ul>
    </div>
</div>

<script>
$(function () {
    var para = GetRequest();
    $.ajax({
		url:'activityDetail',
		type:'POST',
		data:{
		    id:para.id
		},
		success:function (msg) {
			var msg = strToJson(msg);
			if(msg.code == 10000){
			    var data = msg.data;
			    var baseStr = '<li><i class="import-deco">*</i>活动名称： '+data.name+'</li>';
			    if(data.status == 0){
                    baseStr += '<li>活动状态： 未开始</li>'
				}
				else if(data.status == 1){
                    baseStr += '<li>活动状态： 进行中</li>'
                }
                else if(data.status == 2){
                    baseStr += '<li>活动状态： 暂停</li>'
                }
                else if(data.status == 3){
                    baseStr += '<li>活动状态： 已结束</li>'
                }
                baseStr += '<li><i class="import-deco">*</i>活动类型： 活动类型'+data.activity_type+'</li>'
                baseStr += '<li>有效时间： '+data.active_date+' 至 '+data.inactive_date+'</li>';
                baseStr += '<li>订单时效： '+data.order_active_date+' 至 '+data.order_inactive_date+'</li>';
                baseStr += '<li>URL链接： <input type="text" name="real_name" id="url" style="width: 422px" readonly="readonly" value="'+data.url+'">' +
                    '<a href="#" class="copy-btn" data-clipboard-target="#url"> 复制</a>' +
                    '</li>';

			    $('.base-info').html(baseStr);
                var marketStr = '';
                if(data.is_follow == 0){
                    marketStr += '<li><i class="import-deco">*</i>强制关注： 是</li>';
                }
                else{
                    marketStr += '<li><i class="import-deco">*</i>强制关注： 否</li>';
                }
                marketStr += '<li><i class="import-deco">*</i>返利渠道： 红包领取</li>';
                marketStr += '<li><i class="import-deco">*</i>返利类型： 固定金额</li>';
                marketStr += '<li><i class="import-deco">*</i>单笔金额： ¥ '+data.per_amount_str+'</li>';
                marketStr += '<li>活动订单： 1000/50</li>';
                marketStr += '<li>活动金额： 1000/50</li>';
                if(data.is_audit == 0){
                    marketStr += '<li><i class="import-deco">*</i>人工审核： 否</li>'
				}
				else{
                    marketStr += '<li><i class="import-deco">*</i>人工审核： 是</li>'
                }
			    $('.market-type').html(marketStr);
                var shopName = strToJson(data.shop_name);
                $.each(shopName,function (key,value) {
                    $('.join-shop').append('<li>'+key+'： '+value+'</li>');
                })
			}
        }
	})
    ajaxFuc();
    function ajaxFuc() {
        var dataObject = {};
        dataObject.act_type = para.activity_type;
        dataObject.current_page = $('#current_page').val();
        dataObject.page_size = $('#page_size').val();
        dataObject.page_type = 1;
        dataObject.act_name = para.act_name;
        //待改 公众号
        dataObject.wechat_id = 1;
        $.ajax({
            url:'${ctx}/trade/order/orderListDate',
            type:'post',
            data:dataObject,
            success:function (msg) {
                var msg = strToJson(msg);
                if(msg.code == 10000){
                    var data = msg.data;
                    $('.lists-show').html('');
                    var listStr = '';
                    data.list.forEach(function (el,index) {
                        listStr += '<p class="clearfix"><span class="status-name">';
                        if(el.act_status == 0){
                            listStr += '未开始';
                        }
                        else if(el.act_status == 1){
                            listStr += '进行中';
                        }
                        else if(el.act_status == 2){
                            listStr += '暂停';
                        }
                        else if(el.act_status == 3){
                            listStr += '已结束';
                        }
                        listStr += '_'+el.act_name+'</span><span class="start-time">下单时间:'+el.pay_data+'</span>';
                        if(el.draw_date){
                            listStr += '<span class="receive-time">领取时间:'+el.draw_date+'</span>';
                        }
                        listStr += ' <i class="list-right">' +
                            '<a href="${ctx}/trade/order/order_detail?id='+el.id+'&activity_type='+para.activity_type+'" class="order-detail">订单详情</a>' +
                            '<input type="hidden" value="'+el.id+'">' +
                            '</i>';
                        listStr += ' <ul class="order-list clearfix">';
                        listStr += '<li class="mycol-10">'+el.status_str+'</li>';
                        listStr += '<li class="mycol-10">'+el.attention_str+'</li>';
                        listStr += '<li class="mycol-10">'+el.platform_name+'</li>';
                        listStr += '<li class="mycol-15">'+el.shop_name+'</li>';
                        listStr += '<li class="mycol-15">'+el.order_sn+'</li>';
                        listStr += '<li class="mycol-10" title="'+el.member_name+'">'+el.member_name+'</li>';
                        listStr += '<li class="mycol-10">'+el.mobile+'</li>';
                        listStr += '<li class="mycol-10">¥ '+el.act_money+'</li>';
                        listStr += '<li class="mycol-10"><a class="img_ifram" href="#" data-src="'+el.picture_url+'">点击查看</a></li>';
                        listStr += '</ul>';
                    })
                    $('.lists-show').html(listStr);
                    $('#current_page').val(nextPageSec);
                    $('#pageCount').val(data.count);
                    pageList(10,nextPageSec);
                }
            }
        })
    }

    var clipboard = new ClipboardJS('.copy-btn');
    clipboard.on('success', function(e) {
        layer.msg('已复制到粘贴板上');
    });
    clipboard.on('error', function(e) {
        console.log(e);
    });

    pageList(10,1)
    $(".wrap").on("click",".img_ifram", function(){
        var src = $(this).attr("data-src");
        $('#preview-layer').remove();
        $('body').append('<div id="preview-layer" style="display:none;"><img src="' + src + '" style="width:100%;"></div>');
        layer.open({
            type: 1,
            closeBtn: 1,
            title: "信息",
            area: '640px',
            skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: $('#preview-layer')
        });
    })
    //对url的处理
    function GetRequest() {
        var url = location.search; //获取url中"?"符后的字串
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for(var i = 0; i < strs.length; i ++) {
                theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
            }
        }
        return theRequest;
    }
})

</script>
</body>
</html>