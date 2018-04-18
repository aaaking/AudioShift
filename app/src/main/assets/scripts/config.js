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

//if( window.conch )//可以通过该函数把所有画面都设置成半透明
//{
//    window.conch.config.setTransparentMode();
//}

function openWebview() {
    conch && conch.showAssistantTouch(true);
            var ctx = document.createElement('canvas').getContext('2d');
            function render() {
                ctx.fillStyle = '#99d9ea';
                ctx.fillRect(0, 0, window.innerWidth, window.innerHeight);
                window.requestAnimationFrame(render);
            }
            window.requestAnimationFrame(render);
            document.addEventListener('touchstart', () => {
                if (conch) {
                    var l = 50;
                    var t = 50;
                    var w = window.innerWidth - l * 2;
                    var h = window.innerHeight - t * 2;
                    conch.setExternalLinkEx('http://www.layabox.com', l, t, w, h, true);
                    //conch.setExternalLink('http://www.baidu.com');
                }
            });
}