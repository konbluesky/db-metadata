<template>
  <vue-tinymce-text v-model="nativeValue"
                    :toolbar="toolbar" :menubar="menubar"
                    :width="width" :height="height" v-bind:config="innerMeta['conf']"></vue-tinymce-text>
</template>

<script>
import VueTinymceText from 'vue-tinymce-text'
import Val from "../../mixins/value"
import Meta from '../../mixins/meta'
import defaultMeta from '../ui-conf'

export default {
  name: "RichTextBox",
  label: "富文本框",
  mixins: [Meta(defaultMeta), Val()],
  components: {
    'vue-tinymce-text': VueTinymceText
  },
  props: {
    // id: {
    //   type: String,
    //   default: () => 'vue-tinymce-text' + (Math.random() * 10000)
    // },
    value: {
      type: String,
      default: () => ''
    },
    toolbar: {
      type: Array,
      required: false,
      default() {
        return []
      }
    },
    menubar: {
      type: String,
      default: 'file edit insert view format table'
    },
    height: {
      type: [Number, String],
      required: false,
      default: 360
    },
    width: {
      type: [Number, String],
      required: false,
      default: 'auto'
    }
  }
  ,
  data() {
    return {}
  }
}
</script>

<style scoped>

</style>