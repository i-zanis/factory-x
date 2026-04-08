pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "factory-x"

include("common-domain")
include("catalog-service")
include("inventory-service")
include("order-service")
include("ai-assistant-service")
