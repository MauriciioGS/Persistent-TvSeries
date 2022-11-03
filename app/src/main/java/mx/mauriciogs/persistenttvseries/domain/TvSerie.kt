package mx.mauriciogs.persistenttvseries.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvSerie(
    var id: Long = 0,
    var title: String,
    var year: Int,
    var description: String,
    var seasons: String,
    var platform: String,
    var genres: String
) : Parcelable
