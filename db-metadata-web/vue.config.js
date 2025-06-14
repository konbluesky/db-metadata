let alias = require('./alias.config');
let path = require('path')

const name = "db-metadata-web";
module.exports = {
    publicPath: "/",
    outputDir: 'dist',
    assetsDir: 'static',    // 静态资源存放目录,相对于outputDir
    indexPath: 'index.html', // 生成的index.html的输出路径(相对于outputDir), 也可以是绝对路径
    runtimeCompiler: true, // 运行时编译, 支持运行时动态template, dialog.js等部分内容需要此项目支持
    // 禁用生产环境的ESLint检查
    lintOnSave: false,
    // crossorigin: "",  // 设置生成的html中 link和script标签的 crossorigin属性, 仅影响由html-webpack-plugin在构建时注入的标签,直接写在模板中的标签不受影响
    // configureWebpack: config => {
    // },
    configureWebpack: {
        // provide the app's title in webpack's name field, so that
        // it can be accessed in index.html to inject the correct title.
        name: name,
        resolve: {
            ...alias.resolve,  // 提取成独立的alias.config, 配置idea识别`@`根目录
            fallback: {
                "path": require.resolve("path-browserify"),
                "os": require.resolve("os-browserify/browser"),
                "crypto": require.resolve("crypto-browserify"),
                "stream": require.resolve("stream-browserify"),
                "buffer": require.resolve("buffer")
            }
        }
    },
    devServer: {
        host: '0.0.0.0',
        port: 8080,
        https: false,
        hot: true,
        client: {
            overlay: {
                errors: true,
                warnings: false
            }
        },
        // proxy: 'http://192.168.110.67:8888'
        proxy: {
            '^/meta/': {
                target: 'http://localhost:8888',
                // target: 'http://192.168.110.67:8888',
                pathRewrite: {'^/meta': ''},
                changeOrigin: true
            },   // 超过11个node会提示MaxListenersExceededWarning
            '^/file/': {
                target: 'http://localhost:8888',
                // target: 'http://192.168.110.67:8888',
                pathRewrite: {'^/file': '/file'},
                changeOrigin: true
            }
        }
    },
    // 扩展 webpack 配置，使 packages 加入编译
    chainWebpack: config => {
        // 生产环境禁用ESLint
        if (process.env.NODE_ENV === 'production') {
            config.module.rules.delete('eslint')
        }
        
        config.module.rule('js')
            .include.add('/package').end()
            .use('babel').loader('babel-loader').tap(options => {
            return options;
        })

        // set svg-sprite-loader
        config.module
            .rule('svg')
            .exclude.add(path.join(__dirname, 'src/svg')).add(path.join(__dirname, 'package/svg/src'))
            .end()
        config.module
            .rule('icons')
            .test(/\.svg$/)
            .include.add(path.join(__dirname, 'src/svg')).add(path.join(__dirname, 'package/svg/src'))
            .end()
            .use('svg-sprite-loader')
            .loader('svg-sprite-loader')
            .options({
                symbolId: 'me-icon-[name]'
            })
            .end()
    }
};
