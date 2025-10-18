<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          size="mini"
          @click="refresh"
          v-hasPermi="['security:future:start']"
        >开始</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          size="mini"
          @click="stop"
          v-hasPermi="['security:future:stop']"
        >停止</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          size="mini"
          @click="getList"
          v-hasPermi="['security:future:stop']"
        >刷新</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="futuresList" :row-class-name="rowClassName" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编码" align="center" prop="code" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="数值" align="center" prop="price" />
      <el-table-column label="信息" align="center" prop="msg" />
      <el-table-column label="时间" align="center" prop="time" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import {findSinaFiveList} from "@/api/security/sinafuture";

export default {
  name: "FutureSina",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 证劵交易数据源表格数据
      futuresList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 轮询定时器
      clearTimeSet: null
    };
  },
  created() {
    this.refresh();
  },
  beforeDestroy() {
    this.stop(); // 确保在组件销毁时清除定时器
  },
  methods: {
    /** 查询证劵交易数据源列表 */
    getList() {
      //this.loading = true;
      findSinaFiveList().then(response => {
        this.futuresList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    rowClassName({ row }) {
      if (row.positiveNegativeFlag === 1 || row.positiveNegativeFlag === 3) {
        return 'row-red';
      }else if (row.positiveNegativeFlag === 2 || row.positiveNegativeFlag === 4){
        return 'row-green';
      }
      return '';
    },
    /** 爬取数据按钮操作 */
    handleCrawl() {
      crawl();
      this.getList();
    },
    refresh() { // 从服务端加载数据的函数
      if(this.futuresList.length <= 0){
        this.getList();
      }
      // 实现轮询，每 3 秒发一次请求
      if (this.clearTimeSet === null) { // 确保只创建一个定时器
        this.clearTimeSet = setInterval(() => this.getList(), 3000);
      }
    },
    stop(){
      if (this.clearTimeSet !== null) {
        clearInterval(this.clearTimeSet);
        this.clearTimeSet = null; // 重置定时器
      }
    }


  }
};
</script>

<style>
.row-red {
  color: red;
}
.row-green {
  color: green;
}
</style>
