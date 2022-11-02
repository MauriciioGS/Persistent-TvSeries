package mx.mauriciogs.persistenttvseries.view.fragments.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import mx.mauriciogs.persistenttvseries.model.TvSerieRepository
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity

class TvSeriesViewModel(
    private val repository: TvSerieRepository
) : ViewModel() {

    fun insertTvSerie(tvSerie: TvSerieEntity) = viewModelScope.launch {
        repository.insertTvSerieData(tvSerie)
    }

    val allTvSeriesList: LiveData<List<TvSerieEntity>> = repository.allTvSeriesList.asLiveData()

    fun getTvSerie(id: Long): LiveData<TvSerieEntity> = repository.getTvSerie(id).asLiveData()

    fun deleteTvSerie(tvSerie: TvSerieEntity) = viewModelScope.launch {
        repository.deleteTvSerie(tvSerie)
    }

    fun updateTvSerie(tvSerie: TvSerieEntity) = viewModelScope.launch {
        repository.updateTvSerie(tvSerie)
    }

    class TvSerieViewModelFactory(private val repository: TvSerieRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TvSeriesViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TvSeriesViewModel(repository) as T
            }
            throw  java.lang.IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}