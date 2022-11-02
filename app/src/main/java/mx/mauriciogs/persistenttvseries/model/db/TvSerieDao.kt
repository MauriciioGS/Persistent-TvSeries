package mx.mauriciogs.persistenttvseries.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import mx.mauriciogs.persistenttvseries.model.db.Constants.DATABASE_GAME_TABLE
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity

@Dao
interface TvSerieDao {
    @Insert
    suspend fun insertTvSerie(tvSerie: TvSerieEntity)

    @Query("SELECT * FROM $DATABASE_GAME_TABLE ORDER BY serieId")
    fun getAllSeriesList(): Flow<List<TvSerieEntity>>

    @Query("SELECT * FROM $DATABASE_GAME_TABLE WHERE serieId = :id LIMIT 1")
    fun getSerie(id: Long): Flow<TvSerieEntity>

    @Update
    suspend fun updateSerie(serie: TvSerieEntity)

    @Delete
    suspend fun deleteSerie(serie: TvSerieEntity)
}