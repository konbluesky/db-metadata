<template>
    <scroll-pane  id="tags-view-container" ref="scrollPane">
      <template v-for="(tag, index) in visitedViews">
        <pop-menu :ref="'popMenu' + index" trigger="right-click" @show="openMenu(tag)" class="tags-view-item"
                  :style="isActive(tag) ? {'background-color': bgColor, 'color': color, 'border-color': bgColor} : {}">
          <template #label>
            <router-link
                ref="tag"
                :key="tag.fullPath"
                :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
                tag="span"
                class="router-link"
                @click.middle.native="!isAffix(tag)?closeSelectedTag(tag):''"
            >
              <span>{{ tag.meta.title }}</span>
              <span v-if="!isAffix(tag)" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)"/>
            </router-link>
          </template>
          <list style="width: 80px;">
            <list-item style="height: 22px; line-height: 22px;"
                       @click="refreshSelectedTag(selectedTag); closePopMenu('popMenu' + index)"
                       v-if="isActive(tag)">刷新
            </list-item>
            <list-item style="height: 22px; line-height: 22px;" v-if="!isAffix(selectedTag)"
                       @click="closeSelectedTag(selectedTag); closePopMenu('popMenu' + index)">关闭
            </list-item>
            <list-item style="height: 22px; line-height: 22px;"
                       @click="closeOthersTags(selectedTag); closePopMenu('popMenu' + index)">关闭其他
            </list-item>
            <list-item style="height: 22px; line-height: 22px;"
                       @click="closeAllTags(selectedTag); closePopMenu('popMenu' + index)">关闭所有
            </list-item>
            <list-item style="height: 22px; line-height: 22px;" @click="editRoute(selectedTag)"
                       v-if="$isRoot() && currentRouteEditable">编辑路由
            </list-item>
          </list>
        </pop-menu>
      </template>
    </scroll-pane>
</template>

<script>
import ScrollPane from './ScrollPane'
import path from 'path'
import * as TagViewUtil from '../visitedViewMaintain'
import {tagData} from "../data";
import Conf from '../conf'
import {isArray, isEmpty} from "../../../utils/common";
import {getUpdateFormMeta} from "../../../utils/rest";

/**
 * TODO TagView的切换缓存基于vue 动态组件的 keep-alive, 因此keep-alive中的include只能根据组件名来, 而TagView的数据基于路由,
 * 如果多个路由共用一个组件, 则都会被keep-alive命中
 */
export default {
  name: "TagView",
  components: {ScrollPane},
  data() {
    return {
      top: 0,
      left: 0,
      selectedTag: {},
      affixTags: tagData.affixTags,
      visitedViews: tagData.visitedViews,
      cachedViews: tagData.cachedViews,
      showPopMenu: false
    }
  },
  computed: {
    routes() {
      return this.$router.options.routes // TODO 此值并不能获取到addRoutes动态添加进来的路由, 导致无法初始化并固定affix路由
    },
    color() {
      return Conf.color
    },
    bgColor() {
      return Conf.bgColor
    },
    currentRouteEditable() {
      const {selectedTag: {meta: {id} = {}}} = this
      return !isEmpty(id)
    }
  },
  watch: {
    $route() {
      this.addTags()
      this.moveToCurrentTag()
    },
    cachedViews: {
      handler: function (newV) {
        this.$emit('cacheViewChange', newV)
      },
      deep: true
    }
  },
  mounted() {
    this.initTags()
    this.addTags()
  },
  methods: {
    ...TagViewUtil,
    editRoute(route) {
      const {meta: {id}} = route
      getUpdateFormMeta('meta_router', id).then(({data: meta}) => {
        this.$dialog(meta, null, {
          title: '编辑路由'
        })
      })
    },
    closePopMenu(refName) {
      const ref = this.$refs[refName]
      if (isEmpty(ref)) return;
      if (isArray(ref)) {
        ref[0].close()
      } else {
        ref.close()
      }
    },
    isActive(route) {
      return route.fullPath === this.$route.fullPath
    },
    isAffix(tag) {
      return tag.meta && tag.meta.affix
    },
    filterAffixTags(routes, basePath = '/') {
      let tags = []
      routes.forEach(route => {
        if (route.meta && route.meta.affix) {
          const tagPath = path.resolve(basePath, route.path)
          const tagFullPath = path.resolve(basePath, route.fullPath)
          tags.push({
            fullPath: tagFullPath,
            path: tagPath,
            name: route.name,
            meta: {...route.meta}
          })
        }
        if (route.children) {
          const tempTags = this.filterAffixTags(route.children, route.path)
          if (tempTags.length >= 1) {
            tags = [...tags, ...tempTags]
          }
        }
      })
      return tags
    },
    initTags() {
      const affixTags = this.affixTags = this.filterAffixTags(this.routes)
      for (const tag of affixTags) {
        if (tag.name) { // 必须有路由名name
          this.addVisitedView(tag)
        }
      }
    },
    addTags() {
      const {name} = this.$route
      if (name) {
        this.addView(this.$route)
      }
      return false
    },
    moveToCurrentTag() {
      const tags = this.$refs.tag
      this.$nextTick(() => {
        for (const tag of tags) {
          if (tag.to.fullPath === this.$route.fullPath) {
            this.$refs.scrollPane.moveToTarget(tag)
            // when query is different then update
            // if (tag.to.fullPath !== this.$route.fullPath) {
            //   this.updateVisitedView(this.$route)
            // }
            break
          }
        }
      })
    },
    refreshSelectedTag(view) {
      this.deleteCachedView(view).then(() => {
        const {fullPath} = view
        this.$nextTick(() => {
          this.$router.replace({
            path: '/__redirect' + fullPath
          })
        })
      })
    },
    closeSelectedTag(view) {
      this.deleteView(view).then(({visitedViews}) => {
        if (this.isActive(view)) {
          this.toLastView(visitedViews, view)
        }
      })
    },
    closeOthersTags(view) {
      if (view.fullPath !== this.$route.fullPath) {
        this.$router.push(view)
      }
      this.deleteOtherView(this.selectedTag).then(() => {
        this.moveToCurrentTag()
      })
    },
    closeAllTags(view) {
      this.deleteAllViews(view).then(({visitedViews}) => {
        if (this.affixTags.some(tag => tag.fullPath === view.fullPath)) {
          return
        }
        this.toLastView(visitedViews, view)
      })
    },
    toLastView(visitedViews, view) {
      const latestView = visitedViews.slice(-1)[0]
      if (latestView) {
        this.$router.push(latestView.fullPath)
      } else {
        this.$router.push(Conf.outPath)
      }
    },
    openMenu(tag) {
      this.selectedTag = tag
    }
  }
}
</script>

<style lang="scss" scoped>
$tagBarHeight: 38px;
#tags-view-container {
  height: $tagBarHeight;
  width: 100%;
  background: #ffffff;
  box-shadow: 0 6px 6px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);

  $tagHeight: $tagBarHeight - 8;

  .tags-view-item {
    display: inline-block;
    position: relative;
    cursor: pointer;
    height: $tagHeight;
    line-height: $tagHeight;
    border: 1px solid #d8dce5;
    font-size: 12px;
    margin: 4px 2px;

    .router-link {
      display: block;
      padding: 0 10px;
    }

    &:first-of-type {
      margin-left: 5px;
    }

    &:last-of-type {
      margin-right: 5px;
    }
  }
}
</style>