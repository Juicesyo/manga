@file:Suppress("unused")

package Juicesyo.github.manga

import kotlinx.coroutines.delay
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content

object MyPluginMain : KotlinPlugin(
    @OptIn(ConsoleExperimentalApi::class)
    JvmPluginDescription.loadFromResource()
) {
    override fun onEnable() {
        MySetting.reload()
        MyPluginData.reload()
        this.globalEventChannel().subscribeAlways<FriendMessageEvent> {
            if (MySetting.status == "是" && this.message.content.startsWith(MySetting.prefix)) {
                //MyPluginData.reload()
                //manga 名剑冢 2 5
/*
                    //save data initialization
                var SaveIndex = -1 //-1默认新增
                for ((index, SenderDataLOC) in MyPluginData.WhiteList.withIndex()) {
                    if (sender.toString() == SenderDataLOC.split("|")[0]) {
                        SaveIndex = index
                    }
                }

 */

                var msg = this.message.content.replace(MySetting.prefix, "")
                var MangaName = String()
                var Chapter = String()
                var Page = String()
                /*
            if(msg=="next" || msg=="下一页" || msg=="下页")
            {

                if (SaveIndex!=-1) {
                    var MangaDataLOC = MyPluginData.WhiteList.get(SaveIndex).split("|");
                    var MangaName=MangaDataLOC[1]
                    var Chapter = MangaDataLOC[2]
                    var Page = MangaDataLOC[3]
                    var Pages=Integer.parseInt(Page)+1
                    var Chapters=Integer.parseInt(Chapter)+1
                    SendMangaMethods(MangaName,Chapter,Pages.toString(),sender)
                    if (MangaMain.ChapterRuleError==1){
                        MyPluginData.WhiteList[SaveIndex] = "$sender|$MangaName|$Chapters|1"
                    }else {
                        MyPluginData.WhiteList[SaveIndex] = "$sender|$MangaName|$Chapter|$Pages"
                    }
                }else{
                    this.friend.sendMessage("未找到历史阅读记录。")
                }
               }
                */
                if (msg.startsWith("id") || msg.startsWith(" id")){
                    msg = msg.replace("id","")
                    MangaMain.MangaId(msg)
                    sender.sendMessage(MangaMain.MangaId_Json)
                }else {
                    var PluginError = 0
                    try {
                        MangaName = msg.split(" ")[1]
                        Chapter = msg.split(" ")[2]
                        Page = msg.split(" ")[3]
                    } catch (e: Exception) {
                        this.friend.sendMessage("参数错误。")
                        PluginError == 1
                    }
                    if (PluginError != 1) {
                        SendMangaMethods(MangaName,Chapter,Page,sender)
                        /*
                        //save data.
                        var SenderData = "$MangaName|$Chapter|$Page"
                        if (SaveIndex == -1) {
                            MyPluginData.WhiteList.add(SenderData)
                        } else {
                            MyPluginData.WhiteList[SaveIndex] = "$sender|$SenderData"
                        }
                         */
                    }
                }
            }
            //MySetting.reload() // 从数据库自动读取配置实例
            //MyPluginData.reload()

            //logger.info("${MangaMain.MangaId("")}")
        }
    }
}

    // 定义插件数据
// 插件

object MyPluginData : AutoSavePluginData("MangaData") { // "name" 是保存的文件名 (不带后缀)
    var WhiteList: MutableList<String> by value(mutableListOf()) // mutableListOf("a", "b") 是初始值, 可以省略
}

object MySetting : ReadOnlyPluginConfig("MySetting") { // "MySetting" 是保存的文件名 (不带后缀)
    @ValueDescription("是否启用插件")
    val status by value("是")

    @ValueDescription("命令前缀")
    val prefix by value("manga")

    @ValueDescription("模式(normal：默认 source：发送图片原链接 auto：自动翻页)")
    val pattern by value("normal")

    @ValueDescription("启用id获取功能(true：默认)")
    val id by value("true")
}
//为便于调用而创建的类。根据配置发送指定类型消息，已转换参数类型。
suspend fun SendMangaMethods(MangaName:String, Chapter:String, Page:String, sender:Friend) {
    var iChapter = Integer.parseInt(Chapter)
    var iPage = Integer.parseInt(Page)

    if (MySetting.pattern == "source" || MySetting.pattern == "Source") {
        sender.sendMessage(
            MangaMain.MangaImage(
                MangaName,
                iChapter,
                iPage
            )
        )
    }
    if (MySetting.pattern == "normal" || MySetting.pattern == "Normal") {
        sender.sendImage(
            Request.inputStream(
                MangaMain.MangaImage(
                    MangaName,
                    iChapter,
                    iPage
                )
            )
        )
        Request.inputStream.close()
    }
    if (MySetting.pattern == "auto" || MySetting.pattern == "Auto") {
        ImageUpload.sender = sender
        ImageUpload.MangaName = MangaName
        ImageUpload.Chapter = iChapter
        ImageUpload.Page = iPage
        ImageUpload.main(null)
        var i = 0
        //var ImageId : String
        sender.sendMessage("正在缓存图片，请稍后。")
        delay(3000)
        while (ImageUpload.ImageIdS.size != 0){
            if (i==12){break}

            if (ImageUpload.ImageIdS.size < i+1){
                delay(3000)
            }else{
                sender.sendMessage(Image(ImageUpload.ImageIdS[i]))
                //System.out.println(ImageUpload.ImageIdS[i])
                i++
            }
        }
        ImageUpload.ImageIdS.clear()
        sender.sendMessage("Last\nMangaName:${MangaMain.MangaName}\nMangaId:${MangaMain.MangaId(MangaName)}\nChapter:${ImageUpload.Chapter}\nPage:${ImageUpload.Page}")
        /*
        delay(5000)
        while (ImageIo.ImageIdS.size < 12) {
            sender.sendMessage("已缓存（${ImageIo.ImageIdS.size}/12）")
            delay(3000)
        }
        for (ImageId in ImageIo.ImageIdS) {
            sender.sendMessage(Image(ImageId))
        }
         */
        Request.inputStream.close()
    }
}