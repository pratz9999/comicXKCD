package com.shortcut.data.local.entity.mapper

/***
 * Model mapper interface
 * @param T Any Object to map from/to
 * @param DB_ENTITY database entity
 */
interface EntityMapper<T, DB_ENTITY> {
    fun toDbEntity(input: T): DB_ENTITY
    fun fromDbEntity(input: DB_ENTITY): T
}