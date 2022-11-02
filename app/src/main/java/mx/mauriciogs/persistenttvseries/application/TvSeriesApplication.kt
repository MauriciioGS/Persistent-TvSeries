package mx.mauriciogs.persistenttvseries.application

import android.app.Application
import mx.mauriciogs.persistenttvseries.model.TvSerieRepository
import mx.mauriciogs.persistenttvseries.model.db.TvSerieRoom

class TvSeriesApplication : Application(){
    //Para cargar la base de datos con lazy (que se cargue cuando se requiera)
    private val database by lazy{ TvSerieRoom.getDatabase(this@TvSeriesApplication) }

    //Para el repositorio también con lazy
    val repository by lazy { TvSerieRepository(database.tvSerieDao()) }
}
