<template>
  <div style="margin: 15px auto; width: 50%">

    <div style="display: flex; flex-wrap: wrap; margin-bottom: 10px;">
      <div class="card circle" @click="loadCircle('全部')" :class="{'circle-active' : circle === '全部' }">
        <img style="width: 50px; height: 50px; margin-right: 5px; display: block" src="@/assets/imgs/全部.png" alt="">
        <span>全部</span>
      </div>
      <div class="card circle" :class="{'circle-active' : circle === item.name }" v-for="item in circles" :key="item.id" @click="loadCircle(item.name)">
        <img style="width: 50px; height: 50px; margin-right: 5px; display: block" :src="item.img" alt="">
        <span>{{ item.name }}</span>
      </div>
    </div>

    <div>
      <div @click="$router.push('/front/postsDetail?id=' + item.id)" v-for="item in tableData" :key="item.id"
           style="display: flex; margin-bottom: 5px; cursor: pointer" class="card">
        <div style="flex: 1">
          <div style="font-size: 20px; margin-bottom: 10px">{{ item.title }}</div>
          <div style="color: #666; margin-bottom: 10px">{{ item.descr }}</div>
          <div style="color: #666">
            <span><i class="el-icon-user"></i> {{ item.userName }}</span>
            <span style="margin-left: 20px"><i class="el-icon-time"></i> {{ item.time }}</span>
            <span style="margin-left: 20px"><i class="el-icon-reading"></i> {{ item.readCount }}</span>
          </div>
        </div>
        <div>
          <img :src="item.img" alt="" style="width: 80px; height: 80px; display: block">
        </div>
      </div>

      <div style="margin: 15px 0" v-if="total > 0">
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
  name: "Posts",
  data() {
    return {
      circles: [],
      posts: [],
      circle: '全部',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 4
    }
  },
  created() {
    this.loadCircles()
    this.load(1)
  },
  methods: {
    loadCircle(circle) {  // 分页查询
      this.circle = circle
      this.load(1)
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/posts/selectFrontPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          circle: this.circle === '全部' ? null : this.circle,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadCircles() {
      this.$request.get('/circles/selectAll').then(res => {
        this.circles = res.data || []
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>
.circle {
  margin-right: 10px;
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 10px;
}
.circle:hover {
  background-color: #ffedd8;
}
.circle-active {
  background-color: #ffedd8;
}
</style>