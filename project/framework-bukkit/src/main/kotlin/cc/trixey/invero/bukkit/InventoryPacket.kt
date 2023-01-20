package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.nms.handler
import cc.trixey.invero.bukkit.nms.persistContainerId
import cc.trixey.invero.common.event.ClickType
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.bukkit.InventoryPacket
 *
 * @author Arasple
 * @since 2023/1/20 13:14
 */
class InventoryPacket(override val window: BukkitWindow) : ProxyBukkitInventory {

    private var closed: Boolean = true
    private val windowItems = arrayOfNulls<ItemStack?>(containerType.entireWindowSize)

    override fun clear(slots: Collection<Int>) {
        slots.forEach { set(it, null) }
    }

    fun update() {
        handler.sendWindowItems(viewer, persistContainerId, windowItems.toList())
    }

    fun update(vararg slot: Int) {
        slot.forEach {
            handler.sendWindowSetSlot(viewer, persistContainerId, it, windowItems[it])
        }
    }

    override fun get(slot: Int): ItemStack? {
        return windowItems[slot]
    }

    override fun set(slot: Int, itemStack: ItemStack?) {
        windowItems[slot] = itemStack
        update(slot)
    }

    override fun isViewing(): Boolean {
        return !closed
    }

    override fun open() {
        closed = false
        handler.sendWindowOpen(viewer, persistContainerId, containerType, inventoryTitle)
        update()
    }

    override fun close(doCloseInventory: Boolean, updateInventory: Boolean) {
        closed = true
        if (doCloseInventory) handler.sendWindowClose(viewer, persistContainerId)
        if (updateInventory) viewer.updateInventory()
    }

    fun handleClickEvent(slot: Int, type: ClickType) {
        val pos = window.scale.convertToPosition(slot)
        println("handle_click_packet_inv $pos .. ${window.panels.size}")

        window.panels.sortedByDescending { it.weight }.forEach {
            if (pos in it.area) {
                if (it.runClickCallbacks(pos, type, null)) {
                    it.handleClick(pos - it.locate, type, null)
                }
                return
            }
        }
    }

}