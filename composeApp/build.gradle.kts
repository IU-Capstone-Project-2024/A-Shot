// build.gradle.kts

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.jetbrainsCompose)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.ksp)
	alias(libs.plugins.room)

	kotlin("plugin.serialization") version "2.0.0"
}

kotlin {
	jvm("desktop")

	sourceSets {
		val desktopMain by getting

		commonMain.dependencies {
			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.resources)
			implementation(compose.components.uiToolingPreview)
			implementation(compose.materialIconsExtended)

			implementation(libs.room.runtime)
			implementation(libs.sqlite.bundled)

			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")
//			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

			implementation("org.jetbrains.compose.material3:material3-desktop:1.6.11")
			implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
			implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

			implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
		}

		desktopMain.dependencies {
			implementation(compose.desktop.currentOs)
		}
	}
}


compose.desktop {
	application {
		mainClass = "MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "org.example.project"
			packageVersion = "1.0.0"
		}
	}
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	ksp(libs.room.compiler)
}
