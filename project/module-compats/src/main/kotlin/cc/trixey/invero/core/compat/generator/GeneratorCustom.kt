package cc.trixey.invero.core.compat.generator

import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.core.compat.DefGeneratorProvider
import cc.trixey.invero.core.geneartor.BaseGenerator

/**
 * Invero
 * cc.trixey.invero.core.compat.generator.GeneratorCustom
 *
 * @author Arasple
 * @since 2023/1/29 22:30
 */
@DefGeneratorProvider("custom")
class GeneratorCustom : BaseGenerator() {

    override var generated: List<Object>? = listOf()

    override fun generate() {}

}