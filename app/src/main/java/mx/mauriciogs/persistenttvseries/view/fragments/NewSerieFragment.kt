package mx.mauriciogs.persistenttvseries.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import mx.mauriciogs.persistenttvseries.R
import mx.mauriciogs.persistenttvseries.application.TvSeriesApplication
import mx.mauriciogs.persistenttvseries.databinding.FragmentNewSerieBinding
import mx.mauriciogs.persistenttvseries.model.entity.TvSerieEntity
import mx.mauriciogs.persistenttvseries.view.fragments.viewmodels.TvSeriesViewModel

class NewSerieFragment : Fragment(){

    private var _binding: FragmentNewSerieBinding? = null
    private val binding get() = _binding!!

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels {
        TvSeriesViewModel.TvSerieViewModelFactory( (requireActivity().application as TvSeriesApplication).repository )
    }

    private var itemMenuSelected: String = ""
    private var platformSelected: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewSerieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initButton()
    }

    private fun initButton() {
        with(binding) {
            btnDone.setOnClickListener {
                val genresString = checkGenresBoxes()
                when {
                    tietData1.text.toString().isEmpty() -> {
                        emptyField(tietData1, getString(R.string.txt_no_titulo))
                    }
                    tietData2.text.toString().isEmpty() -> {
                        emptyField(tietData2, getString(R.string.txt_no_anio))
                    }
                    tietData3.text.toString().isEmpty() -> {
                        emptyField(tietData3, getString(R.string.txt_no_desc))
                    }
                    genresString.isBlank() -> {
                        Toast.makeText(
                            requireContext(),
                            getText(R.string.txt_no_genero),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    itemMenuSelected.isBlank() -> {
                        Toast.makeText(
                            requireContext(),
                            getText(R.string.txt_no_temporadas),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    platformSelected.isBlank() -> {
                        Toast.makeText(
                            requireContext(),
                            getText(R.string.txt_no_plataforma),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        // Se inserta el dato en la db con ROOM
                        val tvSerie = TvSerieEntity(
                            title = tietData1.text.toString().trim(),
                            year = tietData2.text.toString().toInt(),
                            description = tietData3.text.toString(),
                            platform = platformSelected,
                            seasons = itemMenuSelected,
                            genres = genresString
                        )
                        try {
                            tvSeriesViewModel.insertTvSerie(tvSerie)
                            insertSuccessfull()
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "${getText(R.string.txt_error_guardado)} ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
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

    private fun initComponents() {
        val seasons = resources.getStringArray(R.array.seasons)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_seasons_item, seasons)
        (binding.seasonMenu.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = (AdapterView.OnItemClickListener { parent, _, position, _ ->
                itemMenuSelected = parent?.getItemAtPosition(position).toString()
            })
        }

        val platforms = resources.getStringArray(R.array.platform)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.list_seasons_item, platforms)
        (binding.platformMenu.editText as AutoCompleteTextView).apply {
            setAdapter(adapter2)
            onItemClickListener = (AdapterView.OnItemClickListener { parent, _, position, _ ->
                platformSelected = parent?.getItemAtPosition(position).toString()
            })
        }
    }

    private fun insertSuccessfull() {
        Toast.makeText(
            requireContext(),
            getText(R.string.txt_exito_guardado),
            Toast.LENGTH_SHORT
        ).show()
        with(binding){
            tietData1.text?.clear()
            tietData2.text?.clear()
            tietData3.text?.clear()
            tietData1.requestFocus()

            checkbox1.isChecked = false
            checkbox2.isChecked = false
            checkbox3.isChecked = false
            checkbox4.isChecked = false
            checkbox5.isChecked = false
            checkbox6.isChecked = false
            checkbox7.isChecked = false
            checkbox8.isChecked = false
        }
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
