<template>
    <div class="container el-card">
        <div class="header" v-if="operationBarConf.show">
            <slot name="operation-bar"
                  v-bind:conf="operationBarConf"
                  v-bind:operations="{handleAdd, handleBatchDelete}">
                <el-button-group>
                    <slot name="prefix-btn" v-bind:conf="operationBarConf"></slot>
                    <slot name="add-btn" v-bind:conf="operationBarConf" v-bind:add="handleAdd" v-if="editable">
                        <el-button @click="handleAdd" icon="el-icon-document-add"
                                   v-bind="operationBarConf">新增
                        </el-button>
                    </slot>
                    <slot name="batch-delete-btn" v-bind:conf="operationBarConf"
                          v-bind:batchDelete="handleBatchDelete" v-if="multiMode && editable">
                        <el-button @click="handleBatchDelete($event)" type="danger" icon="el-icon-delete-solid"
                                   v-bind="operationBarConf">删除
                        </el-button>
                    </slot>

                    <el-dropdown @command="handleCommand">
                        <el-button type="primary" v-bind="operationBarConf">
                            <span>节点操作</span>
                            <i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="handleExpandAll">
                                <i class="el-icon-caret-bottom"></i>
                                <span>展开所有</span>
                            </el-dropdown-item>
                            <el-dropdown-item command="handleShrinkAll">
                                <i class="el-icon-caret-right"></i>
                                <span>收起所有</span>
                            </el-dropdown-item>
                            <el-dropdown-item command="handleChoseAll" v-if="multiMode">
                                <i class="el-icon-circle-check"></i>
                                <span>全选</span>
                            </el-dropdown-item>
                            <el-dropdown-item command="handleCleanChose" v-if="multiMode">
                                <i class="el-icon-circle-close"></i>
                                <span>取消全选</span>
                            </el-dropdown-item>
                            <el-dropdown-item command="handleReverseChose" v-if="multiMode">
                                <i class="el-icon-success"></i>
                                <span>反选</span>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <slot name="suffix-btn" v-bind:conf="operationBarConf"></slot>
                </el-button-group>
            </slot>
        </div>
        <el-tree :ref="innerMeta['name']" :data="innerData"
                 v-bind="$reverseMerge(innerMeta.conf, $attrs)"
                 :props="props"
                 @node-click="handleNodeClick"
                 @node-contextmenu="$emit('node-contextmenu')"
                 @check-change="$emit('check-change')"
                 @check="handleNodeCheck"
                 @current-change="$emit('current-change')"
                 @node-expand="$emit('node-expand')"
                 @node-collapse="$emit('node-collapse')">
            <template slot-scope="{ node, data }">
                <slot v-bind:node="node" v-bind:data="data">
                    <span class="el-tree-node__label">{{data[innerMeta.conf['props']['label']]}}</span>
                </slot>
            </template>
        </el-tree>
        <dialog-box :visible="visible" title="节点编辑">
            <form-tmpl :meta="formMeta"></form-tmpl>
        </dialog-box>

        <div style="float: right">
          <meta-easy-edit :object-code="innerMeta.objectCode" component-code="TreeView">
            <template #label><i class="el-icon-setting"></i></template>
          </meta-easy-edit>
        </div>
    </div>
</template>

<script>
    import utils from '../../../utils'
    import {defaultPrimaryKey} from '../../../config'
    import Meta from '../../mixins/meta'
    import DefaultMeta, {CHOSE_TYPE} from '../ui-conf'
    import MetaEasyEdit from "../../meta/src/MetaEasyEdit";
    import {assertEmpty} from "../../../utils/common";

    export default {
        mixins: [Meta(DefaultMeta)],
        name: "TreeView",
        components: {MetaEasyEdit},
        props: {
            data: Array
        },
        data() {
            return {
                innerData: [],
                activeData: {},
                choseData: [],
                halfChoseData: [],
                formMeta: {},
                visible: false
            }
        },
        methods: {
            getAllNodes() {
                return this.ref.store._getAllNodes();
            },
            getCheckedNodes() {
                return this.ref.getCheckedNodes();
            },
            handleCommand(fn) {
                if (this[fn]) this[fn]();
            },
            handleReverseChose() {
                this.getAllNodes().forEach(node => node.checked = !node.checked);
                this.$emit('chose-change', this.getCheckedNodes())
            },
            handleChoseAll() {
                this.ref.setCheckedNodes(this.innerData);
                this.$emit('chose-change', this.getCheckedNodes())
            },
            handleCleanChose() {
                this.ref.setCheckedKeys([]);
                this.$emit('chose-change', this.getCheckedNodes())
            },
            handleExpandAll() {
                this.getAllNodes().forEach(node => node.expanded = true);
            },
            handleShrinkAll() {
                this.getAllNodes().forEach(node => node.expanded = false);
            },
            handleAdd() {
                let currentNode = this.ref.getCurrentNode();
                if (currentNode) {
                    // pxg_todo 新增节点

                } else {
                    this.$message.warning('请先选择一个节点');
                }

            },
            handleBatchDelete() {

            },
            handleNodeClick(row, node, event) {
                const {primaryKey, operLogic: {'chose_type': choseType} = {}} = this;

                if (row[primaryKey] === this.activeData[primaryKey]) {  // cancel active row
                    if (choseType === CHOSE_TYPE.toggle) {
                      this.activeData = {};
                    }
                } else {
                    this.activeData = row;
                }
                this.$emit('active-change', this.activeData);
            },
            handleNodeCheck(row, {checkedNodes, checkedKeys, halfCheckedNodes, halfCheckedKeys}) {
                const {multiMode} = this;
                if (!multiMode) return;
                this.choseData = checkedNodes;
                this.halfChoseData = halfCheckedNodes;
                this.$emit('chose-change', checkedNodes);
                this.$emit('half-chose-change', halfCheckedNodes);
            },

            filter(value) {
                this.ref.filter(value);
            },
            getChechedNodes() {
                return this.ref.getCheckedNodes();
            },
            getCheckedKeys() {
                return this.ref.getCheckedKeys();
            },
            setCheckedKeys(keys) {
                this.ref.setCheckedKeys(keys);
            },
            getHalfCheckedNodes() {
                return this.ref.getHalfCheckedNodes();
            },
            getHalfCheckedKeys() {
                return this.ref.getHalfCheckedKeys();
            },
            setCurrentKey(key) {
                return this.ref.setCurrentKey(key);
            },
            getCurrentKey() {
                return this.ref.getCurrentKey();
            },
            getCurrentNode() {
                return this.ref.getCurrentNode();
            },
            getNode(key) {
                return this.ref.getNode(key);
            },
            remove(key) {
                this.ref.remove(key);
            },
            append(data, parentNode) {
                this.ref.append(data, parentNode);
            },
            insertBefore(data, refNode) {
                this.ref.append(data, refNode);
            },
            insertAfter(data, refNode) {
                this.ref.append(data, refNode);
            },
            getData(params) {
                if (!utils.isObject(params)) params = {};

                if (!this.innerMeta.hasOwnProperty('data_url')) {
                    console.error('lack data_url attribute');
                    return;
                }

                let url = this.innerMeta['data_url'];

                this.$axios.safeGet(url, {
                    params: params
                }).then(resp => {
                    this.innerData = resp.data;
                    this.$emit("update:data", resp.data);
                }).catch(({msg = '获取TreeView数据发生异常'}) => {
                    this.$message.error(msg);
                });
            },
            initData() { // init business data
                let {data} = this;
                if (data !== undefined) {
                    this.innerData = data;
                    return;
                }
                if (this.innerMeta.hasOwnProperty('data_url')) {
                    this.getData();
                    return;
                }
                console.error("data or data_url in meta provide one at least!")
            },
        },
        watch: {
            'data': function (newVal, oldVal) {
                this.initData();    // 为避免data数据过大, 不进行深度监听
            },
            'innerMeta.data_url': {
                handler: function () {
                    this.initData();
                },
                immediate: false
            }
        },
        mounted() {
            this.initData();
        },
        computed: {
            innerMeta() {
              return this.$merge(this.meta, DefaultMeta);
            },
            ref() {
                const {innerMeta: {name} = {}, $refs} = this
                return $refs[name]
            },
            multiMode() {
                const {innerMeta: {conf: {'show-checkbox': multiMode} = {}} = {}} = this
                return multiMode;
            },
            editable() {
                const {innerMeta: {editable} = {}} = this
                return editable
            },
            operLogic() {
                const {innerMeta: {'oper-logic': operLogic} = {}} = this
                return operLogic
            },
            props() {
                return this.innerMeta['conf']['props'];
            },
            operationBarConf() {
                return this.innerMeta['operation-bar'];
            },
            primaryKey() {
                const {objectPrimaryKey} = this.meta
                return assertEmpty(objectPrimaryKey, defaultPrimaryKey)
            }
        }
    }
</script>

<style scoped>
  .container {
      height: 100%;
      box-sizing: border-box;
  }
</style>
