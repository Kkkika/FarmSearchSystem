// 初始化axios
import {message} from "ant-design-vue";
import store from "@/store";
import {Tool} from "@/utils/tool";
import router from "@/router";
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';

const api = axios.create({
  baseURL: "http://localhost:8123/api", // 使用环境变量设置基础URL
  timeout: 10000, // 设置请求超时时间为10秒
});

// 添加请求拦截器
/*
* 使用axios拦截器打印请求和响应的数据
* */

api.interceptors.request.use((config) => {
    console.log('请求参数：', config);
    //从store获取token
    const userToken = store.state.user.accessToken;
    console.log("获取用户token："+ userToken);

    if (userToken) {
      // 使用标准 Authorization 头
      config.headers['Authorization'] = `Bearer ${userToken}`;
      // config.headers.token = `${userToken}`;
      console.log("请求headers增加token:" + userToken);
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  });

// 添加响应拦截器处理错误
let isRefreshing = false;//标记是否正在刷新token
let requests:((token:string) => void)[] = [];//存储因token过期而挂起的请求

api.interceptors.response.use(response => {
  console.log('响应结果：',response);
  return response.data;
},
  async (error) => {
    //超出2xx范围的状态码都会触发该函数
    if (error.response) {
      const { status } = error.response;
      const originalRequest = error.config;

      // 401 Unauthorized 错误表示 Access Token 过期
      if (status === 401) {

        // 如果当前不是正在刷新 token 的状态，才开始刷新
        if (!isRefreshing) {
          isRefreshing = true;

          try {
            //从store获取RefreshToken
            const refreshToken = store.state.user?.refreshToken;
            if (!refreshToken) {
              throw new Error("无有效刷新令牌");
            }
            //调用刷新令牌的接口
            console.log('AccessToken过期，正在尝试刷新...');
            const refreshResponse = await axios.post('/api/admin/refreshToken', { refreshToken });

            //假设后端返回的数据结构是 { success: true, data: { accessToken: '...', refreshToken: '...' } }
            const newTokens = refreshResponse.data.data;
            console.log("刷新令牌返回："+newTokens);

            //刷新成功，更新 store 和 localStorage
            store.commit('updateTokens', newTokens);
            console.log('令牌刷新成功！');

            //重新执行所有被挂起的请求
            requests.forEach(cb => cb(newTokens.accessToken));
            requests = []; //清空队列

            //重新执行本次失败的请求
            originalRequest.headers['Authorization'] = `Bearer ${newTokens.accessToken}`;
            return api(originalRequest);

          } catch (refreshError) {
            //刷新失败，清除登录状态并跳转到登录页
            console.error('刷新令牌失败:', refreshError);
            store.commit('setUser', {}); //清除用户信息
            router.push('/LoginView');
            message.error('登录已过期，请重新登录');
            return Promise.reject(refreshError);
          } finally {
            isRefreshing = false;
          }
        } else {
          //如果当前正在刷新中，则将这个失败的请求挂起
          return new Promise((resolve) => {
            requests.push((token) => {
              originalRequest.headers['Authorization'] = `Bearer ${token}`;
              resolve(api(originalRequest));
            });
          });
        }
      }
    }
    //其他错误，直接抛出
    return Promise.reject(error);

  }
  );




// 导出API实例
export default api;
