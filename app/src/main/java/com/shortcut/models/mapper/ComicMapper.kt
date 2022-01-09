package com.shortcut.models.mapper

import com.shortcut.data.local.entity.ComicEntity
import com.shortcut.models.ComicView

class ComicMapper : ViewMapper<ComicEntity, ComicView> {
    override fun toModelView(input: ComicEntity): ComicView {
        with(input) {
            return ComicView(
                num, title, transcript, alt, imgUrl, year, month, day, isFavorite
            )
        }
    }

    override fun fromModelView(input: ComicView): ComicEntity {
        with(input) {
            return ComicEntity(
                num, title, transcript, alt, imgUrl, year, month, day, isFavorite
            )
        }
    }
}