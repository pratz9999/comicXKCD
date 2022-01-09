package com.shortcut.data.local.entity.mapper

import com.shortcut.data.local.entity.ComicEntity
import com.shortcut.data.remote.model.RemoteComic

class ComicMapper : EntityMapper<RemoteComic, ComicEntity> {

    override fun toDbEntity(input: RemoteComic): ComicEntity {
        with(input) {
            return ComicEntity(
                num,
                title,
                transcript,
                alt,
                img,
                year.toInt(),
                month.toInt(),
                day.toInt(),
            )
        }

    }

    override fun fromDbEntity(input: ComicEntity): RemoteComic {
        with(input) {
            return RemoteComic(
                alt,
                day.toString(),
                imgUrl ?: "",
                "",
                month.toString(),
                "",
                num,
                "",
                title,
                transcript, year.toString()

            )
        }
    }
}