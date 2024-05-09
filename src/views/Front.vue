<template>
  <div style="background-color: #f8f8f8;">
    <!--头部-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title" @click="$router.push('/front/home')">二手交易网</div>
      </div>
      <div class="front-header-center">
        <div class="menu" v-for="item in menus" :key="item.path" @click="$router.push(item.path)"
             :class="{'menu-active': $route.path === item.path}">{{ item.text }}
        </div>
      </div>
      <div>
          <span @click="$router.push('/front/chat')" style="font-size: 16px; color: white; cursor: pointer"><i class="el-icon-chat-dot-round"></i> 聊天消息</span>
        </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="" style="border-radius: 50%;">
              <div style="margin-left: 10px; color: #eee;">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/person')">个人中心</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/goods')">我的商品</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/myPosts')">我的帖子</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/myHelp')">我的求购</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/orders')">我的订单</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/collect')">我的收藏</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/address')">我的地址</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="navTo('/front/MyFeedback')">我的反馈</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>
    <Footer></Footer>

  </div>
</template>

<script>
import Footer from '@/components/Footer.vue'
export default {
  components: { Footer },
  name: "FrontLayout",
  data () {
    return {
      top: '',
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      menus: [
        { text: '系统公告', path: '/front/notice' },
        { text: '热卖专区', path: '/front/home' },
        { text: '社区广场', path: '/front/posts' },
        { text: '求购专区', path: '/front/help' },
        { text: '留言反馈', path: '/front/feedback' },
      ]
    }
  },

  mounted() {

  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
    navTo(url){
      this.$router.push(url)
    }
  }

}
</script>

<style scoped>
  @import "@/assets/css/front.css";
.menu{
  color: #eee;
  font-size: 16px;
  padding: 0 20px;
  cursor: pointer;
}
.menu:hover{
  color: orange;
}
.menu-active{
  color: orange;
}
</style>