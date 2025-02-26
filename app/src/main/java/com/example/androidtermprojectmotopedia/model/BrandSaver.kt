package com.example.androidtermprojectmotopedia.model

import androidx.compose.runtime.saveable.Saver

/**
 * Helper to safely get [index] from [list] and cast to [T]. If out of range or type mismatch,
 * returns [fallback].
 */
@Suppress("UNCHECKED_CAST")
private inline fun <reified T> listGetOr(list: List<Any?>, index: Int, fallback: T): T {
    if (index < 0 || index >= list.size) return fallback
    return list[index] as? T ?: fallback
}

/**
 * Saver for StoreLocation.
 * We'll store [latitude, longitude, address] in a List<Any?>.
 */
val StoreLocationSaver: Saver<StoreLocation, List<Any?>> = Saver(
    save = { location ->
        listOf(
            location.latitude,
            location.longitude,
            location.address
        )
    },
    restore = { restoredList ->
        val lat  = listGetOr(restoredList, 0, 0.0)
        val lng  = listGetOr(restoredList, 1, 0.0)
        val addr = listGetOr(restoredList, 2, "")
        StoreLocation(lat, lng, addr)
    }
)

/**
 * Saver for Store.
 * We'll store [name, locationAsList] in a List<Any?>.
 */
val StoreSaver: Saver<Store, List<Any?>> = Saver(
    save = { store ->
        listOf(
            store.name,
            // Convert the StoreLocation to a saveable list
            with(StoreLocationSaver) {
                save(store.location)
            }
        )
    },
    restore = { restoredList ->
        val storeName = listGetOr(restoredList, 0, "")
        val locationList = listGetOr<List<Any?>>(restoredList, 1, emptyList())
        val location = with(StoreLocationSaver) {
            restore(locationList)
        } ?: StoreLocation()

        Store(storeName, location)
    }
)

/**
 * Saver for Brand.
 * We'll store [brand, founded, founder, logo, headquarters, detail, storeList].
 */
val BrandSaver: Saver<Brand, List<Any?>> = Saver(
    save = { brand ->
        listOf(
            brand.brand,
            brand.founded,
            brand.founder,
            brand.logo,
            brand.headquarters,
            brand.detail,
            // Convert each Store into a saveable list of Any?
            brand.stores.map { store ->
                with(StoreSaver) {
                    save(store) // store -> List<Any?>
                }
            }
        )
    },
    restore = { restoredList ->
        val brandName    = listGetOr(restoredList, 0, "")
        val founded      = listGetOr(restoredList, 1, 0)
        val founder      = listGetOr(restoredList, 2, "")
        val logo         = listGetOr(restoredList, 3, "")
        val headquarters = listGetOr(restoredList, 4, "")
        val detail       = listGetOr(restoredList, 5, "")

        // Convert the List<List<Any?>> back into List<Store>
        val storeListAny = listGetOr<List<Any?>>(restoredList, 6, emptyList())
        val stores = storeListAny.map { storeItem ->
            val storeData = storeItem as? List<Any?> ?: emptyList()
            with(StoreSaver) {
                restore(storeData)
            } ?: Store()
        }

        Brand(
            brand         = brandName,
            founded       = founded,
            founder       = founder,
            logo          = logo,
            headquarters  = headquarters,
            detail        = detail,
            stores        = stores
        )
    }
)
