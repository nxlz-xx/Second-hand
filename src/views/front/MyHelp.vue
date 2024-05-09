<template>
  <div style="width: 80%; margin: 10px auto" class="card">
    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="handleAdd">发布求购</el-button>
    </div>

    <div style="margin: 10px auto">
      <el-table :data="tableData" strip>
        <el-table-column prop="img" label="图片" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.img" style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" align="center"></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="time" label="发布时间" align="center"></el-table-column>
        <el-table-column prop="solved" label="是否解决" align="center">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.solved === '未解决'">未解决</el-tag>
            <el-tag type="success" v-if="scope.row.solved === '已解决'">已解决</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态" align="center">
          <template v-slot="scope">
            <el-tag type="info" v-if="scope.row.status === '待审核'">待审核</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '通过'">通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog title="求购信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="图片" prop="img">
          <el-upload
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="form.content" placeholder="内容"></el-input>
        </el-form-item>
        <el-form-item label="是否解决" prop="solved">
          <el-select style="width: 100%" v-model="form.solved">
            <el-option value="未解决"></el-option>
            <el-option value="已解决"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
export default {
  title: "MyHelp",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    changeStatus(row, status) {
      this.$confirm('您确定'+status+'吗？', '确认操作', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
        this.form.status = status
        this.$request.put('/help/update', this.form).then(res => {
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
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
        this.$request({
            url: this.form.id ? '/help/update' : '/help/add',
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
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/help/delete/' + id).then(res => {
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
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/help/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/help/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
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
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImgSuccess(response, file, fileList) {
      this.form.img = response.data
    },
  }
}
</script>

<style scoped>

</style>