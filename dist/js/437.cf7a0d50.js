"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[437],{9437:function(e,t,s){s.r(t),s.d(t,{default:function(){return c}});var a=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"search"},[t("el-input",{staticStyle:{width:"200px","margin-right":"10px"},attrs:{placeholder:"请输入名称查询"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),t("el-select",{attrs:{placeholder:"请选择用户名"},model:{value:e.userId,callback:function(t){e.userId=t},expression:"userId"}},e._l(e.userForm,(function(e){return t("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1),t("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"info",plain:""},on:{click:function(t){return e.load(1)}}},[e._v("查询")]),t("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"warning",plain:""},on:{click:e.reset}},[e._v("重置")])],1),t("div",{staticClass:"operation"},[t("el-button",{attrs:{type:"danger",plain:""},on:{click:e.delBatch}},[e._v("批量删除")])],1),t("div",{staticClass:"table"},[t("el-table",{attrs:{data:e.tableData,strip:""},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),t("el-table-column",{attrs:{prop:"id",label:"序号",width:"70",align:"center",sortable:""}}),t("el-table-column",{attrs:{prop:"img",label:"图片"},scopedSlots:e._u([{key:"default",fn:function(s){return[s.row.img?t("el-image",{staticStyle:{width:"50px"},attrs:{src:s.row.img,"preview-src-list":[s.row.img]}}):e._e()]}}])}),t("el-table-column",{attrs:{prop:"name",label:"名称","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"price",label:"价格"}}),t("el-table-column",{attrs:{prop:"content",label:"详情",width:"100"},scopedSlots:e._u([{key:"default",fn:function(s){return[t("el-button",{attrs:{type:"success"},on:{click:function(t){return e.preview(s.row.content)}}},[e._v("显示详情")])]}}])}),t("el-table-column",{attrs:{prop:"address",label:"发货地址"}}),t("el-table-column",{attrs:{prop:"date",label:"上架日期"}}),t("el-table-column",{attrs:{prop:"status",label:"审核状态"},scopedSlots:e._u([{key:"default",fn:function(s){return["待审核"===s.row.status?t("el-tag",{attrs:{type:"info"}},[e._v("待审核")]):e._e(),"通过"===s.row.status?t("el-tag",{attrs:{type:"primary"}},[e._v("通过")]):e._e(),"拒绝"===s.row.status?t("el-tag",{attrs:{type:"danger"}},[e._v("拒绝")]):e._e()]}}])}),t("el-table-column",{attrs:{prop:"category",label:"分类"}}),t("el-table-column",{attrs:{prop:"userName",label:"所属用户"}}),t("el-table-column",{attrs:{prop:"saleStatus",label:"上架状态"}}),t("el-table-column",{attrs:{prop:"readCount",label:"浏览量"}}),t("el-table-column",{attrs:{label:"操作",align:"center",width:"240"},scopedSlots:e._u([{key:"default",fn:function(s){return[t("el-button",{attrs:{size:"mini",type:"primary",plain:""},on:{click:function(t){return e.changeStatus(s.row,"通过")}}},[e._v("通过")]),t("el-button",{attrs:{size:"mini",type:"danger",plain:""},on:{click:function(t){return e.changeStatus(s.row,"拒绝")}}},[e._v("拒绝")]),t("el-button",{attrs:{size:"mini",type:"danger",plain:""},on:{click:function(t){return e.del(s.row.id)}}},[e._v("删除")])]}}])})],1),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{background:"","current-page":e.pageNum,"page-sizes":[5,10,20],"page-size":e.pageSize,layout:"total, prev, pager, next",total:e.total},on:{"current-change":e.handleCurrentChange}})],1)],1),t("el-dialog",{attrs:{title:"内容",visible:e.fromVisible1,width:"50%","close-on-click-modal":!1,"destroy-on-close":""},on:{"update:visible":function(t){e.fromVisible1=t}}},[t("div",{staticClass:"w-e-text"},[t("div",{domProps:{innerHTML:e._s(e.content)}})])])],1)},l=[],r={name:"Goods",data(){return{tableData:[],pageNum:1,pageSize:10,total:0,name:null,userId:null,fromVisible:!1,form:{},userForm:[],user:JSON.parse(localStorage.getItem("xm-user")||"{}"),rules:{},ids:[],content:"",fromVisible1:!1}},created(){this.load(1),this.getUser()},methods:{getUser(){this.$request.get("/user/selectAll").then((e=>{"200"==e.code?this.userForm=e.data:this.$message.error(e.msg)}))},preview(e){this.content=e,this.fromVisible1=!0},changeStatus(e,t){this.$confirm("您确定操作吗？","确认操作",{type:"warning"}).then((s=>{this.form=JSON.parse(JSON.stringify(e)),this.form.status=t,this.$request.put("/goods/update",this.form).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.load(1)):this.$message.error(e.msg)}))})).catch((e=>{}))},handleAdd(){this.form={},this.fromVisible=!0},handleEdit(e){this.form=JSON.parse(JSON.stringify(e)),this.fromVisible=!0},save(){this.$refs.formRef.validate((e=>{e&&this.$request({url:this.form.id?"/goods/update":"/goods/add",method:this.form.id?"PUT":"POST",data:this.form}).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.load(1),this.fromVisible=!1):this.$message.error(e.msg)}))}))},del(e){this.$confirm("您确定删除吗？","确认删除",{type:"warning"}).then((t=>{this.$request.delete("/goods/delete/"+e).then((e=>{"200"===e.code?(this.$message.success("操作成功"),this.load(1)):this.$message.error(e.msg)}))})).catch((()=>{}))},handleSelectionChange(e){this.ids=e.map((e=>e.id))},delBatch(){this.ids.length?this.$confirm("您确定批量删除这些数据吗？","确认删除",{type:"warning"}).then((e=>{this.$request.delete("/goods/delete/batch",{data:this.ids}).then((e=>{"200"===e.code?(this.$message.success("操作成功"),this.load(1)):this.$message.error(e.msg)}))})).catch((()=>{})):this.$message.warning("请选择数据")},load(e){e&&(this.pageNum=e),this.$request.get("/goods/selectPage",{params:{pageNum:this.pageNum,pageSize:this.pageSize,name:this.name,userId:this.userId}}).then((e=>{"200"===e.code?(this.tableData=e.data?.list,this.total=e.data?.total):this.$message.error(e.msg)}))},reset(){this.name=null,this.userId=null,this.load(1)},handleCurrentChange(e){this.load(e)},handleAvatarSuccess(e,t,s){this.form.img=e.data}}},i=r,n=s(3736),o=(0,n.Z)(i,a,l,!1,null,"52a7ddda",null),c=o.exports}}]);
//# sourceMappingURL=437.cf7a0d50.js.map