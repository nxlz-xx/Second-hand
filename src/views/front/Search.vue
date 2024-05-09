<template>
  <div style="width: 70%; margin: 10px auto">
    <div style="margin-bottom: 10px">
      <el-input v-model="name" size="medium" style="width: 200px; margin-right: 10px"></el-input>
      <el-button type="primary" @click="loadGoods(1)">搜 索</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>
    <div class="card">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in goodsList" :key="item.id">
            <div style="margin-bottom: 15px;">
              <img :src="item.img" alt="" class="goods-img" @click="$router.push('/front/goodsDetail?id=' + item.id)">
              <div class="goods-name line2">{{ item.name }}</div>
              <div style="display: flex; align-items: baseline">
                <strong style="color: red; font-size: 24px">￥{{ item.price }}</strong>
                <span style="margin-left: 20px; color: #666">{{ item.readCount }} 人浏览</span>
                <span style="margin-left: 20px; color: #666">{{item.likesCount}} 人点赞</span>
              </div>
            </div>
          </el-col>
        </el-row>

      <div style="margin: 15px auto">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Search",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      goodsList: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 8,  // 每页显示的个数
      total: 0,
      name: this.$route.query.name
    }
  },
  created() {
    this.loadGoods(1)
  },
  methods: {
    loadGoods(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/search', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id,
          name: this.name
        }
      }).then(res => {
        if (res.code === '200') {
          this.goodsList = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.loadGoods(pageNum)
    },
    reset() {
      this.name = null
      this.loadGoods(1)
    }
  }
}
</script>

<style scoped>
.goods-img{
  width: 100%; 
  height: 300px; 
  display: block; 
  margin-bottom: 10px; 
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.2s;
}
.goods-img:hover{
  scale: 1.03;
}
.goods-name{
  font-size: 14px; 
  height: 39px;
  color: #555; 
  margin-bottom: 10px;
}
</style>