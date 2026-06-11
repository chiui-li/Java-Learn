# Java 基础面试问题汇总

## 1. 原型设计和与后端联调是怎么开展的？

### 原型设计阶段：
- **需求分析**：与客户沟通，明确产品功能、业务逻辑和用户需求
- **线框图（Wireframe）**：使用 Axure、Sketch、Figma 等工具绘制低保真线框图
- **高保真原型**：在用户反馈确认后，制作接近最终效果的高保真设计稿
- **交互说明文档**：详细标注交互动效、状态流转逻辑

### 与后端联调阶段：
1. **接口约定先行（API First）**  
   - 使用 Swagger/YApi/Apigateway 等工具定义好接口契约 (Swagger) before 实现前后端分离项目，前端可提前开发 Mock data 模拟服务层调用测试验证功能完成度。

2. **字段规范统一**：
   ```javascript
   // ✅ 推荐做法（驼峰命名）
   { id: 1, name: "张三" }
   
   ❌避免蛇形下划线+特殊字符组合 (api_id)
     - JSON.parse() 自动转义问题处理复杂嵌套对象转换性能损耗较大需要额外配置全局转换器。

3. **联调流程**：
   ```bash
   # 后端先行部署测试环境地址 + Swagger/Postman接口文档同步更新状态字段变化及时通知前端调整UI适配错误码统一 (401, 502) 
     - WebSocket实时推送消息即时展示 loading 动画避免长时间空转超时时间约定一致。

## 2. 浏览器的兼容和多端适配怎么处理？

### CSS/JS 兼容性处理：
```javascript
// ✅ Polyfill + PostCSS/Autoprefixer自动注入脚本兼容旧版浏览器+ES6+新特性降级方案提供回退样式使用媒体查询 (@media) 或 flex/gridfallback写法保证基础体验。
const div = document.querySelector('.container'); // ES6 选择器

if (!window.CSS.supports('display', 'flex')) { ❌❌不支持则用float替代布局方式适配IE10+旧版浏览器降级方案提供回退机制确保页面可用性核心逻辑优先保障功能完整性视觉样式做渐进增强处理。
```

### PC/移动多端适配：
- **REM/RP**单位（响应式）配合 `viewport`meta 标签实现流动布局设计
- **媒体查询 (Media Queries)**: 
  ```css
  /* 大屏 >1200px */ @media(min-width:1920px){ .sidebar{display:none;} }  
    /*平板768~1024区间*/   @media(max-width:1023px && min -width :768 px ){.container {padding 16px}}    
      移动端<768调整字体字号按钮尺寸优化触摸区域 ≥44px满足 iOS/Android操作规范。
```

### 适配方案对比：
| 策略               | 优点                     | 缺点                      | 推荐场景                 |
|--------------------|--------------------------|--:|-------------||
| Fluid Layout (fluid%)      │ 自适应能力强            ├─复杂嵌套布局需手动维护断点阈值   | PC/平板多端混合项目
Rem + Media Query    │ 精确控制栅格间距        ├──部分CSS属性兼容性仍需Polyfill修复│移动端优先开发流程  
Fixed Width         ┃简单快速但缺乏灵活性     ╳无法应对不同屏幕尺寸            │内部管理系统后台操作类H5页面

##3.浏览器保存数据的方法有哪些？异同点？

### 本地存储方式对比：
|方法        |容量   |过期时间          |同步/跨域特性    |使用场景                  |  
----- ---- -------- ------ --------------------------- ------- ----------- ----- --- -------------
LocalStorage │10MB     │永久 (直到删除)      ❌不支持主动清除机制│简单配置数据表单缓存信息展示历史记录等无需实时验证的离线可用静态资源预加载。     
IndexDB   ┃5GB+      │手动控制              ✓支持事务API操作复杂结构化数据存储需求如用户行为日志文件记录功能模块间独立隔离状态保持场景  
Cookie     ├─4KB(单个)  ⏱️29天默认失效时间可配置 Session ID CSRF Token敏感信息加密参数传递 HTTPHeader自动携带服务端校验权限级别判断逻辑。     
SessionStorage ┃5MB        ┌┤仅同域不同标签页读写隔离    ✓独立上下文状态管理功能切换时动态更新表单数据等需要临时存储但不宜持久化场景   
WebSQL(已弃用)│2GB       │手动清理              ⚠️跨浏览器支持不足推荐迁移至IndexDB方案替代原有数据库需求遗留系统维护成本较高兼容性差等问题。

### 各方法异同点总结：
- **共同特性**都提供异步API接口封装 Promise/回调函数形式调用存储结果自动持久化处理流程无需额外配置即可直接操作数据读写增删查改功能基本一致底层实现由浏览器内核决定性能表现略有差异  
- **核心区别**: LocalStorage适合轻量级数据本地缓存需求，Cookie 需要服务端配合传递状态值验证逻辑等场景；SessionStorage类似LocalStorage但限制在当前标签页内使用不共享到其它窗口实例中。

##4.React数据通信的方法？

### Context API + Provider模式:
```jsx 
// ✅创建全局上下文对象类型声明定义属性约束规则检查  
const ThemeContext = React.createContext('light'); 

function App() {      
  return (       
    <ThemeContext.Provider value="dark">          
      <Child />        
    </ThemeContext.Provider>  
  );  
}

function Child() {
  const theme = useContext(ThemeContext); // ❌无法访问父级数据传递状态向上穿透问题需要解决。
  return <h1>{theme}</h1>;
}
```

### Redux中间件模式: 
```javascript 
// ✅Redux-Toolkit简化创建Store+Reducer配置流程开发效率提升明显  
const store = configureStore({              
  reducer: {            
    users: userSlice.reducer,        
    products: productSlice.reducer      
  }    
});  

export default store;
```

### Hooks + useReducer替代方案： 
```javascript
function Counter() {       
  const [count, inc] = useReducer((state, action) => ({         count: state.count + (action === 'inc' ? 10 : 0), }),       [{}, false]);   
     
 return (      
   <button onClick={inc} >         
     Count:{count}        
 </button>    
 );
```

### API通信层设计： 
- **Axios拦截器统一处理请求头注入 +响应数据处理异常捕获逻辑**:  
  ```javascript 
  const api = axios.create({ baseURL: '/api' });  

    // ✅请求前添加Token凭证校验逻辑  
   
   config.authHeader = getTokenFromStorage();    
     
     //响应的数据解析失败时弹出提示框并跳转登录页面功能模块间状态同步机制避免重复渲染组件浪费资源情况发生。
    catch (error) {       
      if(error.response.status===401){         
        router.push('/login');        
       }      
     }  
   ```

### 不同通信方案适用场景： 
- **Context API**:轻量级全局状态管理如用户身份认证信息主题配置语言类型选择等基础设置需求适合中小项目快速搭建无需复杂依赖关系链构建开发成本较低。  
- **Redux**中大型应用组件层级较深父子关系不明确数据流向混乱时引入中间件机制解决异步操作订阅模式发布事件通知功能增强整体架构清晰可见性提升便于团队协作完成模块分工协作任务分配等工作流配置自动化测试覆盖率提高产品质量稳定性保障效果显著。