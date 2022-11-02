package mx.mauriciogs.persistenttvseries.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import mx.mauriciogs.persistenttvseries.model.db.TvSerieDao
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity

class TvSerieRepository(
    private val tvSerieDao: TvSerieDao
) {
    // Create
    @WorkerThread
    suspend fun insertTvSerieData(serieEntity: TvSerieEntity){
        tvSerieDao.insertTvSerie(serieEntity)
    }

    // Read
    val allTvSeriesList: Flow<List<TvSerieEntity>> = tvSerieDao.getAllSeriesList()
    fun getTvSerie(id: Long): Flow<TvSerieEntity> = tvSerieDao.getSerie(id)

    // Update
    @WorkerThread
    suspend fun updateTvSerie(serieEntity: TvSerieEntity){
        tvSerieDao.updateSerie(serieEntity)
    }

    // Delete
    @WorkerThread
    suspend fun deleteTvSerie(serieEntity: TvSerieEntity){
        tvSerieDao.deleteSerie(serieEntity)
    }
}