# mirai-manga
漫画爬虫
# 说明
* 命令格式：`前缀 漫画名 章节 页数`
* 支持：  
- [x] 私聊
- [ ] 群聊
- [x] 单张图片
- [ ] 下一页
- [ ] 自动翻页
- [ ] 延迟翻页
* 配置：  
目录 `\config\Juicesyo.github.manga\MySetting.yml`  
    # 是否启用插件  status: 是
    # 命令前缀  prefix: manga
    # 模式(source：发送图片原链接)  pattern: normal
# 试运行
1.通过`<$ git clone https://github.com/Juicesyo/mirai-manga.git>`等方式下载此项目到本地  
2.在**MangaMain.java**中*main*函数去掉注释后直接运行，其他调试也可在此进行
# 注意事项
window QQ**webp格式**不支持
