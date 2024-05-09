<template>
  <div style="width: 60%; margin: 10px auto">

    <div style="display: flex; grid-gap: 20px">

      <!-- 聊天组开始 -->
      <div class="chat-card" style="width: 200px; min-height: 600px">
        <div v-if="chatGroupList.length > 0" @click="loadChatList(item.chatUserId)" v-for="item in chatGroupList" :key="item.id">
          <div style="position: relative" v-if="item.userId === user.id" class="chat-group" :class="{'chat-group-active' : item.chatUserId === activeChatUserId}">
            <img :src="item.chatUserAvatar" style="width: 50px; height: 50px; border-radius: 50%" />
            <el-badge :value="item.chatNum" :max="99" v-if="item.chatNum > 0 && !activeChatUserId">
              <span style="color: #333">{{ item.chatUserName }}</span>
            </el-badge>
            <span v-else style="color: #333">{{ item.chatUserName }}</span>

            <i class="el-icon-close" style="position: absolute; top: 10px; right: 10px" @click.stop="closeGroup(item.id)"></i>
          </div>
        </div>
        <div v-if="chatGroupList.length === 0" style="color: #666; padding: 20px 0; text-align: center">暂无聊天对象..</div>
      </div>
      <!-- 聊天组结束 -->

      <!-- 聊天消息开始 -->
      <div class="chat-card" style="flex: 1; height: 600px">
        <div class="chat-text" ref="chatContainer" style="height: 450px; border-bottom: 1px solid #ddd; overflow-y: auto;">
          <div v-for="item in chatList" :key="item.id" style="margin-bottom: 20px">

            <!-- 左边的聊天消息开始 -->
            <!-- item.chatUserId === user.id 表示对方在跟当前登录的人聊天 user表示对方  显示对方的名称和头像        -->
            <div v-if="item.chatUserId === user.id">
                <div style="color: #666; font-size: 12px; padding: 5px 0; margin: 5px 0; text-align: center;">{{ item.time }}</div>
                <div style="display: flex; grid-gap: 10px">
                    <img style="width: 50px; height: 50px; border-radius: 5px" :src="item.userAvatar" alt="">
                    <div style="max-width: 50%; width: fit-content">
                    <div style="color: #666; margin-bottom: 5px">
                        <span>{{ item.userName }}</span>
                    </div>
                    <div style="background-color: #d1e7ff; padding: 10px; border-radius: 5px;" v-text="item.text"></div>
                    </div>
                </div>
            </div>
            <!-- 左边的聊天消息结束 -->

            <!-- 右边的聊天消息开始 -->
            <div v-if="item.userId === user.id">
                <div style="color: #666; font-size: 12px; padding: 5px 0; margin: 5px 0; text-align: center;">{{ item.time }}</div>
                <div style="display: flex; justify-content: right; grid-gap: 10px">
                  <div style="max-width: 50%; width: fit-content">
                    <div style="color: #666; margin-bottom: 5px; text-align: right">{{ item.userName }}</div>
                    <div style="background-color: #a3eecb; padding: 10px; border-radius: 5px;" v-text="item.text"></div>
                  </div>
                  <img style="width: 50px; height: 50px; border-radius: 5px" :src="item.userAvatar" alt="">
                </div>
            </div>
            <!-- 右边的聊天消息结束 -->
            
          </div>
        </div>
        <!-- 聊天消息结束 -->

        <!-- 聊天输入框开始 -->
        <div style="height: 150px">
          <textarea v-model="form.text" style="height: 100px; width: 100%; padding: 20px; border: none;
             border-bottom: 1px solid #ddd; outline: none"></textarea>
          <div style="text-align: right; padding-right: 10px">
            <el-button type="primary" size="mini" @click="send">发送</el-button>
          </div>
        </div>
        <!-- 聊天输入框结束 -->

      </div>

    </div>
  </div>
</template>

<script>
let socket

export default {
  name: "Chat",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      chatGroupList: [],
      activeChatUserId: 0,
      form: {},
      chatList: [],
      audio: null,
      scrollToBottom: false, 
    }
  },
  created() {
    this.init()
    this.loadChatGroup()
    this.loadAudio() // 加载音频
  },
  methods: {
    loadAudio(){
        const audioUrl = require('@/assets/audio/chat.mp3');  
        this.audio = new Audio(audioUrl);  //替换为你的音频文件路径
        this.audio.volume = 1 // 设置音量（0.0到1.0之间）
    },
    closeGroup(id) {
      this.$request.delete('/chatGroup/delete/' + id).then(res => {
        this.loadChatGroup()
        this.chatList = []  // 清空聊天消息
      })
    },
    send() {
      if (!this.activeChatUserId) {
        this.$message.warning('请选择聊天对象')
        return
      }
      if (!this.form.text) {
        this.$message.warning('请输入消息内容')
        return
      }
      this.form.chatUserId = this.activeChatUserId
      this.form.userId = this.user.id
      this.$request.post('/chatInfo/add', this.form).then(res => {
        this.form = {}
        this.loadChatList(this.activeChatUserId)

        // 发送socket消息
        socket.send(this.user.id) //传递自己id以便接收消息语音提醒
      })
    },
    loadChatList(chatUserId) {
      this.scrollToBottom = true
      this.form = {}
      this.activeChatUserId = chatUserId

      // 更新消息读取状态
      this.$request.put('/chatInfo/updateRead/' + chatUserId).then(res => {
        this.loadChatGroup()
      })
      this.$request.get('/chatInfo/selectUserChat/' + chatUserId).then(res => {
        if(res.code == '200'){
            this.chatList = res.data || []
            // 使用 Vue 的 $nextTick 确保 DOM 更新完成后再滚动  
            this.$nextTick(() => {
              // 使用 Vue 的 ref 访问元素 
              const chatContainer = this.$refs.chatContainer;
              if (chatContainer) {  
                if(this.scrollToBottom){ 
                  // 直接设置滚动容器的 scrollTop 为其 scrollHeight 来滚动到底部  
                  chatContainer.scrollTop = chatContainer.scrollHeight;  
                  this.scrollToBottom = false 
                }
              } 
            })
        }else{
            this.$message.error(res.msg)
        }
      })
    },
    loadChatGroup() {
      this.$request.get('/chatGroup/selectUserGroup').then(res => {
        this.chatGroupList = res.data || []
      })
    },
    init() {
      let _this = this
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket")
      } else {
        console.log("您的浏览器支持WebSocket")
        let socketUrl = "ws://localhost:9020/chatServer/" + this.user.id
        if (socket != null) {
          socket.close()
          socket = null
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl)
        //打开事件
        socket.onopen = function () {
          console.log("websocket已打开")
        }
        //  浏览器端收消息，获得从服务端数据库里的最新的聊天消息
        socket.onmessage = function (msg) {
          let userId = JSON.parse(msg.data)
          if (userId == _this.user.id) {
            _this.loadChatGroup()
            _this.loadChatList(_this.activeChatUserId)
          }else{
            _this.loadChatGroup()
            _this.loadChatList(_this.activeChatUserId)

            // 播放音频
            _this.audio.currentTime = 0 // 重置播放位置
            _this.audio.play()
          }
        }
        //关闭事件
        socket.onclose = function () {
          console.log("websocket已关闭")
        }
        //发生了错误事件
        socket.onerror = function () {
          console.log("websocket发生了错误")
        }
      }
    }
  }
}
</script>

<style scoped>
.chat-card {
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.1);
}
.chat-group {
  padding: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  grid-gap: 10px;
  border-bottom: 1px solid #ddd
}
.chat-group-active {
  background-color: #d1e7ff;
}
.chat-text {
  padding: 20px;
}
.chat-text::-webkit-scrollbar {
  width: 10px;
}
.chat-text::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: #d1e7ff;
}
::v-deep .el-badge__content.is-fixed {
  right: 0px;
}
</style>