import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
	kotlin("jvm")
	id("org.jetbrains.compose")
}

group = "test"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
}

dependencies {
	implementation(compose.desktop.currentOs)
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
	implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
}



compose.desktop {
	application {
		mainClass = "MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "Debug"
			packageVersion = "1.0.0"

			windows {
				iconFile.set(project.file("src/main/resources/icons/icon.ico"))
			}
			macOS {
				iconFile.set(project.file("src/main/resources/icons/icon.icns"))
			}
			linux {
				iconFile.set(project.file("src/main/resources/icons/icon.png"))
			}
		}
	}
}
