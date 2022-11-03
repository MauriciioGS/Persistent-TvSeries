package mx.mauriciogs.persistenttvseries.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import mx.mauriciogs.persistenttvseries.R
import mx.mauriciogs.persistenttvseries.application.TvSeriesApplication
import mx.mauriciogs.persistenttvseries.databinding.FragmentEditTvSerieBinding
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity
import mx.mauriciogs.persistenttvseries.view.fragments.viewmodels.TvSeriesViewModel

class EditTvSerieFragment : Fragment() {

    private var _binding: FragmentEditTvSerieBinding? = null
    private val binding get() = _binding!!

    var tvSerieEntity: TvSerieEntity? = null
    private var serieId: Long = 0
    private var itemMenuSelected: String = ""
    private var platformSelected: String = ""

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels {
        TvSeriesViewModel.TvSerieViewModelFactory( (requireActivity().application as TvSeriesApplication).repository )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTvSerieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        initButton()
    }

    private fun initButton() {
        with(binding) {
            btnDone.setOnClickListener {
                if (validateData()){
                    val genresString = checkGenresBoxes()
                    if (genresString.isBlank()){
                        Toast.makeText(
                            requireContext(),
                            getText(R.string.txt_no_genero),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else{
                        try {
                            // Se inserta el dato en la db con ROOM
                            val tvSerie = TvSerieEntity(
                                id = tvSerieEntity!!.id,
                                title = tietData1.text.toString().trim(),
                                year = tietData2.text.toString().toInt(),
                                description = tietData3.text.toString(),
                                platform = platformSelected,
                                seasons = itemMenuSelected,
                                genres = genresString
                            )
                            tvSeriesViewModel.updateTvSerie(tvSerie)
                            updateSuccessfull()
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "${getText(R.string.error_actualizar)} ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateData(): Boolean {
        return with(binding){
            when{
                tietData1.text.toString().isEmpty() -> {
                    emptyField(tietData1, getString(R.string.txt_no_titulo))
                    false
                }
                tietData2.text.toString().isEmpty() -> {
                    emptyField(tietData2, getString(R.string.txt_no_anio))
                    false
                }
                tietData3.text.toString().isEmpty() -> {
                    emptyField(tietData3, getString(R.string.txt_no_desc))
                    false
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun updateSuccessfull() {
        Toast.makeText(
            requireContext(),
            getText(R.string.exito_actualizar),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(EditTvSerieFragmentDirections.actionEditTvSerieFragmentToListSeriesFragment())
    }

    private fun getArgs() {
        val args: EditTvSerieFragmentArgs by navArgs()
        serieId = args.id
        if (serieId != 0L) {
            tvSeriesViewModel.getTvSerie(serieId).observe(viewLifecycleOwner) {
                it?.let {
                    tvSerieEntity = it
                    setData()
                }
            }
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
        tvSerieEntity?.let {
            with(binding){
                tietData1.setText(tvSerieEntity!!.title)
                tietData1.requestFocus()
                tietData2.setText(tvSerieEntity!!.year.toString())
                tietData3.setText(tvSerieEntity!!.description)

                setCheckBoxes()

                val seasons = resources.getStringArray(R.array.seasons)
                val adapter = ArrayAdapter(requireContext(), R.layout.list_seasons_item, seasons)
                (binding.seasonMenu.editText as AutoCompleteTextView).apply {
                    setAdapter(adapter)
                    itemMenuSelected =
                        adapter.getItem(seasons.indexOf(tvSerieEntity!!.seasons)).toString()
                    setText(itemMenuSelected, false)

                    onItemClickListener = (AdapterView.OnItemClickListener { parent, _, position, _ ->
                        itemMenuSelected = parent?.getItemAtPosition(position).toString()
                    })
                }

                val platforms = resources.getStringArray(R.array.platform)
                val adapter2 = ArrayAdapter(requireContext(), R.layout.list_seasons_item, platforms)
                (binding.platformMenu.editText as AutoCompleteTextView).apply {
                    setAdapter(adapter2)
                    platformSelected =
                        adapter2.getItem(platforms.indexOf(tvSerieEntity!!.platform)).toString()
                    setText(platformSelected, false)

                    onItemClickListener = (AdapterView.OnItemClickListener { parent, _, position, _ ->
                        platformSelected = parent?.getItemAtPosition(position).toString()
                    })
                }
            }
        }
    }

    private fun setCheckBoxes() {
        val genres = tvSerieEntity!!.genres.split(" ,").toTypedArray()
        with(binding){
            if (genres[0].contains(checkbox1.text))
                checkbox1.isChecked = true
            if (genres[0].contains(checkbox2.text))
                checkbox2.isChecked = true
            if (genres[0].contains(checkbox3.text))
                checkbox3.isChecked = true
            if (genres[0].contains(checkbox4.text))
                checkbox4.isChecked = true
            if (genres[0].contains(checkbox5.text))
                checkbox5.isChecked = true
            if (genres[0].contains(checkbox6.text))
                checkbox6.isChecked = true
            if (genres[0].contains(checkbox7.text))
                checkbox7.isChecked = true
            if (genres[0].contains(checkbox8.text))
                checkbox8.isChecked = true
        }
    }

    private fun checkGenresBoxes(): String {
        var genres = ""
        with(binding) {
            if (checkbox1.isChecked)
                genres = "$genres, ${checkbox1.text}"
            if (checkbox2.isChecked)
                genres = "$genres, ${checkbox2.text}"
            if (checkbox3.isChecked)
                genres = "$genres, ${checkbox3.text}"
            if (checkbox4.isChecked)
                genres = "$genres, ${checkbox4.text}"
            if (checkbox5.isChecked)
                genres = "$genres, ${checkbox5.text}"
            if (checkbox6.isChecked)
                genres = "$genres, ${checkbox6.text}"
            if (checkbox7.isChecked)
                genres = "$genres, ${checkbox7.text}"
            if (checkbox8.isChecked)
                genres = "$genres, ${checkbox8.text}"
        }
        if (genres.isBlank())
            return genres
        return genres.substring(2)
    }

    private fun emptyField(editText: TextInputEditText, message: String) {
        editText.error = message
        Toast.makeText(
            requireContext(),
            getText(R.string.txt_no_campos),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}