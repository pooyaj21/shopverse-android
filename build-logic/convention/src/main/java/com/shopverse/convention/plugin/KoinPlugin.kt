package com.shopverse.convention.plugin

import com.shopverse.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused") // Used in `build.gradle.kts`
class KoinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findLibrary("koin.core").get())
            }
        }
    }
}
