<template>
  <div style="width: 50%; margin: 10px auto">
    <div class="card" style="margin-bottom: 10px">
      <div style="font-size: 20px; text-align: center; margin-bottom: 20px">{{ posts.title }}</div>
      <div style="color: #666; margin-bottom: 40px; text-align: center">
        <span><i class="el-icon-user"></i> {{ posts.userName }}</span>
        <span style="margin-left: 20px"><i class="el-icon-time"></i> {{ posts.time }}</span>
        <span style="margin-left: 20px"><i class="el-icon-reading"></i> {{ posts.readCount }}</span>
      </div>
      <div class="w-e-text" v-html="posts.content"></div>
    </div>

    <div class="card" style="padding: 20px 30px">
      <Comment :fid="id" module="posts"></Comment>
    </div>
  </div>
</template>

<script>
import Comment from "@/components/Comment";
export default {
  name: "PostsDetail",
  components: {Comment},
  data() {
    return {
      id: this.$route.query.id,
      posts: {}
    }
  },
  created() {
    this.$request.put('/posts/updateCount/' + this.id).then(res => {
      this.load()
    })
  },
  methods: {
    load() {
      this.$request.get('/posts/selectById/' + this.id).then(res => {
        if(res.code == '200'){
            this.posts = res.data || {}
        }else{
            this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>