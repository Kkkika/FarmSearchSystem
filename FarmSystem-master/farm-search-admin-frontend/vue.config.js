const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  // 保持你原有的配置
  transpileDependencies: true,

  // Webpack 的相关配置
  configureWebpack: {
    resolve: {
      fallback: {
        "crypto": require.resolve("crypto-browserify"),
        "stream": require.resolve("stream-browserify"),
        "http": require.resolve("stream-http"),
        "https-": require.resolve("https-browserify"),
        "url": require.resolve("url/"),
        "zlib": require.resolve("browserify-zlib"),
        "util": require.resolve("util/"),
        "assert": require.resolve("assert/")
      }
    }
  },

  // 开发服务器 (devServer) 的相关配置
  // 注意：在 Vue CLI 中，这个字段叫做 'devServer' 而不是 'server'
  devServer: {
    // 你的前端开发服务器的端口号
    port: 8080,

    // 代理配置，用于解决跨域问题
    proxy: {
      // 当你的请求路径以 /api 开头时，会触发此代理
      '/api': {
        // 将请求转发到的目标后端服务器地址
        target: 'http://localhost:8123', // <--- 我假设这是你最终想要的后端地址

        // 允许跨域
        changeOrigin: true,

        // 路径重写 (Path Rewrite)
        // 这一步非常关键，它会把请求路径中的 /api 前缀去掉，再发给后端
        // 例如：前端请求 /api/admin/list -> 代理后发给后端的请求是 /admin/list
        // 如果你的后端接口本身就带 /api，则需要去掉这一行
        pathRewrite: {
          '^/api': '' // 将 ^/api 替换为空字符串
        }
      }
      // 如果你还有其他代理规则，可以继续在这里添加
      // '/another-api': { ... }
    }
  }
});
