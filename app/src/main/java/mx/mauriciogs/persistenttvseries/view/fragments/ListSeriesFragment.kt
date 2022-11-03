package mx.mauriciogs.persistenttvseries.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mx.mauriciogs.persistenttvseries.R
import mx.mauriciogs.persistenttvseries.application.TvSeriesApplication
import mx.mauriciogs.persistenttvseries.databinding.FragmentListSeriesBinding
import mx.mauriciogs.persistenttvseries.domain.TvSerie
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity
import mx.mauriciogs.persistenttvseries.view.adapters.TvSeriesAdapter
import mx.mauriciogs.persistenttvseries.view.fragments.viewmodels.TvSeriesViewModel

class ListSeriesFragment : Fragment() {

    private var _binding: FragmentListSeriesBinding? = null
    private val binding get() = _binding!!

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels {
        TvSeriesViewModel.TvSerieViewModelFactory( (requireActivity().application as TvSeriesApplication).repository )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listSeriesFragment_to_newSerieFragment)
        }
    }

    private fun initRecyclerView() {
        val tvSeriesAdapter = TvSeriesAdapter(requireActivity(), this)
        binding.rvTvSeriesList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvTvSeriesList.adapter = tvSeriesAdapter

        tvSeriesViewModel.allTvSeriesList.observe(viewLifecycleOwner) { tvSeries ->
            tvSeries?.let {
                if (tvSeries.isNotEmpty()) {
                    binding.llcNoReg.visibility = View.GONE
                    tvSeriesAdapter.tvSeriesList(tvSeries)
                } else {
                    binding.llcNoReg.visibility = View.VISIBLE
                }
            }
        }
    }

    fun selectedTvSerie(tvSerie: TvSerieEntity){
        // To DetailSerie
        findNavController().navigate(
            ListSeriesFragmentDirections.actionListSeriesFragmentToTvDetailFragment(
                TvSerie(
                    id = tvSerie.id,
                    title = tvSerie.title,
                    year = tvSerie.year,
                    description = tvSerie.description,
                    seasons = tvSerie.seasons,
                    platform = tvSerie.platform,
                    genres = tvSerie.genres
                )
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}