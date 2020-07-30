import {elementVersion} from "../../config";

export const ConfDesc = `
    |配置项|说明|类型|可选值|默认值|
    |-----|---|----|----|-----|
    |component_name|渲染组件名|string|组件列表|-|
    |name|name属性|string|-|-|
    |label|该实例标签名,表现为表单域的label|string|-|-|
    |seats|文件含义组成的字符数组, 一个坑一个上传按钮, 对应一个文件。例如:['营业执照','身份证正面','身份证反面']， 需要特别注意, 配置此字段后conf.limit无效, 每个上传按钮只能输入一个图片|array of string|-|[]|
    |conf|ElementUI(` + elementVersion + `)中el-upload的原生配置项|object|-|-|
`;

export default {
    "component_name": "FileBox",
    "name": "FileBox",
    "label": "文件上传框",
    "inline": false,
    "seats": [''],
    "conf": {
        "action": "/file/upload?objectCode={objectCode}&fieldCode={fieldCode}",
        "drag": false,
        "tip": "上传文件限制不超过2M",
        "auto-upload": true,
        "show-file-list": true,
        "limit": 1,
        "multiple": false,  // 暂时单选
    }
}
