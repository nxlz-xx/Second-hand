<template>
  <div style="width: 70%; margin: 20px auto">

    <div class="search">
      <el-input placeholder="请输入商品名称" style="width: 200px" v-model="goodsName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="danger" plain @click="delBatch">批量取消收藏</el-button>
    </div>

    <div class="card">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="商品图片" width="100">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 50px; height: 50px; border-radius: 5px;" v-if="scope.row.goodsImg"
                        :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column label="商品链接" width="100" align="center">
          <template v-slot="scope">
            <el-link type="primary" @click="$router.push('/front/goodsDetail?id=' + scope.row.goodsId)">点击打开</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="收藏时间" align="center"></el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>取消收藏</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 15px">
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
  name: "Collect",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      goodsName: null,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    del(id) {   // 单个删除
      this.$confirm('您确定取消收藏吗？', '确认取消', {type: "warning"}).then(response => {
        this.$request.delete('/collect/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('取消收藏成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定取消收藏吗？', '确认取消', {type: "warning"}).then(response => {
        this.$request.delete('/collect/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('取消收藏成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/collect/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          goodsName: this.goodsName,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.goodsName = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>