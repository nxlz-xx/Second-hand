<template>
  <div style="min-height: 100vh;">

    <div class="bg">
      <div style="width: 600px;">
        <div style="text-align: center; color: #eeeeee; font-size: 30px; margin-bottom: 20px;">好看、好用、好玩，都在这里</div>
        <div style="display: flex;">
          <el-input v-model="name" size="medium" prefix-icon="el-icon-search" placeholder="请输入商品名称"></el-input>
          <el-button type="primary" size="medium" style="margin-left: 5px; background-color: #409EFF" @click="navToSearch"><i class="el-icon-search"></i></el-button>
        </div>
      </div>
    </div>

    <div style="width: 70%; margin: 10px auto; background-color: #fff; padding: 20px; position: relative;">
      <el-button type="primary" size="medium" style="position: absolute; top: 20px; right: -150px;" @click="$router.push('/front/addGoods')">发布商品</el-button>
      <el-button type="warning" size="medium" style="position: absolute; top: 80px; right: -150px;" @click="$router.push('/front/myHelp')">发布求购</el-button>
      <div style="margin-bottom: 20px;">
        <el-select v-model="category" size="medium" style="width: 200px" @change="loadGoods(1)">
          <el-option label="全部" value="全部"></el-option>
          <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.name"></el-option>
        </el-select>
        <el-select v-model="sort" size="medium" style="width: 200px; margin-left: 20px;" @change="loadGoods(1)">
          <el-option label="最新" value="最新"></el-option>
          <el-option label="最热" value="最热"></el-option>
        </el-select>
      </div>
      <div>
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
      </div>
      <div class="pagination" style="margin-top: 15px;">
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

  data() {
    return {
      pageNum: 1,   // 当前的页码
      pageSize: 8,  // 每页显示的个数
      total: 0,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      category: '全部',
      sort: '最新',
      categoryList: [],
      goodsList: [],
      name: null
    }
  },
  mounted() {
    this.loadCategory()
    this.loadGoods(1)
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadCategory(){
      this.$request.get('/category/selectAll').then(res => {
        if(res.code == '200'){
          this.categoryList = res.data
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadGoods(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/selectFrontPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id,
          category: this.category == '全部' ? null : this.category,
          sort: this.sort,
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
    navToSearch(){
      this.$router.push({path: '/front/search', query: {name: this.name} })
    }
  }
}
</script>

<style scoped>
.bg{
  height: 400px;
  background-image: url("@/assets/imgs/home.jpg");
  background-size: 100%;
  background-position-y: center;
  display: flex;
  align-items: center;
  justify-content: center;
}
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
.goods-name:hover{
  color: #15c0eb !important;
}
</style>
