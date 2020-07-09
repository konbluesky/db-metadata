<template>
    <el-container style="height: 100%; display: flex; overflow: hidden;">
        <div style="flex: 2">
            <ComponentList></ComponentList>
        </div>
        <div style="flex: 5;margin-left: 5px">
            <WorkArea @select="handleSelectFormItem" v-model="formMeta">
                <template #operation-extend>
                    <drop-down-box placeholder="选择元对象" v-model="objectCode" :data-url="metaObjectCodeUrl"
                                   @change="handleChange" @clear="handleClear" filterable>
                        <template #options="{options}">
                            <el-option v-for="item in options" :key="item.code" :label="item.code"
                                       :value="item.code">
                                {{item.code}}
                            </el-option>
                        </template>
                    </drop-down-box>
                </template>
            </WorkArea>
        </div>
        <div style="flex: 3;margin-left: 5px">
            <ConfArea v-model="formMeta" :select-index="selectIndex"></ConfArea>
        </div>
    </el-container>
</template>

<script>
    import utils from '../../utils'
    import {restUrl} from "../../constant/url";
    import ComponentList from './ComponentList'
    import WorkArea from './WorkArea'
    import ConfArea from './ConfArea'
    import DefaultFormViewMeta from '../../core/form/ui-conf'

    export default {
        name: "FormBuilder",
        components: {ComponentList, WorkArea, ConfArea},
        props: {
            oc: String
        },
        data() {
            const objectCode = utils.assertUndefined(this.oc, this.$route.query.objectCode);
            return {
                formMeta: this.$merge({}, DefaultFormViewMeta),
                selectIndex: null,
                objectCode: objectCode,
                metaObjectCodeUrl: restUrl.OBJECT_CODE_LIST,
            }
        },
        methods: {
            handleClear() {
                this.objectCode = null;
                this.handleChange();
                this.setInitState();
            },
            handleChange() {
                const {objectCode} = this;
                this.loadConf(objectCode);
                this.$emit('oc-change', objectCode);
            },
            setInitState() {
                this.formMeta = this.$merge({}, DefaultFormViewMeta);
            },
            handleSelectFormItem(formMeta, selectIndex) {
                this.formMeta = formMeta;
                this.selectIndex = selectIndex;
            },
            loadConf(objectCode) {
                if (utils.isEmpty(objectCode)) return;
                const url = this.$compile(restUrl.COMPONENT_INSTANCE_META, {
                    componentCode: 'FormView',
                    objectCode: objectCode
                });
                this.$axios.safeGet(url).then(resp => {
                    let formMeta = resp.data;
                    this.$reverseMerge(this.formMeta, formMeta, true);
                }).catch(err => {
                    this.$message.error(err.msg);
                    this.setInitState();
                })
            }
        },
        watch: {
            oc(newVal) {
                this.objectCode = newVal;
                this.loadConf(this.objectCode);
            }
        },
        mounted() {
            const objectCode = this.objectCode;
            this.loadConf(objectCode);
        },
    }
</script>

<style scoped>
    .el-row, .el-col {
        height: 100%;
    }
</style>