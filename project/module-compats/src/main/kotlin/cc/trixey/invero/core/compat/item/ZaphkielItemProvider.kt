package cc.trixey.invero.core.compat.item

import cc.trixey.invero.common.ItemSourceProvider
import cc.trixey.invero.core.compat.DefItemProvider
import cc.trixey.invero.core.compat.PluginHook
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.ZaphkielItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:44
 */
@DefItemProvider(["zaphkiel", "zap"])
class ZaphkielItemProvider : ItemSourceProvider, PluginHook() {

    override val pluginName = "Zaphkiel"

    override fun getItem(identifier: String, context: Any?): ItemStack? {
        return ZaphkielAPI.getItem(identifier)?.itemStack
    }

}