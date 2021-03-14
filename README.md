# mirai-manga
漫画爬虫
# 说明
* 命令格式：  
`前缀 漫画名 章节 页数`  
`前缀 id 漫画名`
* 支持：  
- [x] 私聊
- [ ] ~~群聊~~
- [x] 单张图片
- [ ] ~~下一页~~
- [X] 自动翻页
- [ ] ~~延迟翻页~~
* 配置：  
目录 `\config\Juicesyo.github.manga\MySetting.yml`  
    是否启用插件  *status:* `是`  
    命令前缀  *prefix:* `manga `   
	启用id获取功能(true：默认)  *id:* `true`  
    模式(normal:默认 source：发送图片原链接 auto:自动翻页)  *pattern:* `normal`  
# 试运行
1.通过`<$ git clone https://github.com/Juicesyo/mirai-manga.git>`等方式克隆此仓库至本地  
2.在**MangaMain.java**中*main*函数去掉注释后直接运行，其他调试也可在此进行
# 注意事项
Windows QQ**webp格式**不支持  
# 联系方式
交流群：581103186