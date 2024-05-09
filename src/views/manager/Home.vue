<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <div style="display: flex; margin: 10px 0; gap: 10px;">
     <div id="line" class="card" style="flex: 1; height: 500px;"></div>
     <div id="bar" class="card" style="flex: 1; height: 500px;"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      lineOption: {
        title: {
          text: '商品销售额统计',
          left: 'center',
          subtext: '趋势图',
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          left: 'left'
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [],
            type: 'line',
            smooth: true
          },
        ]
      },
      barOption: {
        title: {
          text: '商家销售额统计',
          subtext: '柱状图',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          left: 'left'
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [],
            type: 'bar',
            smooth: true,
            itemStyle: {
              normal: {
                color: function(params) { // 根据索引返回对应的颜色
                  let colorList = ['#ffaa2e','#32C5E9','#fa4c4c','#08b448','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
                  return colorList[params.dataIndex];
                }
              }
            },
          }
        ]
      },

    }
  },
  created() {
    this.loadLine()
    this.loadBar()
  },
  methods: {
    //折线图
    loadLine(){
      this.$request.get('/echarts/line').then(res => {
        if(res.code == '200'){
          let linetDom = document.getElementById('line');
          let lineChart = echarts.init(linetDom);
          this.lineOption.xAxis.data = res.data?.map(v => v.name) || []
          this.lineOption.series[0].data = res.data?.map(v => v.value) || []
          lineChart.setOption(this.lineOption)
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    loadBar(){
      this.$request.get('/echarts/bar').then(res => {
        if(res.code == '200'){
          let barDom = document.getElementById('bar');
          let barChart = echarts.init(barDom);
          this.barOption.xAxis.data = res.data?.map(v => v.name) || []
          this.barOption.series[0].data = res.data?.map(v => v.value) || []
          barChart.setOption(this.barOption)
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
