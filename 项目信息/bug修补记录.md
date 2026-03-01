# 1.目录穿越，在客户端获取文件的时候，用这个漏洞可以获取用户可获取的权限以外的文件；

详情可以看[Java代码审计之目录穿越(任意文件下载/读取) - chm0d的安全避风港 - 博客园](https://www.cnblogs.com/chm0d/p/17664249.html)

使用bug示例：

客户端访问这，便可以获取linux操作系统的passwd文件（重要的用户信息文件）

https://linyi.love/file?filepn=../../../../etc/passwd

修复方法主要是用java的path类的normalize()方法规范文件路径，把../直接转化为上级路径，之后检测路径是否在目标目录，否的话返回badrequest

# 2.maven的pom文件没加`<transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>` 导致的spi机制找不到对应jar包的提供的服务

详细解决过程见`liaoyis_online\遇到的问题\25.12.03_maven打包jar文件.md` 

或者网址[idea中使用debug远程调试案例（从问题出现到定位到问题）详细过程-CSDN博客](https://blog.csdn.net/fjdvgf/article/details/155535777?spm=1001.2014.3001.5501)

# 3.在tomcat上能运行的multipart文件上传并保存功能，在使用嵌入式jetty容器后，上文件上传后被保存到文件系统后会被自动删除

这是springmvc框架和jetty协同运行后出现的；

**1.具体表现**为在自己代码里用`file.transferTo(destFile)`保存文件到文件系统后（补充：1.file为jetty实现的MultipartFile接口的类的对象；2.destFile为File对象，内含文件路径），文件系统的该文件会在**请求处理过程中**的出dispatcherServlet.dodispatch()方法后调用的cleanMultipart()方法的时候清除；

**2.解决方法**是用底层文件写入方法替代file.transferTo(destFile)方法

**3.解决方法**：出现现象之后debug到造成问题的原因
