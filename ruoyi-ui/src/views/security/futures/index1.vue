<template>
  <div class="app-container merged-container">
    <!-- ==================== 左侧：实时监控（占 4/5） ==================== -->
    <div class="left-panel">
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

      <el-table
        v-loading="monitorLoading"
        :data="monitorList"
        :row-class-name="rowClassName"
      >
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
        v-show="monitorTotal > 0"
        :total="monitorTotal"
        :page.sync="monitorQueryParams.pageNum"
        :limit.sync="monitorQueryParams.pageSize"
        @pagination="getMonitorList"
      />
    </div>

    <!-- ==================== 右侧：管理（占 1/5，仅名称搜索 + 修改按钮） ==================== -->
    <div class="right-panel">
      <el-form
        :model="queryParams"
        ref="queryForm"
        size="small"
        v-show="showSearch"
        label-width="68px"
      >
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="futuresList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="名称" align="center" prop="name" />
        <el-table-column label="主键" align="center" prop="id" />
        <el-table-column label="编码" align="center" prop="code" />
        <el-table-column label="交易所-编码" align="center" prop="exchangeCode" />
        <el-table-column label="偏离值" align="center" prop="deviation" />
        <el-table-column label="波动" align="center" prop="undulate" />
        <el-table-column label="类型" align="center" prop="type" />
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />

      <!-- 修改对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="编码" prop="code">
            <el-input v-model="form.code" placeholder="请输入编码" />
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入名称" />
          </el-form-item>
          <el-form-item label="交易所-编码" prop="exchangeCode">
            <el-input v-model="form.exchangeCode" placeholder="请输入交易所-编码" />
          </el-form-item>
          <el-form-item label="偏离值" prop="deviation">
            <el-input v-model="form.deviation" placeholder="请输入偏离值" />
          </el-form-item>
          <el-form-item label="波动" prop="undulate">
            <el-input v-model="form.undulate" placeholder="请输入波动" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态">
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { findListByPoints } from "@/api/security/future";
import {
  listFutures,
  getFutures,
  updateFutures
} from "@/api/security/futures";

export default {
  name: "FuturesMerged",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      /* ========== 左侧：实时监控 ========== */
      monitorLoading: true,
      monitorTotal: 0,
      monitorList: [],
      monitorQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      clearTimeSet: null,

      /* ========== 右侧：管理 ========== */
      loading: true,
      ids: [],
      showSearch: true,
      total: 0,
      futuresList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      form: {},
      rules: {}
    };
  },
  created() {
    // 左侧：初次加载
    this.refreshOne();
    // 右侧：初次加载
    this.getList();
  },
  beforeDestroy() {
    this.stop(); // 销毁时清理轮询定时器
  },
  methods: {
    /* ==================== 左侧方法 ==================== */
    getMonitorList() {
      findListByPoints().then(response => {
        this.monitorList = response.rows;
        this.monitorTotal = response.total;
        this.monitorLoading = false;
      });
    },
    rowClassName({ row }) {
      if (row.num >= 100) {
        return '';
      } else if (row.num <= 5) {
        return 'row-red';
      } else if (row.num <= 10) {
        return 'row-yellow';
      } else if (row.num <= 20) {
        return 'row-green';
      }
      return '';
    },
    refresh() {
      if (this.monitorList.length <= 0) {
        this.getMonitorList();
      }
      if (this.clearTimeSet === null) {
        this.clearTimeSet = setInterval(() => this.getMonitorList(), 4000);
      }
    },
    refreshOne() {
      this.getMonitorList();
    },
    stop() {
      if (this.clearTimeSet !== null) {
        clearInterval(this.clearTimeSet);
        this.clearTimeSet = null;
      }
    },

    /* ==================== 右侧方法 ==================== */
    getList() {
      this.loading = true;
      listFutures(this.queryParams).then(response => {
        this.futuresList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        code: null,
        name: null,
        exchangeCode: null,
        deviation: null,
        undulate: null,
        type: null,
        status: null,
        addUser: null,
        addTime: null,
        modifierUser: null,
        modifierTime: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getFutures(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改证劵交易";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateFutures(this.form).then(() => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
        }
      });
    }
  }
};
</script>

<style scoped>
/* ==================== 左右布局 ==================== */
.merged-container {
  display: flex;
  width: 100%;
  height: calc(100vh - 84px); /* 减去顶部导航/tagsView高度，可按需调整 */
  gap: 10px;
  box-sizing: border-box;
  overflow: hidden;
}

/* 左侧占 4/5 */
.left-panel {
  flex: 4 1 0;
  min-width: 0;      /* 关键：防止 flex 子项被表格撑开溢出 */
  overflow: auto;    /* 内容超出时各自滚动 */
  padding: 10px;
  background: #fff;
  border-radius: 4px;
  border-right: 1px solid #e0e0e0;
}

/* 右侧占 1/5 */
.right-panel {
  flex: 1 1 0;
  min-width: 0;
  overflow: auto;
  padding: 10px;
  background: #fff;
  border-radius: 4px;
}

/* 左侧表格行颜色（原样式） */
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
