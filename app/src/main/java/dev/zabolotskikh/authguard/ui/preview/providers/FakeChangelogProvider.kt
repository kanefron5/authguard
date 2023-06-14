package dev.zabolotskikh.authguard.ui.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.zabolotskikh.authguard.domain.model.ChangelogItem
import dev.zabolotskikh.authguard.domain.model.Release

class FakeChangelogProvider : PreviewParameterProvider<List<Release>> {
    override val values: Sequence<List<Release>>
        get() = sequenceOf(
            listOf(
                Release(
                    "v0.1.0", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem(
                            "TEST-2",
                            "kdkfkdkog kodkgodkgkdk fkdkogkodkgodkgkdkfkdkogkodkgodkgkdkfkdkogkodkgodkgkdkfkdkogkodkgodkg",
                            "https://example.com/"
                        ),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-4", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                )
            ),
            listOf(
                Release(
                    "v0.1.0", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem(
                            "TEST-2",
                            "kdkfkdkog kodkgodkgkdk fkdkogkodkgodkgkdkfkdkogkodkgodkgkdkfkdkogkodkgodkgkdkfkdkogkodkgodkg",
                            "https://example.com/"
                        ),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-4", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
                Release(
                    "v0.0.1", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-2", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
                Release(
                    "v0.0.1", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-2", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
                Release(
                    "v0.0.1", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-2", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
                Release(
                    "v0.0.1", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-2", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
                Release(
                    "v0.0.1", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-2", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                )
            )
        )
}