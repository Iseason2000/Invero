package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.bukkit.nms.WindowProperty
import cc.trixey.invero.ui.bukkit.nms.handler
import cc.trixey.invero.ui.bukkit.nms.persistContainerId
import cc.trixey.invero.ui.common.ContainerType
import cc.trixey.invero.ui.common.StorageMode

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.WindowAnvil
 *
 * @author Arasple
 * @since 2023/2/24 13:05
 *
 * Slot range	Description
 * 0	first item
 * 1	second item
 * 2	result
 * 3–29	main inventory
 * 30–38	hotbar
 */
class WindowAnvil(
    viewer: PlayerViewer,
    title: String = "Untitled_Anvil",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true
) : WindowDefault(viewer, ContainerType.ANVIL, title, storageMode, virtual) {

    fun setRepairCost(value: Int) = handler
        .sendWindowUpdateData(viewer.get(), persistContainerId, WindowProperty.ANVIL_REPAIR_COST, value)

    fun getRenameText() {
//        ((inventory as InventoryVanilla).container as AnvilInventory).renameText
    }

}