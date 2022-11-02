package mx.mauriciogs.persistenttvseries.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.mauriciogs.persistenttvseries.R
import mx.mauriciogs.persistenttvseries.databinding.TvSerieItemBinding
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity

class TvSeriesAdapter(
    private val context: Context
): RecyclerView.Adapter<TvSeriesAdapter.ViewHolder>() {

    private var tvSeries: List<TvSerieEntity> = listOf()
    private val layoutInflater = LayoutInflater.from(context)
    private val platforms = context.resources.getStringArray(R.array.platform)

    class ViewHolder(view: TvSerieItemBinding): RecyclerView.ViewHolder(view.root){
        val tvsTitle = view.tvSerieName
        val tvsGenre = view.tvSerieGenres
        val tvsSeasons = view.tvSerieSeasons
        val tvsYear = view.tvSerieYear
        val tvsDesc = view.tvSerieDesc
        val ivPlatform = view.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TvSerieItemBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvsTitle.text = tvSeries[position].title
        holder.tvsGenre.text = tvSeries[position].genres
        holder.tvsYear.text = tvSeries[position].year.toString()
        holder.tvsSeasons.text = tvSeries[position].seasons
        holder.tvsDesc.text = ""

        try {
            when(tvSeries[position].platform){
                platforms[0] -> holder.ivPlatform.setImageResource(R.drawable.disneyplus)
                platforms[1] -> holder.ivPlatform.setImageResource(R.drawable.netflix)
                platforms[2] -> holder.ivPlatform.setImageResource(R.drawable.primevideo)
                platforms[3] -> holder.ivPlatform.setImageResource(R.drawable.hbomax)
                platforms[4] -> holder.ivPlatform.setImageResource(R.drawable.paramount)
                platforms[5] -> holder.ivPlatform.setImageResource(R.drawable.starplus)
                platforms[6] -> holder.ivPlatform.setImageResource(R.drawable.crunchy)
                else -> holder.ivPlatform.setImageResource(R.drawable.logo)
            }
        } catch (npe: java.lang.NullPointerException){
            holder.ivPlatform.setImageResource(R.drawable.logo)
            Log.e("NPE", "${npe.message}")
        }
    }

    override fun getItemCount(): Int = tvSeries.size

    fun tvSeriesList(list: List<TvSerieEntity>){
        tvSeries = list
        notifyItemRangeChanged(tvSeries.indexOf(tvSeries.first()), itemCount)
    }

}
