package mx.mauriciogs.persistenttvseries.view.fragments

import android.R.attr.description
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

    private val tvSeriesViewModel: TvSeriesViewModel by viewModels() {
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
                        emptyField(tietData1, "Se requiere un titulo")
                    }
                    tietData2.text.toString().isEmpty() -> {
                        emptyField(tietData2, "Se requiere un año")
                    }
                    genresString.isBlank() -> {
                        Toast.makeText(
                            requireContext(),
                            "Se requiere al menos un género",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        // Se inserta el dato en la db con ROOM
                        val tvSerie = TvSerieEntity(
                            title = tietData1.text.toString().trim(),
                            year = tietData2.text.toString().toInt(),
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
                                "Error al guardar el juego ${e.message}",
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
            when {
                checkbox1.isChecked -> genres = "$genres, ${checkbox1.text}"
                checkbox2.isChecked -> genres = "$genres, ${checkbox2.text}"
                checkbox3.isChecked -> genres = "$genres, ${checkbox3.text}"
                checkbox4.isChecked -> genres = "$genres, ${checkbox4.text}"
                checkbox5.isChecked -> genres = "$genres, ${checkbox5.text}"
                checkbox6.isChecked -> genres = "$genres, ${checkbox6.text}"
                checkbox7.isChecked -> genres = "$genres, ${checkbox7.text}"
                checkbox8.isChecked -> genres = "$genres, ${checkbox8.text}"
                else -> { }
            }
        }
        return genres
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
            "Juego agregado con éxito",
            Toast.LENGTH_SHORT
        ).show()
        with(binding){
            tietData1.text?.clear()
            tietData2.text?.clear()
            tietData1.requestFocus()
        }
    }

    private fun emptyField(editText: TextInputEditText, message: String) {
        editText.error = message
        Toast.makeText(
            requireContext(),
            "Llena todos los campos",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
