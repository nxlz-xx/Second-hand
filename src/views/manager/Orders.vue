<template>
  <div>

    <div class="search">
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

    <div class="table">
      <el-table :data="tableData" strip >
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="goodsImg" label="商品图片" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.goodsImg" style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip align="center"></el-table-column>
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