<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 左右布局 -->
    <el-row :gutter="20">
      <!-- 第一部分（左侧） -->
      <el-col :span="12" class="box1">
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
        </el-row>
        <el-table v-loading="loading" :data="futuresList" :row-class-name="rowClassName" >
          <el-table-column type="selection" width="55" align="center" />
<!--          <el-table-column label="编码" align="center" prop="code" />-->
          <el-table-column label="名称" align="center" prop="name" />
          <el-table-column label="数值" align="center" prop="price" />
          <el-table-column label="偏离" align="center" prop="proportion" />
          <el-table-column label="振幅" align="center" prop="dailySpread" />
          <el-table-column label="当前振幅" align="center" prop="theCurrentAmplitude" />
          <el-table-column label="振幅" align="center" prop="dailySpread" />
          <el-table-column label="提示振幅" align="center" prop="undulate" />
<!--          <el-table-column label="波动提示值" align="center" prop="undulate" />-->
        </el-table>
      </el-col>

      <!-- 第二部分（右侧） -->
      <el-col :span="12" class="box1">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              size="mini"
              @click="logSina15('start')"
              v-hasPermi="['security:future:start']"
            >开始</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              size="mini"
              @click="logSina15('stop')"
              v-hasPermi="['security:future:stop']"
            >停止</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              size="mini"
              @click="getSinaList"
              v-hasPermi="['security:future:stop']"
            >刷新</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="futuresSinaList" :row-class-name="rowClassName" >
          <el-table-column type="selection" width="55" align="center" />
<!--          <el-table-column label="编码" align="center" prop="code" />-->
          <el-table-column label="名称" align="center" prop="name" />
          <el-table-column label="数值" align="center"  prop="price" />
          <el-table-column label="信息" align="center" prop="msg" />
          <el-table-column label="时间" align="center" prop="time" />
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {findList,findSinaList,logSina15} from "@/api/security/indexf";

export default {
  name: "indexf",
  data() {
    return {
      // 遮罩层
      loading: true,
      sinaLoading: true,
      // 总条数
      total: 0,
      sinaTotal: 0,
      // 证劵交易数据源表格数据
      futuresList: [],
      futuresSinaList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100
      },
      // 轮询定时器
      clearTimeSet: null,
      // 轮询定时器
      clearSinaTimeSet: null
    };
  },
  created() {
    this.refresh();
    this.refreshSina();
  },
  beforeDestroy() {
    this.stop(); // 确保在组件销毁时清除定时器
    this.stopSina(); // 确保在组件销毁时清除定时器
  },
  methods: {
    /** 查询证劵交易数据源列表 */
    getList() {
      //this.loading = true;
      findList().then(response => {
        this.futuresList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    getSinaList() {
      //this.loading = true;
      findSinaList().then(response => {
        this.futuresSinaList = response.rows;
        this.sinaTotal = response.total;
        this.sinaLoading = false;
      });
    },

    logSina15(flag) {
      logSina15(flag).then(response => {
      });
    },

    rowClassName({ row }) {
      if (row.positiveNegativeFlag === 1) {
        return 'row-red';
      }else if (row.positiveNegativeFlag === 2){
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
    refreshSina() { // 从服务端加载数据的函数
      if(this.futuresSinaList.length <= 0){
        this.getSinaList();
      }
      // 实现轮询，每 4 秒发一次请求
      if (this.clearSinaTimeSet === null) { // 确保只创建一个定时器
        this.clearSinaTimeSet = setInterval(() => this.getSinaList(), 4000);
      }
    },
    stop(){
      if (this.clearTimeSet !== null) {
        clearInterval(this.clearTimeSet);
        this.clearTimeSet = null; // 重置定时器
      }
    }
    ,
    stopSina(){
      if (this.clearSinaTimeSet !== null) {
        clearInterval(this.clearSinaTimeSet);
        this.clearSinaTimeSet = null; // 重置定时器
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

