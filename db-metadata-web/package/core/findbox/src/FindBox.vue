<template>
    <div>
        <el-input v-model="nativeValue"
                  v-bind="$reverseMerge(innerMeta.conf, $attrs)"
                  :name="innerMeta.name"
                  @blur="$emit('blur', $event)"
                  @focus="$emit('focus', $event)"
                  @change="$emit('change', $event)"
                  @clear="handlerClear($event)"
                  v-on:click.native="handlerClick($event)"
                  suffix-icon="el-icon-search"
        ></el-input>
        <dialog-box :visible.sync="dialogVisible" :meta="dialogMeta">
            <find-panel :meta="findPanelMeta" @ok="handlerOk" @cancel="handlerCancel"></find-panel>
            <template #footer><span></span></template>
        </dialog-box>
    </div>
</template>

<script>
    import utils from '../../../utils'
    import Meta from '../../mixins/meta'
    import Val from '../../mixins/value'
    import FindPanel from './FindPanel'
    import DefaultMeta from '../ui-conf'

    export default {
        mixins: [Meta(DefaultMeta), Val()],
        name: "FindBox",
        label: "查找框",
        components: {FindPanel},
        props: {
            value: [String, Number, Array]
        },
        data() {
            return {
                findPanelMeta: {},
                dialogMeta: {},
                dialogVisible: false
            }
        },
        methods: {
            handlerClick(ev) {
                if (ev) ev.stopPropagation();
                const {innerMeta} = this;
                const {data_url: url} = innerMeta;

                if (!url) {
                    console.error('data_url is required property, and not nullable. meta: %o', innerMeta);
                    return;
                }
                this.$axios.get(url).then(resp => {
                    this.findPanelMeta = resp.data;
                    this.findPanelMeta.component_name = 'FindPanel';
                    this.dialogVisible = true;
                }).catch(({msg ='Error'}) => {
                    this.$message.error(msg);
                })
            },
            handlerOk(row) {
                const {table: tableMeta} = this.findPanelMeta;
                const {objectPrimaryKey} = tableMeta;
                const feedBackValue = utils.extractValue(row, objectPrimaryKey);
                if (this.$listeners.hasOwnProperty('ok')) {
                    this.$emit('ok', {row, meta: tableMeta});
                    this.dialogVisible = false;
                    return;
                }

                this.nativeValue = feedBackValue[0];
                this.dialogVisible = false;
            },
            handlerCancel() {
                this.nativeValue = null;
                this.dialogVisible = false;
            },
            handlerClear(ev) {
                if (ev) ev.stopPropagation();
                this.$emit('clear', ev);
            }
        }
    }
</script>

<style scoped>

</style>
