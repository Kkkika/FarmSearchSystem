import 'vue-router';

//使用 declare module 扩展 vue-router 模块
declare module 'vue-router' {
  //扩展 RouteMeta 接口
  interface RouteMeta {
    //定义你的 meta 字段，并指定类型
    //'?' 表示这些字段是可选的
    requiresAuth?: boolean;
    roles?: string[]; //roles 字段是一个字符串数组
    title?: string;   //你也可以添加其他自定义字段，比如页面标题
  }
}
