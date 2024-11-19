pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter() // Только если это не создает конфликтов
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Только если это не создает конфликтов
    }
}

rootProject.name = "Diňle-de Hiňlen"
include(":app")
