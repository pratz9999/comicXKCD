package com.shortcut.models.mapper

/***
 * Model mapper interface
 * @param VIEW_MODEL Any Object to map from/to
 * @param DB_ENTITY database entity
 */
interface ViewMapper<DB_ENTITY, VIEW_MODEL> {
    fun toModelView(input: DB_ENTITY): VIEW_MODEL
    fun fromModelView(input: VIEW_MODEL): DB_ENTITY
}