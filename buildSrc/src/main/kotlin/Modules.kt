object Modules {
    const val DATA = ":data"
    const val NETWORK = ":network"

    object Features {
        object Search {
            const val UI = ":features:search:ui"
            const val DOMAIN = ":features:search:domain"
        }
        object RequestLocation {
            const val UI = ":features:requestlocation:ui"
            const val DOMAIN = ":features:requestlocation:domain"
        }
    }

    object Core {
        const val UI = ":core:ui"
        const val MODELS = ":core:models"
        const val TEST = ":core:test"
    }
}
