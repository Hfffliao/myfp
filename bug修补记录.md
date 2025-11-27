1.目录穿越，在客户端获取文件的时候，用这个漏洞可以获取用户可获取的权限以外的文件；

详情可以看[Java代码审计之目录穿越(任意文件下载/读取) - chm0d的安全避风港 - 博客园](https://www.cnblogs.com/chm0d/p/17664249.html)

使用bug示例：

客户端访问这，便可以获取linux操作系统的passwd文件（重要的用户信息文件）

https://linyi.love/file?filepn=../../../../etc/passwd

修复方法主要是用java的path类的normalize()方法规范文件路径，把../直接转化为上级路径，之后检测路径是否在目标目录，否的话返回badrequest
