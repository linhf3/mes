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
        <el-button
          type="warning"
          plain
          size="mini"
          @click="refreshOne"
          v-hasPermi="['security:future:start']"
        >搜索</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="futuresList" :row-class-name="rowClassName" >
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="数值" align="center" prop="price" />
      <el-table-column label="偏离" align="center" prop="proportion" />
      <el-table-column label="排名" align="center" prop="num">
        <template #default="{ row }">
          {{ row.num === 21 || row.num == 100 ? '-' : row.num }}
        </template>
      </el-table-column>
      <el-table-column label="振幅" align="center" prop="dailySpread" />
      <el-table-column label="当前振幅" align="center" prop="theCurrentAmplitude" />
      <el-table-column label="前二十" width="650" align="center" prop="dailySpread5" />
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
import {findListByPoints} from "@/api/security/future";

export default {
  name: "Future",
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
    this.refreshOne();
  },
  beforeDestroy() {
    this.stop(); // 确保在组件销毁时清除定时器
  },
  methods: {
    /** 查询证劵交易数据源列表 */
    getList() {
      //this.loading = true;
      findListByPoints().then(response => {
        this.futuresList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    rowClassName({ row }) {
      if (row.num >= 100){
        return '';
      }else if (row.num <= 5) {
        return 'row-red';
      }else if (row.num <= 10){
        return 'row-yellow';
      }else if (row.num <= 20){
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
      // 实现轮询，每 5 秒发一次请求
      if (this.clearTimeSet === null) { // 确保只创建一个定时器
        this.clearTimeSet = setInterval(() => this.getList(), 4000);
      }
    },
    refreshOne() { // 从服务端加载数据的函数
        this.getList();
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
.row-green {
  color: green;
}
.row-red {
  color: red;
}
.row-yellow {
  color: #af3f3f;
}
</style>
