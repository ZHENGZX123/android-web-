(function(){
window.wx= {};
    wx.callback = {};
    wx.event = {};
       var arr = ['checkJsApi',
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo',
        'onMenuShareQZone',
        'hideMenuItems',
        'showMenuItems',
        'hideAllNonBaseMenuItem',
        'showAllNonBaseMenuItem',
        'translateVoice',
        'startRecord',
        'stopRecord',
        'pauseVoice',
        'stopVoice',
        'uploadVoice',
        'downloadVoice',
        'chooseImage',
        'previewImage',
        'uploadImage',
        'downloadImage',
        'getNetworkType',
        'openLocation',
        'getLocation',
        'hideOptionMenu',
        'showOptionMenu',
        'closeWindow',
        'playVoice',
  'scanQRCode',
  'chooseWXPay',
  'openProductSpecificView',
  'addCard',
  'chooseCard',
  'openCard'];
  var processMethod=function(){  };
  var processEvent=function(){
      var eventArr = ['onVoiceRecordEnd','onVoicePlayEnd'];
      wx.callback.event = {};
      for(var i in eventArr){
          var name = eventArr[i];
          wx[name] = (function(name){
              return function(option){
                wx.callback.event[name] = option;
                }
          })(name);
       }
    };
          processEvent();
  for(var i in arr){
      var methodName = arr[i];
      var _methodName = '_'+methodName;
      wx.callback[methodName] = {success:function(){},fail:function(){}};
      wx[methodName] = (function(methodName, _methodName){
          return function(arg){
              if(arg){
                  wx.callback[methodName].success = arg.success;
                  wx.callback[methodName].fail = arg.fail;
                  wx.callback[methodName].cancel = arg.cancel;
                  wx.callback[methodName].complete = arg.complete;
                  delete arg.success;
                  delete arg.fail;
                  delete arg.cancel;
                  delete arg.complete;
                  wx[_methodName](methodName, JSON.stringify(arg));
              } else {
                  wx[_methodName]();
              }
            }
      })(methodName, _methodName)
 };
})()