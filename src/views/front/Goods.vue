<template>
  <div style="width: 70%; margin: 10px auto" class="card">

    <div class="search" style="margin-bottom: 15px;">
        <el-input placeholder="请输入名称查询" style="width: 200px; margin-right: 10px" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>
    <div>
      <el-table :data="tableData" strip>
        <el-table-column prop="img" label="图片" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.img" style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" show-overflow-tooltip  align="center"></el-table-column>
        <el-table-column prop="price" label="价格"  align="center">
          <template v-slot="scope">
            <span style="color: red; font-size: 15px">￥ {{scope.row.price}} </span>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类"  align="center"></el-table-column>
        <el-table-column prop="content" label="详情" width="100"  align="center">
          <template v-slot="scope">
            <el-button type="success" plain @click="preview(scope.row.content)">显示详情</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="发货地址"  align="center"></el-table-column>
        <el-table-column prop="status" label="审核状态"  align="center">
          <template v-slot="scope">
            <el-tag type="info" v-if="scope.row.status === '待审核'">待审核</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '通过'">通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="saleStatus" label="上架状态"  align="center"></el-table-column>
        <el-table-column prop="readCount" label="浏览量"  align="center"></el-table-column>
        <el-table-column prop="date" label="上架日期"  align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin: 15px 0">
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

    <el-dialog title="内容" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="content"></div>
    </el-dialog>

  </div>
</template>
<script>
export default {
  name: "Goods",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 6,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    preview(content) {
      this.content = content
      this.fromVisible = true
    },
    changeStatus(row, status) {
      this.$confirm('您确定操作吗？', '确认操作', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
        this.form.status = status
        this.$request.put('/goods/update', this.form).then(res => {
          if (res.code === '200') {  // 表示成功保存
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(err => {})
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.$router.push('/front/addGoods?id=' + row.id)
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/goods/update' : '/goods/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/goods/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
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
      this.$request.get('/goods/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          userId: this.user.id
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
    reset() {
      this.name = null
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