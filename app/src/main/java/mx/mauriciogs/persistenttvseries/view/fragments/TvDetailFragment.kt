package mx.mauriciogs.persistenttvseries.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import mx.mauriciogs.persistenttvseries.R
import mx.mauriciogs.persistenttvseries.application.TvSeriesApplication
import mx.mauriciogs.persistenttvseries.databinding.FragmentTvDetailBinding
import mx.mauriciogs.persistenttvseries.domain.TvSerie
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity
import mx.mauriciogs.persistenttvseries.view.fragments.viewmodels.TvSeriesViewModel

class TvDetailFragment : Fragment() {

    private var _binding: FragmentTvDetailBinding? = null
    private val binding get() = _binding!!

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels() {
        TvSeriesViewModel.TvSerieViewModelFactory( (requireActivity().application as TvSeriesApplication).repository )
    }

    var tvSerieEntity: TvSerieEntity? = null
    private var tvSerie: TvSerie? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            btnDelete.setOnClickListener {
                deleteTvSerie()
            }
            btnEdit.setOnClickListener {
                findNavController().navigate(TvDetailFragmentDirections.actionTvDetailFragmentToEditTvSerieFragment(
                    tvSerie!!.id
                ))
            }
        }
    }

    private fun deleteTvSerie() {
        AlertDialog.Builder(requireContext())
            .setTitle(getText(R.string.dialog_titulo))
            .setMessage("¿ ${getText(R.string.dialog_message)} ${tvSerie?.title}?")
            .setPositiveButton(getText(R.string.btn_aceptar), DialogInterface.OnClickListener { _, _ ->
                try{
                    tvSeriesViewModel.deleteTvSerie(tvSerieEntity!!)
                    deleteSuccessful()
                }catch(e: Exception){
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.error_borrado),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setNegativeButton(getText(R.string.btn_cancelar), DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
            .show()
    }

    private fun deleteSuccessful() {
        Toast.makeText(
            requireContext(),
            getText(R.string.exito_borrado),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(TvDetailFragmentDirections.actionTvDetailFragmentToListSeriesFragment2())
    }

    private fun getArgs() {
        val args: TvDetailFragmentArgs by navArgs()
        tvSerie = args.tvSerie
        if (tvSerie != null) {
            tvSerieEntity = TvSerieEntity(
                id = tvSerie!!.id,
                title = tvSerie!!.title,
                year = tvSerie!!.year,
                description = tvSerie!!.description,
                platform = tvSerie!!.platform,
                genres = tvSerie!!.genres,
                seasons = tvSerie!!.seasons
            )
            setData()
        } else {
            Toast.makeText(
                requireContext(),
                getText(R.string.error_general),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }
    }
    private fun setData() {

        with(binding){
            tietTitle.setText(tvSerie!!.title)
            tietYear.setText(tvSerie!!.year.toString())
            tietSeasons.setText(tvSerie!!.seasons)
            tietPlatform.setText(tvSerie!!.platform)
            tietGenres.setText(tvSerie!!.genres)

            tietTitle.inputType = InputType.TYPE_NULL
            tietYear.inputType = InputType.TYPE_NULL
            tietSeasons.inputType = InputType.TYPE_NULL
            tietPlatform.inputType = InputType.TYPE_NULL
            tietGenres.inputType = InputType.TYPE_NULL
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}