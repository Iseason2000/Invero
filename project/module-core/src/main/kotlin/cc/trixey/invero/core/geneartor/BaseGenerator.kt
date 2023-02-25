package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.Object

/**
 * Invero
 * cc.trixey.invero.core.geneartor.BaseGenerator
 *
 * @author Arasple
 * @since 2023/2/2 14:39
 */
abstract class BaseGenerator : ElementGenerator {

    override var generated: List<Object>? = null

    override fun generate(context: Any?) = this.generate()

    abstract fun generate()

    override fun filter(block: (Object) -> Boolean): ElementGenerator {
        generated = generated?.filter(block)
        return this
    }

    override fun <R : Comparable<R>> sortBy(block: (Object) -> R): ElementGenerator {
        generated = generated?.sortedBy(block)
        return this
    }

}