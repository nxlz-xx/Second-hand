<template>
  <div style="margin: 10px auto; padding: 10px 0; width: 50%;">
    <div style="display: flex; margin-bottom: 40px; gap: 20px;">
        <img :src="goods.img" alt="" style="width: 50%; height: 400px; display: block; border-radius: 5px">
        <div style="flex: 1; width: 0;">
            <el-tooltip :content="goods.name" placement="top-start">
                <div style="font-weight: bold; font-size: 18px; margin: 20px auto;" class="line1">{{ goods.name }}</div>
            </el-tooltip>
            <div style="display: flex; font-size: 16px;">
                <div><span style="color: #666;">浏览</span> {{ goods.readCount }}</div>
                <div style="margin-left: 30px;"><span style="color: #666;">点赞</span> {{ goods.likesCount }}</div>
                <div style="margin-left: 30px;"><span style="color: #666;">收藏</span> {{ goods.collectCount }}</div>
            </div>
            <div style="color: red; font-size: 24px; font-weight: bold; margin: 40px 0;">￥ {{ goods.price }}</div>
            <div style="margin-bottom: 20px; font-size: 15px;"><span style="color: #666">发货地：</span> {{ goods.address }}</div>
            <div style="margin-bottom: 20px; font-size: 15px;">
                <span style="color: #666">卖家：</span> {{ goods.userName }}
                <el-tooltip effect="light" content="聊天" placement="right" :hide-after="2000">
                    <i @click="chat(goods.userId)" class="el-icon-chat-dot-round" style="font-size: 18px; margin-left: 3px; cursor: pointer"></i>
                </el-tooltip>
            </div>
            <div style="margin-bottom: 40px; font-size: 15px;"><span style="color: #666">发布日期：</span> {{ goods.date }}</div>
            <div>
                <el-button v-if="!goods.userLikes" size="medium" type="warning" @click="likes(1)">点赞</el-button>
                <el-button v-if="goods.userLikes" size="medium" type="warning" @click="likes(0)">已点赞</el-button>
                <el-button v-if="!goods.userCollect" size="medium" type="danger" @click="collect(1)">收藏</el-button>
                <el-button v-if="goods.userCollect" size="medium" type="danger" @click="collect(0)">已收藏</el-button>
                <el-button size="medium" type="success" @click="buy">立即购买</el-button>
            </div>
        </div>
    </div>

    <div>
        <div style="display: flex; border-bottom: 1px solid orange; margin-bottom: 10px;">
            <div style="padding: 10px 20px; cursor: pointer;" 
            :class="{'active' : current == '商品详情'}" @click="changeItem('商品详情')">商品详情
            </div>
            <div style="padding: 10px 20px; cursor: pointer;" 
            :class="{'active' : current == '商品评论'}" @click="changeItem('商品评论')">商品评论
            </div>
        </div>
        <div v-if="current == '商品详情'">
            <div v-html="goods.content"></div>
        </div>
        <div v-else>
            <Comment :fid="id" module="goods"></Comment>
        </div>
    </div>

    <el-dialog title="请选择收货地址" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
        <div style="padding: 0 20px;">
            <el-radio-group v-model="form.addressId">
                <el-radio v-for="item in addressList" :key="item.id" :label="item.id" style="margin-bottom: 15px;">
                    {{item.name + '-' + item.address + '-' + item.phone}}
                </el-radio>
            </el-radio-group>
            <a v-if="addressList.length == 0" href="/front/address" target="_blank">还没有收货地址？去创建</a>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="addOrder">提交订单</el-button>
        </div>
    </el-dialog>

  </div>
</template>

<script>
import Comment from '@/components/Comment.vue'
export default {
  components: { Comment },

  data() {
    return {
        user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
        id: this.$route.query.id,
        goods: {},
        current: '商品详情',
        dialogVisible: false,
        addressList: [],
        form: {}
    }
  },
  created() {  
    //先更新浏览量
    this.$request.put('/goods/updateReadCount/' + this.id).then(res => {
        this.loadGoods()
    }),
    this.loadAddress()
  },
  methods: {
    chat(userId) {
        this.$request.post('/chatGroup/add', { chatUserId: userId, userId: this.user.id }).then(res => {
            this.$router.push('/front/chat')
        })
    },
    addOrder(){
        if(!this.form.addressId){
            this.$message.warning("请选择收货地址")
            return
        }
        this.form.goodsId = this.id
        this.$request.post('/orders/add', this.form).then(res => {
            if(res.code == '200'){
                this.$message.success("下单成功")
                this.dialogVisible = false
                this.$router.push('/front/orders')
            }else{
                this.$message.error(res.msg)
            }
        })
    },
    buy(){
        this.dialogVisible = true
    },
    loadAddress(){
        this.$request.get('/address/selectAll').then(res => {
            if(res.code == '200'){
                this.addressList = res.data
            }else{
                this.$message.error(res.msg)
            }
        })
    },
    loadGoods(){
      this.$request.get('/goods/selectById/' + this.id).then(res => {
        if(res.code == '200'){
            this.goods = res.data
        }else{
            this.$message.error(res.msg)
        }
      })
    },
    changeItem(current){
        this.current = current
    },
    likes(num){
        let data = {
            goodsId: this.id,
            module: 'goods'
        }
        this.$request.post('/likes/add', data).then(res => {
            if(res.code == '200'){
                if(num == 1){
                    this.$message.success("点赞成功")
                }else{
                    this.$message.success("已取消点赞")
                }
                this.loadGoods()
            }else{
                this.$message.error(res.msg)
            }
        })
    },
    collect(num){
         let data = {
            goodsId: this.id,
            module: 'goods'
        }
        this.$request.post('/collect/add', data).then(res => {
            if(res.code == '200'){
                if(num){
                    this.$message.success("收藏成功")
                }else{
                    this.$message.success("已取消收藏")
                }
                this.loadGoods()
            }else{
                this.$message.error(res.msg)
            }
        })
    }
  }
}
</script>

<style>
.active{
    background-color: orangered;
    color: #eee;
}
</style>