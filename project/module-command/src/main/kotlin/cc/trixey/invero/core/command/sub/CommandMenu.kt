package cc.trixey.invero.core.command.sub

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.util.PasteResult.Status.ERROR
import cc.trixey.invero.common.util.PasteResult.Status.SUCCESS
import cc.trixey.invero.common.util.createContent
import cc.trixey.invero.common.util.parseMappedArguments
import cc.trixey.invero.common.util.paste
import cc.trixey.invero.core.BaseMenu
import cc.trixey.invero.core.command.createHelper
import cc.trixey.invero.core.command.menu
import cc.trixey.invero.core.command.player
import cc.trixey.invero.core.command.suggestMenuIds
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.common.platform.function.submitAsync
import taboolib.module.chat.uncolored
import taboolib.platform.util.sendLang
import java.util.concurrent.TimeUnit

/**
 * Invero
 * cc.trixey.invero.core.command.sub.CommandMenu
 *
 * @author Arasple
 * @since 2023/2/18 13:03
 */
@CommandHeader(name = "menu", aliases = ["m"], permission = "invero.command.menu", description = "Manage menus")
object CommandMenu {

    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody
    val reload = subCommand {
        execute { sender, _, _ ->
            submitAsync { Invero.API.getMenuManager().reload(sender) }
        }
    }

    /*
    list [filter]
     */
    @CommandBody
    val list = subCommand {
        dynamic("filter", optional = true) {
            suggestUncheck {
                val tags = hashMapOf<String, Int>()
                Invero.API.getMenuManager().getMenus().forEach {
                    val tag = it.id!!.split("-", "_")[0].lowercase()
                    tags[tag] = tags.computeIfAbsent(tag) { 0 } + 1
                }

                tags.filterValues { it >= 2 }.keys.toList()
            }

            execute<CommandSender> { sender, ctx, _ ->
                submitAsync { sender.notifyMenus(ctx.getOrNull("filter")?.lowercase()) }
            }
        }

        execute<CommandSender> { sender, _, _ ->
            submitAsync { sender.notifyMenus() }
        }
    }

    private fun CommandSender.notifyMenus(filter: String? = null) {
        val menus = Invero.API
            .getMenuManager()
            .getMenus()
            .filter { filter == null || filter in it.id!!.lowercase() }

        if (menus.isEmpty()) return sendLang("menu-list-empty", filter ?: "NULL")

        sendLang("menu-list-header", menus.size)
        val stripTitleBy = Regex("(%|<|\\[|\\{\\{)(.+)(%|}}|]|>)")

        menus.forEach {
            it as BaseMenu

            sendLang(
                "menu-list-item",
                it.id ?: "NULL",
                it.settings.rows ?: -1,
                it.settings.title.default.replace(stripTitleBy, "").uncolored()
            )
        }
    }

    /*
    dump [id]
     */
    @CommandBody
    val dump = subCommand {
        dynamic("menu") {
            suggestMenuIds()
            execute<CommandSender> { player, ctx, _ ->
                val menu = ctx.menu ?: return@execute
                submitAsync {
                    val serialized = Invero.API.getMenuManager().serializeToJson(menu)
                    player.sendLang("paste-init")

                    paste(
                        "Invero Menu Serialization",
                        "menu serialized as json",
                        48,
                        TimeUnit.HOURS,
                        createContent("${menu.id}", serialized, "JSON"),
                    ).apply {
                        when (status) {
                            SUCCESS -> player.sendLang("paste-success", anonymousLink)
                            ERROR -> player.sendLang("paste-failed")
                        }
                    }
                }
            }
        }
    }

    /*
    open <menu> [player] [arguments]
     */
    @CommandBody
    val open = subCommand {
        dynamic("menu") {
            suggestMenuIds()

            dynamic("player", optional = true) {
                suggestPlayers()
                execute<CommandSender> { _, ctx, _ -> ctx.player?.let { ctx.menu?.open(it) } }

                dynamic("arguments", optional = true) {
                    execute<CommandSender> { _, ctx, content ->
                        ctx.player?.let { ctx.menu?.open(it, content.parseMappedArguments()) }
                    }
                }
            }
            execute<Player> { player, ctx, _ -> ctx.menu?.open(player) }
        }
    }

}