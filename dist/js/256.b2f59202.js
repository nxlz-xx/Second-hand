"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[256],{2256:function(t,e,i){i.r(e),i.d(e,{default:function(){return n}});var a=function(){var t=this,e=t._self._c;return e("div",{staticStyle:{width:"60%",margin:"10px auto"}},[e("div",{staticStyle:{display:"flex","grid-gap":"20px"}},[e("div",{staticClass:"chat-card",staticStyle:{width:"200px","min-height":"600px"}},[t._l(t.chatGroupList,(function(i){return t.chatGroupList.length>0?e("div",{key:i.id,on:{click:function(e){return t.loadChatList(i.chatUserId)}}},[i.userId===t.user.id?e("div",{staticClass:"chat-group",class:{"chat-group-active":i.chatUserId===t.activeChatUserId},staticStyle:{position:"relative"}},[e("img",{staticStyle:{width:"50px",height:"50px","border-radius":"50%"},attrs:{src:i.chatUserAvatar}}),i.chatNum>0&&!t.activeChatUserId?e("el-badge",{attrs:{value:i.chatNum,max:99}},[e("span",{staticStyle:{color:"#333"}},[t._v(t._s(i.chatUserName))])]):e("span",{staticStyle:{color:"#333"}},[t._v(t._s(i.chatUserName))]),e("i",{staticClass:"el-icon-close",staticStyle:{position:"absolute",top:"10px",right:"10px"},on:{click:function(e){return e.stopPropagation(),t.closeGroup(i.id)}}})],1):t._e()]):t._e()})),0===t.chatGroupList.length?e("div",{staticStyle:{color:"#666",padding:"20px 0","text-align":"center"}},[t._v("暂无聊天对象..")]):t._e()],2),e("div",{staticClass:"chat-card",staticStyle:{flex:"1",height:"600px"}},[e("div",{ref:"chatContainer",staticClass:"chat-text",staticStyle:{height:"450px","border-bottom":"1px solid #ddd","overflow-y":"auto"}},t._l(t.chatList,(function(i){return e("div",{key:i.id,staticStyle:{"margin-bottom":"20px"}},[i.chatUserId===t.user.id?e("div",[e("div",{staticStyle:{color:"#666","font-size":"12px",padding:"5px 0",margin:"5px 0","text-align":"center"}},[t._v(t._s(i.time))]),e("div",{staticStyle:{display:"flex","grid-gap":"10px"}},[e("img",{staticStyle:{width:"50px",height:"50px","border-radius":"5px"},attrs:{src:i.userAvatar,alt:""}}),e("div",{staticStyle:{"max-width":"50%",width:"fit-content"}},[e("div",{staticStyle:{color:"#666","margin-bottom":"5px"}},[e("span",[t._v(t._s(i.userName))])]),e("div",{staticStyle:{"background-color":"#d1e7ff",padding:"10px","border-radius":"5px"},domProps:{textContent:t._s(i.text)}})])])]):t._e(),i.userId===t.user.id?e("div",[e("div",{staticStyle:{color:"#666","font-size":"12px",padding:"5px 0",margin:"5px 0","text-align":"center"}},[t._v(t._s(i.time))]),e("div",{staticStyle:{display:"flex","justify-content":"right","grid-gap":"10px"}},[e("div",{staticStyle:{"max-width":"50%",width:"fit-content"}},[e("div",{staticStyle:{color:"#666","margin-bottom":"5px","text-align":"right"}},[t._v(t._s(i.userName))]),e("div",{staticStyle:{"background-color":"#a3eecb",padding:"10px","border-radius":"5px"},domProps:{textContent:t._s(i.text)}})]),e("img",{staticStyle:{width:"50px",height:"50px","border-radius":"5px"},attrs:{src:i.userAvatar,alt:""}})])]):t._e()])})),0),e("div",{staticStyle:{height:"150px"}},[e("textarea",{directives:[{name:"model",rawName:"v-model",value:t.form.text,expression:"form.text"}],staticStyle:{height:"100px",width:"100%",padding:"20px",border:"none","border-bottom":"1px solid #ddd",outline:"none"},domProps:{value:t.form.text},on:{input:function(e){e.target.composing||t.$set(t.form,"text",e.target.value)}}}),e("div",{staticStyle:{"text-align":"right","padding-right":"10px"}},[e("el-button",{attrs:{type:"primary",size:"mini"},on:{click:t.send}},[t._v("发送")])],1)])])])])},s=[];let o;var r={name:"Chat",data(){return{user:JSON.parse(localStorage.getItem("xm-user")||"{}"),chatGroupList:[],activeChatUserId:0,form:{},chatList:[],audio:null,scrollToBottom:!1}},created(){this.init(),this.loadChatGroup(),this.loadAudio()},methods:{loadAudio(){const t=i(8900);this.audio=new Audio(t),this.audio.volume=1},closeGroup(t){this.$request.delete("/chatGroup/delete/"+t).then((t=>{this.loadChatGroup(),this.chatList=[]}))},send(){this.activeChatUserId?this.form.text?(this.form.chatUserId=this.activeChatUserId,this.form.userId=this.user.id,this.$request.post("/chatInfo/add",this.form).then((t=>{this.form={},this.loadChatList(this.activeChatUserId),o.send(this.user.id)}))):this.$message.warning("请输入消息内容"):this.$message.warning("请选择聊天对象")},loadChatList(t){this.scrollToBottom=!0,this.form={},this.activeChatUserId=t,this.$request.put("/chatInfo/updateRead/"+t).then((t=>{this.loadChatGroup()})),this.$request.get("/chatInfo/selectUserChat/"+t).then((t=>{"200"==t.code?(this.chatList=t.data||[],this.$nextTick((()=>{const t=this.$refs.chatContainer;t&&this.scrollToBottom&&(t.scrollTop=t.scrollHeight,this.scrollToBottom=!1)}))):this.$message.error(t.msg)}))},loadChatGroup(){this.$request.get("/chatGroup/selectUserGroup").then((t=>{this.chatGroupList=t.data||[]}))},init(){let t=this;if("undefined"==typeof WebSocket)console.log("您的浏览器不支持WebSocket");else{console.log("您的浏览器支持WebSocket");let e="ws://8.130.100.221:9020/chatServer/"+this.user.id;null!=o&&(o.close(),o=null),o=new WebSocket(e),o.onopen=function(){console.log("websocket已打开")},o.onmessage=function(e){let i=JSON.parse(e.data);i==t.user.id?(t.loadChatGroup(),t.loadChatList(t.activeChatUserId)):(t.loadChatGroup(),t.loadChatList(t.activeChatUserId),t.audio.currentTime=0,t.audio.play())},o.onclose=function(){console.log("websocket已关闭")},o.onerror=function(){console.log("websocket发生了错误")}}}}},c=r,d=i(3736),l=(0,d.Z)(c,a,s,!1,null,"39c2b586",null),n=l.exports},8900:function(t,e,i){t.exports=i.p+"media/chat.0a129a1d.mp3"}}]);
//# sourceMappingURL=256.b2f59202.js.map