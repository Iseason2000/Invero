package cc.trixey.invero.common.api

import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.platform.util.bukkitPlugin

/**
 * Invero
 * cc.trixey.invero.common.api.InveroSettings
 *
 * @author Arasple
 * @since 2023/1/19 11:51
 */
object InveroSettings {

    @Config
    lateinit var conf: Configuration
        private set

    /**
     * 插件根目录
     */
    val pluginFolder by lazy { bukkitPlugin.dataFolder }

    /**
     * 工作路径
     */
    @ConfigNode("Workspaces.paths")
    var workspaces = listOf<String>()
        private set

    /**
     * 数据库类型
     */
    @ConfigNode("Database.type")
    var databaseType = "SQLITE"
        private set

    /**
     * SQL 配置
     */
    @ConfigNode("Database.sql")
    var sqlSection: ConfigurationSection? = null
        private set

    /**
     * 文件名过滤
     */
    @ConfigNode("Workspaces.filter")
    var fileFilter = "^(?![#!]).*\\.(?i)(conf|hocon|yaml|yml|json)\$"
        private set

}