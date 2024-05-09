import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/login',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限', title: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页', title: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息', title: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息', title: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码', title: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息', title: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'user', name: 'User', meta: { name: '用户信息', title: '用户信息' }, component: () => import('../views/manager/User') },
      { path: 'category', name: 'Category', meta: { name: '分类管理', title: '分类管理' }, component: () => import('../views/manager/Category') },
      { path: 'goods', name: 'Goods', meta: { name: '商品管理', title: '商品管理' }, component: () => import('../views/manager/Goods') },
      { path: 'circles', name: 'Circles', meta: { name: '圈子管理', title: '圈子管理' }, component: () => import('../views/manager/Circles') },
      { path: 'posts', name: 'Posts', meta: { name: '社区帖子', title: '社区帖子' }, component: () => import('../views/manager/Posts') },
      { path: 'help', name: 'Help', meta: { name: '求购管理', title: '求购管理' }, component: () => import('../views/manager/Help') },
      { path: 'orders', name: 'Orders', meta: { name: '订单管理', title: '订单管理' }, component: () => import('../views/manager/Orders') },
      { path: 'address', name: 'Address', meta: { name: '收货地址', title: '收货地址' }, component: () => import('../views/manager/Address') },
      { path: 'comment', name: 'Comment', meta: { name: '评论信息', title: '评论信息' }, component: () => import('../views/manager/Comment') },
      { path: 'feedback', name: 'Feedback', meta: { name: '用户反馈', title: '用户反馈' }, component: () => import('../views/manager/Feedback') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页', title: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'notice', name: 'Notice', meta: { name: '系统公告', title: '系统公告' }, component: () => import('../views/front/Notice') },
      { path: 'person', name: 'Person', meta: { name: '个人中心', title: '个人中心' }, component: () => import('../views/front/Person') },
      { path: 'goodsDetail', name: 'GoodsDetail', meta: { name: '商品信息', title: '商品信息' }, component: () => import('../views/front/GoodsDetail') },
      { path: 'collect', name: 'Collect', meta: { name: '我的收藏', title: '我的收藏' }, component: () => import('../views/front/Collect') },
      { path: 'addGoods', name: 'AddGoods', meta: { name: '发布商品', title: '发布商品' }, component: () => import('../views/front/AddGoods') },
      { path: 'goods', name: 'Goods', meta: { name: '我的商品', title: '我的商品' }, component: () => import('../views/front/Goods') },
      { path: 'address', name: 'Address', meta: { name: '我的地址', title: '我的地址' }, component: () => import('../views/front/Address') },
      { path: 'orders', name: 'Orders', meta: { name: '我的订单', title: '我的订单' }, component: () => import('../views/front/Orders') },
      { path: 'myPosts', name: 'MyPosts', meta: { name: '我的帖子', title: '我的帖子' }, component: () => import('../views/front/MyPosts') },
      { path: 'posts', name: 'Posts', meta: { name: '社区广场', title: '社区广场' }, component: () => import('../views/front/Posts') },
      { path: 'postsDetail', name: 'PostsDetail', meta: { name: '帖子详情', title: '帖子详情' }, component: () => import('../views/front/PostsDetail') },
      { path: 'help', name: 'Help', meta: { name: '求购专区', title: '求购专区' }, component: () => import('../views/front/Help') },
      { path: 'myHelp', name: 'MyHelp', meta: { name: '我的求购', title: '我的求购' }, component: () => import('../views/front/MyHelp') },
      { path: 'feedback', name: 'Feedback', meta: { name: '留言反馈', title: '求购留言反馈专区' }, component: () => import('../views/front/Feedback') },
      { path: 'myFeedback', name: 'MyFeedback', meta: { name: '我的反馈', title: '我的反馈' }, component: () => import('../views/front/MyFeedback') },
      { path: 'chat', name: 'Chat', meta: { name: '聊天信息', title: '聊天信息' }, component: () => import('../views/front/Chat') },
      { path: 'search', name: 'Search', meta: { name: '搜索商品', title: '搜索商品' }, component: () => import('../views/front/Search') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录', title: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册', title: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问', title: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to ,from, next) => {
  if(to.meta.title){
    document.title = to.meta.title
    next()
  }else{
    next()
  }
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
