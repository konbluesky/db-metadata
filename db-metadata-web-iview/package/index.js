import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import utils from './utils'
// 核心组件
import BoolBox from './core/boolbox'
import CheckBox from './core/checkbox'
import CodeBox from './core/codebox'
import DataList from './core/datalist'
import DateBox from './core/datebox'
import DateTimeBox from './core/datetimebox'
import DialogBox from './core/dialogbox'
import DropDownBox from './core/dropdownbox'
import FileBox from './core/filebox'
import FindBox from './core/findbox'
import FormView from './core/form'
import RowGrid from './core/grid'
import IconBox from './core/iconbox'
import ImgBox from './core/imgbox'
import JsonBox from './core/jsonbox'
import List from './core/list'
import ListItem from './core/listitem'
import MiniFormBox from './core/miniformbox'
import NumBox from './core/numbox'
import PassBox from './core/passbox'
import PopMenu from './core/popmenu'
import RadioBox from './core/radiobox'
import SearchPanel from './core/searchpanel'
import SqlBox from './core/sqlbox'
import Tags from './core/tags'
import TableList from './core/tablelist'
import TableTreeList from './core/tabletreelist'
import TextAreaBox from './core/textareabox'
import TextBox from './core/textbox'
import TimeBox from './core/timebox'
import Tree from './core/tree'
import ZTogglePanel from './core/ztogglepanel'
// 模板组件
import DataListTableTmpl from './template/DataListTableTmpl'
import FormTmpl from './template/FormTmpl'
import MasterSlaveTableTmpl from './template/MasterSlaveTableTmpl'
import SingleGridTmpl from './template/SingleGridTmpl'
import TableFormTmpl from './template/TableFormTmpl'
import TreeFormTmpl from './template/TreeFormTmpl'
import TreeTableTmpl from './template/TreeTableTmpl'
// meta 组件
import {MetaEasyEdit, MiniFormField, MiniFormObject} from "./core/meta"
import MetaDataManager from "./meta/MetaDataManager";
import FormBuilder from './meta/form-builder'
import GlobalConfList from "./meta/component/GlobalConfList";
import GlobalConf from "./meta/component/GlobalConf";
import InstanceConfList from "./meta/component-instance/InstanceConfList";
import InstanceConfEdit from "./meta/component-instance/InstanceConfEdit";
import InstanceConfNew from "./meta/component-instance/InstanceConfNew";

import GlobalFnRegister from './register/fn-register'
import GlobalFilterRegister from './register/filter-register'
import {restUrl, routeUrl} from './constant/url'

const components = [
    // atom or container
    BoolBox,
    CheckBox,
    CodeBox,
    DataList,
    DateBox,
    DateTimeBox,
    DialogBox,
    DropDownBox,
    FileBox,
    FindBox,
    FormView,
    RowGrid,
    IconBox,
    ImgBox,
    JsonBox,
    List,
    ListItem,
    MiniFormBox,
    MiniFormObject,
    MiniFormField,
    MetaEasyEdit,
    NumBox,
    PassBox,
    PopMenu,
    RadioBox,
    SearchPanel,
    SqlBox,
    Tags,
    TableList,
    Tree,
    TableTreeList,
    TextAreaBox,
    TextBox,
    TimeBox,
    ZTogglePanel,

    // template
    DataListTableTmpl,
    FormTmpl,
    MasterSlaveTableTmpl,
    SingleGridTmpl,
    TableFormTmpl,
    TreeFormTmpl,
    TreeTableTmpl,

    // meta
    MetaDataManager,
    FormBuilder,
    GlobalConfList,
    GlobalConf,
    InstanceConfList,
    InstanceConfEdit,
    InstanceConfNew
];

const install = function (Vue, opts = {}) {
    if (install.installed) return;

    Vue.use(ElementUI, opts);
    // 自定义url覆盖
    if (opts.routeUrl) {
        utils.reverseMerge(routeUrl, opts.routeUrl, false);
    }
    if (opts.restUrl) {
        utils.reverseMerge(restUrl, opts.restUrl, false);
    }

    Vue.use(GlobalFnRegister, opts);    // 全局方法
    Vue.use(GlobalFilterRegister, opts);    // 全局过滤器

    components.map(component => Vue.component(component.name, component))
};

if (typeof window !== 'undefined' && window.Vue) {
    install(window.Vue);
}

export default {
    install,
    utils,
    routeUrl,
    restUrl,

    // atom or container
    BoolBox,
    CheckBox,
    CodeBox,
    DataList,
    DateBox,
    DateTimeBox,
    DialogBox,
    DropDownBox,
    FileBox,
    FindBox,
    FormView,
    RowGrid,
    IconBox,
    ImgBox,
    JsonBox,
    List,
    ListItem,
    MiniFormBox,
    MiniFormObject,
    MiniFormField,
    MetaEasyEdit,
    NumBox,
    PassBox,
    PopMenu,
    RadioBox,
    SearchPanel,
    SqlBox,
    Tags,
    TableList,
    Tree,
    TableTreeList,
    TextAreaBox,
    TextBox,
    TimeBox,
    ZTogglePanel,

    // template
    DataListTableTmpl,
    FormTmpl,
    MasterSlaveTableTmpl,
    SingleGridTmpl,
    TableFormTmpl,
    TreeFormTmpl,
    TreeTableTmpl,

    // meta
    MetaDataManager,
    FormBuilder,
    GlobalConfList,
    GlobalConf,
    InstanceConfList,
    InstanceConfEdit,
    InstanceConfNew
}

export {
    utils,
    routeUrl,
    restUrl,

    // atom or container
    BoolBox,
    CheckBox,
    CodeBox,
    DataList,
    DateBox,
    DateTimeBox,
    DialogBox,
    DropDownBox,
    FileBox,
    FindBox,
    FormView,
    RowGrid,
    IconBox,
    ImgBox,
    JsonBox,
    List,
    ListItem,
    MiniFormBox,
    MiniFormObject,
    MiniFormField,
    MetaEasyEdit,
    NumBox,
    PassBox,
    PopMenu,
    RadioBox,
    SearchPanel,
    SqlBox,
    Tags,
    TableList,
    Tree,
    TableTreeList,
    TextAreaBox,
    TextBox,
    TimeBox,
    ZTogglePanel,

    // template
    DataListTableTmpl,
    FormTmpl,
    MasterSlaveTableTmpl,
    SingleGridTmpl,
    TableFormTmpl,
    TreeFormTmpl,
    TreeTableTmpl,

    // meta
    MetaDataManager,
    FormBuilder,
    GlobalConfList,
    GlobalConf,
    InstanceConfList,
    InstanceConfEdit,
    InstanceConfNew
}
