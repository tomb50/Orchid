package com.eden.orchid.sourcedoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.model.SourceDocModel

@Description("The module home pages for a given module type..", name = "Source Pages")
class SourceDocModulesMenuItemType : OrchidMenuFactory("sourcedocModules") {

    @Option
    lateinit var title: String

    @Option
    lateinit var moduleType: String

    @Option
    lateinit var moduleGroup: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        return try {
            val model: SourceDocModel? =
                context.resolve(SourceDocModel::class.java, moduleType) ?: context.resolve(SourceDocModel::class.java)

            // no model, return early
            if (model == null) return emptyList()

            model
                .modules
                .filter { if(moduleGroup.isNotBlank()) it.moduleGroup == moduleGroup else true }
                .map { it.homepage }
                .sortedBy { it.title }
                .map { page ->
                    MenuItem.Builder(context)
                        .page(page)
                        .build()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}
