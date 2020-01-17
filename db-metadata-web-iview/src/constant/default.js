import {PAGE_NUM_AREA} from './constant'

/**
 * 组件的默认UI配置
 */
const DEFAULT_CONF = {
    "TableList": {
        "component_name": "TableList",
        "name": "TableList",
        "label": "表格模板",
        "data_url": "/table/list/{objectCode}", // required
        "delete_url": '/table/delete/{objectCode}?{primaryKvExp}',
        "multi_select": true, //多选
        "editable": false,
        "conf": {
            "default-sort": {"prop": "id", "order": "descending"}, // descending, ascending
            "highlight-current-row": true,
            "size": "medium", // medium, small, mini
            // "max-height": 500,
        },
        "columns": [
            // {
            //     name: 'name',
            //     label: 'label',
            //     component_name: '',
            //     editable: false,     // 表格内可编辑
            //     searchable: true,    // 搜索面板支持该字段搜索
            //     conf: {
            //         width: '',
            //         sortable: true,  // 表格中可否根据此列进行排序
            //         //...
            //     }
            // }
        ], // 字段元数据
        "pagination": { // element ui配置
            "page-size": PAGE_NUM_AREA[0],
            "page-sizes": PAGE_NUM_AREA,
            "current-page": 1,
            "layout": "total, sizes, prev, pager, next, jumper"
        },
        "operation-bar": { // 针对操作栏中所有按钮的默认设置
            "size": "mini",
            "type": "primary"
        },
        "operation-column": {
            "fixed": 'right'
        },
        "buttons": {
            "edit": {
                "label": "编辑",
                "conf": {
                    "icon": "el-icon-edit",
                    "size": "mini",
                    "type": "primary"
                }
            },
            "delete": {
                "label": "删除",
                "conf": {
                    "size": "mini",
                    "icon": "el-icon-delete",
                    "type": "danger"
                }
            }
        }
    },
    "TableTreeList": {
        "component_name": "TableTreeList",
        "name": "TableTreeList",
        "label": "树型表",
        "data_url": "/table/list/{objectCode}",
        "delete_url": "/table/delete?objectCode={objectCode}&ids={ids}",
        "children_data_url": "/table/list/{objectCode}?parentPrimary={parentPrimary}",
        "multi_select": false,
        "editable": false,
        "conf": {
            "row-key": "id",
            "tree-props": {
                "hasChildren": "hasChildren",
                "children": "children"
            },
            "default-expand-all": false,
            "expand-row-keys": [],  // 初始展开的行, 数组中的内容为row-key指定属性对应的值
            "indent": 16, // 树节点缩进
            "lazy": false,
            "default-sort": {"prop": "id", "order": "descending"}, // descending, ascending
            "highlight-current-row": true,
            "size": "medium", // medium, small, mini
        },
        "columns": [],
        "pagination": { // element ui配置
            "page-size": PAGE_NUM_AREA[0],
            "page-sizes": PAGE_NUM_AREA,
            "current-page": 1,
            "layout": "total, sizes, prev, pager, next, jumper"
        },
        "operation-bar": { // 针对操作栏中所有按钮的默认设置
            "size": "mini",
            "type": "primary"
        },
        "buttons": {
            "edit": {
                "label": "编辑",
                "conf": {
                    "icon": "el-icon-edit",
                    "size": "mini",
                    "type": "primary"
                }
            },
            "delete": {
                "label": "删除",
                "conf": {
                    "size": "mini",
                    "icon": "el-icon-delete",
                    "type": "danger"
                }
            }
        }
    },
    "BoolBox": {
        "component_name": "BoolBox",
        "name": "BoolBox",
        "label": "布尔框",
        "default_value": false,
        "inline": true,
        "conf": {}
    },
    "TextBox": {
        "component_name": "TextBox",
        "name": "TextBox",
        "label": "文本框",
        "inline": false,
        "conf": {
            "placeholder": "请输入内容..",
            "clearable": true
        }
    },
    "PassBox": {
        "component_name": "PassBox",
        "name": "PassBox",
        "label": "密码框",
        "inline": false,
        "conf": {
            "placeholder": "请输入密码..",
            "clearable": true
        }
    },
    "TextAreaBox": {
        "component_name": "TextAreaBox",
        "name": "TextAreaBox",
        "label": "文本域",
        "inline": false,
        "conf": {
            "placeholder": "请输入文本内容.."
        }
    },
    "DropDownBox": {
        "component_name": "DropDownBox",
        "name": "DropDownBox",
        "label": "下拉框",
        "inline": true,
        "group": false,
        "data_url": "/dict/yn",
        "options": [
            //     {"key":"value"}
        ],
        "conf": {
            "clearable": true,
            "multiple": false
        }
    },
    "TimeBox": {
        "component_name": "TimeBox",
        "name": "TimeBox",
        "label": "时间框",
        "inline": true,
        "conf": {
            "value-format": "HH:mm:ss",
            "clearable": true
        }
    },
    "DateBox": {
        "component_name": "DateBox",
        "name": "DateBox",
        "label": "日期框",
        "inline": true,
        "conf": {
            "value-format": "yyyy-MM-dd",
            "clearable": true
        }
    },
    "DateTimeBox": {
        "component_name": "DateTimeBox",
        "name": "DateTimeBox",
        "label": "日期时间框",
        "inline": true,
        "conf": {
            "value-format": "yyyy-MM-dd HH:mm:ss",
            "clearable": true
        }
    },
    "NumBox": {
        "component_name": "NumBox",
        "name": "NumBox",
        "label": "数字框",
        "inline": true,
        "default_value": 0,
        "conf": {
            "controls": true,
            "placeholder": "请输入数值.."
        }
    },
    "RadioBox": {
        "component_name": "RadioBox",
        "name": "RadioBox",
        "label": "单选框",
        "inline": true,
        "data_url": "/dict/yn",
        "options": [
            //     {"key":"value"}
        ],
        "conf": {}
    },
    "CheckBox": {
        "component_name": "CheckBox",
        "name": "CheckBox",
        "label": "多选框",
        "inline": true,
        "data_url": "/dict/yn",
        "options": [
            // {"key":"value"}
        ],
        "conf": {}
    },
    "JsonBox": {
        "component_name": "JsonBox",
        "name": "JsonBox",
        "label": "Json框",
        "inline": false,
        "default_value": {},
        "conf": {
            "mode": "code",
            "modes": [
                "code",
                "tree",
                "text",
                "view",
                "form"
            ]
        }
    },
    "ImgBox": {
        "component_name": "ImgBox",
        "name": "ImgBox",
        "label": "图片上传框",
        "inline": false,
        "conf": {
            "action": "/file/upload?objectCode={objectCode}&fieldCode={fieldCode}",
            "drag": false,
            "list-type": "picture-card",
            "auto-upload": true,
            "limit": 1,
            "multiple": false,  // 暂时单选
        }
    },
    "FileBox": {
        "component_name": "FileBox",
        "name": "FileBox",
        "label": "文件上传框",
        "inline": false,
        "conf": {
            "action": "/file/upload?objectCode={objectCode}&fieldCode={fieldCode}",
            "tip": "上传文件限制不超过2M",
            "auto-upload": true,
            "show-file-list": true,
            "limit": 1,
            "multiple": false,  // 暂时单选
        }
    },
    "MiniFormBox": {
        "component_name": "MiniFormBox",
        "name": "MiniFormBox",
        "label": "迷你表单框",
        "inline": false,
        "conf": {
            "label-width": "80px",
            "size": "mini",
            "disabled": false,
            "inline": false
        }
    },
    "DialogBox": {
        "component_name": "DialogBox",
        "name": "DialogBox",
        "label": "弹出框",
        "conf": {
            "title": "标题",
            "fullscreen": false,
            "width": "50%",
            "modal": true,
            "lock-scroll": true,
            "show-close": true,
            "center": false,
            "destroy-on-close": true
        }
    },
    "SearchPanel": {
        "component_name": "SearchPanel",
        "name": "SearchPanel",
        "label": "搜索面板",
        "expand": false,
        "label-position": "top-center",
        "conf": {
            "label-width": '80px',
            "size": 'mini', // medium|small|mini
        },
        "columns": []
    },
    "FormView": {
        "component_name": "FormView",
        "name": "FormView",
        "label": "表单模板",
        // "action": "/form/doAdd/{objectCode}", // form action (url), pxg_todo 屏蔽, FormBuilder实例配置时,会导致此属性和值配入库中, 但是此值需要区分三种表单环境
        "conf": {
            "label-width": '100px',
            "size": 'medium', // medium|small|mini
            "disabled": false,
            "inline": false,
            "rules": {
                // eg:
                // "id": [{required: true, message: "必填字段", trigger: "blur"}],
                // ...
            },
            // ...
        },
        "columns": [],
        // eg:
        // {
        //     name: 'id',
        //     label: 'ID',
        //     component_name: 'TextBox',
        //
        //     inline: false,
        //     showable: true,  // 表单中是否隐藏此域
        //     default_value: 1,// 默认值
        //
        //     conf: {
        //         clearable: true,
        //         placeholder: "请输入..",
        //         readonly: true,  // 只读
        //         disabled: false, // 禁用
        //         required: false, // 是否必填
        //         // ...
        //     }
        // }
        "buttons": {
            "submit": {
                "label": "提交",
                "conf": {
                    // ... support conf of el-button
                    "type": "primary"
                }
            },
            "cancel": {
                "label": '取消',
                "conf": {
                    // ... support conf of el-button
                }
            }
        }
    },
    "FindBox": {
        "component_name": "FindBox",
        "name": "FindBox",
        "label": "查找框",
        "data_url": "/meta/fields/{objectCode}", // 这个data_url响应的是FindPanel的meta, 也可以是TableList的meta
        "conf": {
            "clearable": true,
            "placeholder": "戳我展开搜索面板.."
        }
    },
    "IconBox": {
        "component_name": "IconBox",
        "name": "IconBox",
        "label": "图标框"
    },
    "FindPanel": {
        "component_name": "FindPanel",
        "name": "FindPanel",
        "label": "查找面板",
        "data_url": "/table/list/{objectCode}",
        "columns": [// {
            //     component_name: 'TextBox',
            //     name: 'id',
            //     label: 'ID',
            //     searchable: true,    // 搜索面板支持该字段搜索, 若为true, 则会在FindPanel的搜索面板中生成搜索控件
            //     conf: {
            //         width: '',
            //         sortable: true,  // 表格中可否根据此列进行排序
            //         //...
            //     }
            // },
            // {
            //     component_name: 'TextBox',
            //     name: 'name',
            //     label: '姓名',
            //     searchable: true,    // 搜索面板支持该字段搜索, 若为true, 则会在FindPanel的搜索面板中生成搜索控件
            //     conf: {
            //         width: '',
            //         sortable: true,  // 表格中可否根据此列进行排序
            //         //...
            //     }
            // }
        ]
    },
    "SqlBox": {
        "component_name": "SqlBox",
        "name": "SqlBox",
        "label": "SQL输入框",
        "check": true,
        "check_url": "/check/sql?sql={sql}",
        "theme": "default", // default、ambiance
        "lineNumbers": true,
        "mode": 'text/x-mysql',
        "conf": {}
    },
    "CodeBox": {
        "component_name": "CodeBox",
        "name": "CodeBox",
        "label": "代码编辑框",
        "width": "auto",
        "height": "120px",
        "show-theme-chose": true,
        "show-mode-chose": true,
        "themes": ["darcula", "material", "ambiance", "elegant", "eclipse", "idea"],
        "modes": ["text/javascript", "text/html", "text/css", "text/x-vue", "text/markdown"],
        "conf": {   // codemirror config
            "theme": "darcula",
            "mode": "text/javascript",
            "lineNumbers": true,
            "indentWithTabs": true,
            "smartIndent": true,
            "matchBrackets": true,
            "extraKeys": {'Ctrl': 'autocomplete'}, //自定义快捷键
        }
    },
    "Tree": {
        "component_name": "Tree",
        "name": "Tree",
        "label": "树型列表",
        "data_url": "/tree/list/{objectCode}",
        "conf": {
            "node-key": "id",
            "highlight-current": true,
            "default-expand-all": false,
            "check-on-click-node": false,
            "expand-on-click-node": false,
            "show-checkbox": true,
            "default-expanded-keys": [],
            "default-checked-keys": [],
            "accordion": false,
            "indent": 16,
            "icon-class": "el-icon-caret-right",
            "draggable": false,
            "props": {
                "label": "label",
                "children": "children"
            }
        },
        "operation-bar": {
            "size": "mini",
            "type": "primary"
        },
        "behavior": {
            "allowDrag": function (node) {
                return true
            },
            "allowDrop": function (draddingNode, dropNode, type) {
                return true
            },
            "filterNodeMethod": function (value, data, node) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            "renderContent": function (h, {node, data, store}) {
            }
        }
    },
    "DataList": {
        "component_name": "DataList",
        "name": "DataList",
        "label": "列表",
        "data_url": "/data/list/{objectCode}",
        "conf": {
            "label-props": {
                "label": "label"
            }
        },
        "pagination": { // element ui配置
            "page-size": PAGE_NUM_AREA[0],
            "page-sizes": PAGE_NUM_AREA,
            "current-page": 1,
            "layout": "total, prev, pager, next"
        }
    }
};
export default DEFAULT_CONF;