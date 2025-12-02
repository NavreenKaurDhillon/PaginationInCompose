package com.example.demopaginationapp.navigation

object Screens {
    const val Home = "home_screen"
    const val ProductsList = "product_screen"
    const val Favorites = "fav_screen"
    const val ReposList = "list_screen"
    const val Cart = "cart_screen"
    const val Search = "search_screen"
    const val Coupons = "coupon_screen"
    const val RepoDetail = "detail_screen"
    const val ProductDetail = "product_detail_screen"
    const val Brands = "brand_screen"
    const val Categories = "categories_screen"
}

val bottomBarRoutes = listOf(Screens.Home, Screens.Categories, Screens.Brands, Screens.Favorites, Screens.Cart)