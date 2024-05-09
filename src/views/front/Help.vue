<template>
  <div style="width: 50%; margin: 10px auto">
    <div style="margin-bottom: 10px">
      <el-input v-model="title" placeholder="请输入标题" style="width: 200px; margin-right: 10px" clearable></el-input>
      <el-button type="primary" @click="load(1)">查询</el-button>
    </div>
    <div class="card" v-for="item in tableData" :key="item.id" style="margin-bottom: 5px; display: flex; grid-gap: 10px">
      <img :src="item.userAvatar" alt="" style="width: 50px; height: 50px; display: block; border-radius: 50%">
      <div style="flex: 1">
        <div style="margin-bottom: 5px">
          <strong style="margin-right: 10px">{{ item.title }}</strong>
          <el-tag type="danger" v-if="item.solved === '未解决'">未解决</el-tag>
          <el-tag type="success" v-if="item.solved === '已解决'">已解决</el-tag>
        </div>
        <div style="color: #888;"><span style="margin-right: 20px"><i class="el-icon-user"></i> {{ item.userName }} </span> <i class="el-icon-time"></i><span> {{ item.time }} </span></div>

        <div style="margin: 15px 0">
          <el-image :preview-src-list="[item.img]" :src="item.img" alt="" style="width: 200px"></el-image>
        </div>
        <div v-html="item.content" style="margin: 15px 0"></div>
        <div style="margin: 20px 0">
          <el-button type="primary" @click="handleCommentShow(item)">展开 / 折叠 求购评论</el-button>
        </div>
        <div v-if="item.showComment">
          <Comment :fid="item.id" module="help"></Comment>
        </div>
      </div>
    </div>

    <div style="margin: 15px auto" v-if="total > 0">
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
</template>

<script>
import Comment from "@/components/Comment";
export default {
  name: "Help",
  components: {Comment},
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      title: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleCommentShow(help) {
      this.$set(help, 'showComment', !help.showComment)
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/help/selectFrontPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title
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
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>