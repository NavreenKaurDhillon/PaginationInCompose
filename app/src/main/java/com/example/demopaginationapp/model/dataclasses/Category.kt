package com.example.demopaginationapp.model.dataclasses

data class Category(
    val children: List<Children>,
    val icon: String,
    val image: String,
    val isLastChildCategory: String,
    val prodcat_active: String,
    val prodcat_code: String,
    val prodcat_content_block: String,
    val prodcat_id: String,
    val prodcat_name: String,
    val prodcat_parent: String,
    val prodcat_updated_on: String,
    val prodrootcat_code: String,
    val productCounts: String
)