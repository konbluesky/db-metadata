<template>
    <el-form v-bind="bindConf" :model="value" size="mini">
        <template v-for="(item, index) in innerMeta.columns">
            <el-form-item :key="item.name + index" :label="item.label||item.name" :prop="item.name"
                          :class="{'inline': item.inline, 'width-align': item.inline}">
                <component :is="item.component_name" v-model="value[item.name]" :meta="item" v-bind="bindConf"></component>
            </el-form-item>
        </template>
    </el-form>
</template>

<script>
    import Meta from '../../mixins/meta'
    import Val from '../../mixins/value'
    import DefaultFormViewMeta from '../../formview/ui-conf'

    export default {
        name: "MiniForm",
        label: "迷你表单",
        description: "输入控件的一种,JsonBox的表单表现形式",
        mixins: [Meta(DefaultFormViewMeta), Val()],
        props: {
            value: {
                type: [Object, String],
                default: () => {}
            }
        },
        computed: {
          bindConf() {
            const {innerMeta: {component_name}, innerMeta, $attrs} = this
            if (component_name === 'MiniFormBox' || component_name === 'FormView') {
              return this.$reverseMerge(innerMeta.conf, $attrs)
            }
            return {}
          }
        }
    }
</script>

<style scoped>

</style>
