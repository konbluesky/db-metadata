#!/bin/bash

echo "🔧 修复前端依赖和构建问题..."

# 清理旧的依赖和缓存
echo "🧹 清理旧依赖..."
rm -rf node_modules
rm -f package-lock.json
rm -f yarn.lock

# 清理npm和yarn缓存
echo "🗑️  清理缓存..."
npm cache clean --force
if command -v yarn &> /dev/null; then
    yarn cache clean
fi

# 清理Vue CLI缓存
if [ -d ~/.vue ]; then
    echo "🗑️  清理Vue CLI缓存..."
    rm -rf ~/.vue
fi

# 设置npm镜像源（可选，加速下载）
echo "🔧 设置npm镜像源..."
npm config set registry https://registry.npmmirror.com/

# 重新安装依赖
echo "📦 重新安装依赖..."
npm install

echo "✅ 依赖修复完成！"
echo ""
echo "📝 修复内容："
echo "   ✓ 更新ESLint配置使用@babel/eslint-parser"
echo "   ✓ 添加webpack polyfill配置"
echo "   ✓ 安装所需的polyfill依赖"
echo ""
echo "🚀 可以运行以下命令："
echo "   npm run serve   # 启动开发服务器"
echo "   npm run build   # 构建生产版本"
echo "   npm run lint    # 检查代码规范" 