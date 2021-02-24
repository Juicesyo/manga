@file:Suppress("unused")

package Juicesyo.github.manga

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.content

object MyPluginMain : KotlinPlugin(
    @OptIn(ConsoleExperimentalApi::class)
    JvmPluginDescription.loadFromResource()
) {
    override fun onEnable() {
        this.globalEventChannel().subscribeAlways<GroupMessageEvent> {
            if (MySetting.status == "是" && this.message.content.startsWith(MySetting.prefix)) {
                //manga 名剑冢 2 5
                var msg = this.message.content.replace(MySetting.prefix, "")
                var MangaName = String()
                var Chapter = String()
                var page = String()
                var PluginError = 0
                try {
                    MangaName = msg.split(" ")[1]
                    Chapter = msg.split(" ")[2]
                    page = msg.split(" ")[3]
                } catch (e: Exception) {
                    this.group.sendMessage("参数错误。")
                    PluginError == 1
                }
                if (PluginError != 1) {
                    if (MySetting.pattern == "source" || MySetting.pattern == "Source") {
                        this.group.sendMessage(
                            MangaMain.MangaImage(
                                MangaName,
                                Integer.parseInt(Chapter),
                                Integer.parseInt(page)
                            )
                        )
                    } else {
                        this.group.sendImage(
                            MangaMain.MangaImageInputStream(
                                MangaName,
                                Integer.parseInt(Chapter),
                                Integer.parseInt(page)
                            ), ".jpg"
                        )
                        MangaMain.inputStream.close()
                    }
                }
            }
            MySetting.reload() // 从数据库自动读取配置实例
            //MyPluginData.reload()

            //logger.info("${MangaMain.MangaId("风起苍岚",)}")
        }
    }
}

    // 定义插件数据
// 插件
/*
object MyPluginData : AutoSavePluginData("MangaData") { // "name" 是保存的文件名 (不带后缀)
    var list: MutableList<String> by value(mutableListOf("a", "b")) // mutableListOf("a", "b") 是初始值, 可以省略
    var long: Long by value(0L) // 允许 var
    var int by value(0) // 可以使用类型推断, 但更推荐使用 `var long: Long by value(0)` 这种定义方式.


    // 带默认值的非空 map.
    // notnullMap[1] 的返回值总是非 null 的 MutableMap<Int, String>
    var notnullMap
            by value<MutableMap<Int, MutableMap<Int, String>>>().withEmptyDefault()

    // 可将 MutableMap<Long, Long> 映射到 MutableMap<Bot, Long>.
    val botToLongMap: MutableMap<Bot, Long> by value<MutableMap<Long, Long>>().mapKeys(Bot::getInstance, Bot::id)
}
*/
    object MySetting : ReadOnlyPluginConfig("MySetting") { // "MySetting" 是保存的文件名 (不带后缀)
        @ValueDescription("是否启用插件")
        val status by value("是")

        @ValueDescription("命令前缀")
        val prefix by value("manga")

        @ValueDescription("模式(source：发送图片原链接)")
        val pattern by value("normal")
    }