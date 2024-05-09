<template>
  <div style="width: 85%; margin: 10px auto" class="card">

    <div style="margin-bottom: 15px; display: flex">
      <div style="flex: 1">
        <el-radio-group v-model="url" @change="load(1)">
          <el-radio-button label="selectPage">我购买的订单</el-radio-button>
          <el-radio-button label="selectSalePage">我出售的订单</el-radio-button>
        </el-radio-group>
      </div>
      <div>
        <el-input v-model="goodsName" placeholder="请输入商品名称" style="width: 200px; margin-right: 5px" clearable></el-input>
        <el-input v-model="orderNo" placeholder="请输入订单编号" style="width: 200px; margin-right: 5px" clearable></el-input>
        <el-select v-model="status" style="width: 200px; margin-right: 5px" clearable>
          <el-option value="待支付"></el-option>
          <el-option value="待发货"></el-option>
          <el-option value="待收货"></el-option>
          <el-option value="已完成"></el-option>
          <el-option value="已取消"></el-option>
        </el-select>
        <el-button type="primary" plain @click="load(1)">搜索</el-button>
        <el-button type="warning" plain @click="reset">重置</el-button>
      </div>
    </div>

    <div style="margin: 10px 0">
      <el-table :data="tableData" strip >
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="goodsImg" label="商品图片" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.goodsImg" style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip align="center">
            <template v-slot="scope">
                <span class="goodsName" @click="$router.push('/front/goodsDetail?id=' + scope.row.goodsId)">{{scope.row.goodsName}}</span>
            </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单编号" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="total" label="总价" align="center">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.total }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="下单时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="userName" label="下单人名称" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="address" label="收货地址" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="phone" label="联系方式" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="saleName" label="卖家名称" align="center"></el-table-column>
        <el-table-column prop="payNo" label="支付单号" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="payTime" label="支付时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="status" label="订单状态" align="center">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '待支付'">待支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待发货'">待发货</el-tag>
            <el-tag type="warning" v-if="scope.row.status === '待收货'">待收货</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已完成'">已完成</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button v-if="scope.row.status === '待支付' && scope.row.userId === user.id" size="mini" type="primary" plain @click="pay(scope.row.orderNo)">支付</el-button>
            <el-button v-if="scope.row.status === '待支付'" size="mini" type="danger" plain @click="changeStatus(scope.row, '已取消')">取消</el-button>
            <el-button v-if="scope.row.status === '待发货' && scope.row.saleId === user.id" size="mini" type="info" plain @click="changeStatus(scope.row, '待收货')">发货</el-button>
            <el-button v-if="scope.row.status === '待收货' && scope.row.userId === user.id" size="mini" type="primary" plain @click="changeStatus(scope.row, '已完成')">收货</el-button>
            <el-button v-if="scope.row.status === '已完成' || scope.row.status === '已取消'" size="mini" type="danger" @click="del(scope.row.id)">删除</el-button>
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

  </div>
</template>
<script>
export default {
  name: "Orders",
  data() {
    return {
      url: 'selectPage',
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      goodsName: null,
      orderNo: null,
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    pay(orderNo) {
        window.open(this.$baseUrl + '/alipay/pay?orderNo=' + orderNo)
    },
    changeStatus(row, status) {
      this.$confirm('您确认操作吗？', '确认操作', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))
        this.form.status = status
        this.$request.put('/orders/update', this.form).then(res => {
          if (res.code === '200') {  // 表示成功保存
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(e => {})
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
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/orders/update' : '/orders/add',
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
        this.$request.delete('/orders/delete/' + id).then(res => {
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
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/orders/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/orders/' + this.url, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          goodsName: this.goodsName,
          status: this.status,
          orderNo: this.orderNo,
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
      this.goodsName = null
      this.orderNo = null
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>
.goodsName{
    cursor: pointer;
}
.goodsName:hover{
    color: #409EFF;
}
</style>