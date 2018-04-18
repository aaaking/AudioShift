var loadingView= window.loadingView;
if(loadingView)
{
    loadingView.loadingAutoClose=true;
	loadingView.showTextInfo=true;
    loadingView.bgColor("#ffffff");
    loadingView.setFontColor("#000000");
    loadingView.setTips(["新世界的大门即将打开", "敌军还有30秒抵达战场", "妈妈说，心急吃不了热豆腐"]);
}
window.onLayaInitError=function(e)
{
	console.log("onLayaInitError error=" + e);
	alert("加载游戏失败，可能由于您的网络不稳定，请退出重进");
}
//网络监听
/**
NET_NO = 0;
NET_WIFI = 1;
NET_2G = 2;
NET_3G = 3;
NET_4G = 4;
NET_UNKNOWN=5
*/
//if( conch )
//{
//    conch.setNetworkEvtFunction(function(type)
//    {
//        alert(type)
//    });
//}