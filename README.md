## 基于 Spring Security的JWT通用后台安全框架
前言：每次使用SS框架时，总要重新书写JWT部分，因此我将其抽取出来了

## 使用
使用gradle将其打包供其他项目使用
./gradlew build

打成的包会在 build/libs/下，将其引入其他项目即可使用

可选择实现
UserDetailsService（登录时用户从哪获取）, RegisterOperation（注册或修改用户账号密码） 接口
默认使用内存保存用户

默认登录
http://localhost:80/login?username=xxx&password=xxx

默认注册
http://localhost:80/register?username=xxx&password=xxx


