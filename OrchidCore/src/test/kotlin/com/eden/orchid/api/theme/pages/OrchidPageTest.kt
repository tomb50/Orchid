package com.eden.orchid.testhelpers.com.eden.orchid.api.theme.pages

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.site.OrchidSite
import com.eden.orchid.api.site.OrchidSiteImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class OrchidPageTest {

    private lateinit var context: OrchidContext
    private lateinit var site: OrchidSite
    private lateinit var extractor: OptionsExtractor
    private lateinit var reference: OrchidReference
    private lateinit var resource: OrchidResource
    private lateinit var underTest: OrchidPage

    fun setup(baseUrl: String, pagePath: String) {
        context = mock(OrchidContext::class.java)
        extractor = mock(OptionsExtractor::class.java)
        site = OrchidSiteImpl("1.0", "1.0", baseUrl, "dev", "peb")
        `when`(context.getService(OrchidSite::class.java)).thenReturn(site)
        `when`(context.resolve(OptionsExtractor::class.java)).thenReturn(extractor)
        `when`(context.baseUrl).thenCallRealMethod()

        reference = OrchidReference(context, pagePath, true)
        resource = StringResource("page content", reference)
        underTest = OrchidPage(resource, "test", "Test Page")
    }

    @ParameterizedTest
    @CsvSource(
        // | base URL                  | Page Path | Expected                              |
        "/                             , page/one  , /page/one                             ",
        "/inner                        , page/one  , /inner/page/one                       ",
        "/inner/                       , page/one  , /inner/page/one                       ",
        "/inner/deep                   , page/one  , /inner/deep/page/one                  ",
        "/inner/deep/                  , page/one  , /inner/deep/page/one                  ",
        "/                             , /page/one , /page/one                             ",
        "/inner                        , /page/one , /inner/page/one                       ",
        "/inner/                       , /page/one , /inner/page/one                       ",
        "/inner/deep                   , /page/one , /inner/deep/page/one                  ",
        "/inner/deep/                  , /page/one , /inner/deep/page/one                  ",

        "http://example.com            , page/one  , http://example.com/page/one           ",
        "http://example.com/           , page/one  , http://example.com/page/one           ",
        "http://example.com/inner      , page/one  , http://example.com/inner/page/one     ",
        "http://example.com/inner/     , page/one  , http://example.com/inner/page/one     ",
        "http://example.com/inner/deep , page/one  , http://example.com/inner/deep/page/one",
        "http://example.com/inner/deep/, page/one  , http://example.com/inner/deep/page/one",
        "http://example.com            , /page/one , http://example.com/page/one           ",
        "http://example.com/           , /page/one , http://example.com/page/one           ",
        "http://example.com/inner      , /page/one , http://example.com/inner/page/one     ",
        "http://example.com/inner/     , /page/one , http://example.com/inner/page/one     ",
        "http://example.com/inner/deep , /page/one , http://example.com/inner/deep/page/one",
        "http://example.com/inner/deep/, /page/one , http://example.com/inner/deep/page/one"
    )
    fun testGetLink(baseUrl: String, pagePath: String, expected: String) {
        setup(baseUrl, pagePath)
        expectThat(underTest.link).isEqualTo(expected)
    }

}