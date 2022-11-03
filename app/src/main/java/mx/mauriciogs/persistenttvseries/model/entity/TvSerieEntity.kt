package mx.mauriciogs.persistenttvseries.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import mx.mauriciogs.persistenttvseries.model.db.Constants.DATABASE_GAME_TABLE

@Entity(tableName = DATABASE_GAME_TABLE)
data class TvSerieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "serieId") var id: Long = 0,
    @ColumnInfo(name = "serieTitle") var title: String,
    @ColumnInfo(name = "serieYear") var year: Int,
    @ColumnInfo(name = "serieDescription") var description: String,
    @ColumnInfo(name = "serieSeasons") var seasons: String,
    @ColumnInfo(name = "seriePlatform") var platform: String,
    @ColumnInfo(name = "serieGenres") var genres: String
)
