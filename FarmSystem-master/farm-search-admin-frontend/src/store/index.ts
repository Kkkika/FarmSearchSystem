import { createStore } from 'vuex'

//声明对象
declare let SessionStorage :any;
const USER = 'USER';//声明key

const store = createStore({
  state: {
    //创建用户对象
    user:SessionStorage.get(USER) || {}
  },
  getters: {

    //判断用户登陆状态
    isLoggedIn(state){
      //返回布尔值
      return !!(state.user && state.user.accessToken);
    },
    //返回用户身份
    isAdmin(state){
      return state.user.userType;
    }
  },
  mutations: {
    //提供外部调用方法存储用户信息
    setUser(state,user){
      state.user = user;
      SessionStorage.set(USER,user);
    },
  },
  actions: {
  },
  modules: {
  }
}) //对外暴露
export default store;
