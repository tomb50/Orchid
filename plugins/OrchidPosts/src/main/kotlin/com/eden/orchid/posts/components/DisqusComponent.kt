package com.eden.orchid.posts.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent

@Description("Easily add comments to any page with Disqus.", name = "Disqus")
class DisqusComponent : OrchidComponent("disqus") {

    @Option
    @Description("Your disqus shortname.")
    lateinit var shortname: String

    @Option
    @Description("A site-wide unique identifier for the comment section on this page. Defaults to the page's URL.")
    lateinit var identifier: String

}

