package picflow.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import picflow.model.Album

data class AlbumUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val esPrivado: Boolean = false,
    val errorNombre: String? = null,
    val errorTipo: String? = null,
    val errorDescripcion: String? = null
)

class AlbumViewModel : ViewModel() {

    var uiState by mutableStateOf(AlbumUiState())
        private set

    val albumes = mutableStateListOf<Album>()
    private var siguienteId = 1

    fun actualizarNombre(v: String) {
        uiState = uiState.copy(nombre = v, errorNombre = null)
    }

    fun actualizarDescripcion(v: String) {
        uiState = uiState.copy(descripcion = v, errorDescripcion = null)
    }

    fun actualizarTipo(v: String) {
        uiState = uiState.copy(tipo = v, errorTipo = null)
    }

    fun actualizarPrivado(v: Boolean) {
        uiState = uiState.copy(esPrivado = v)
    }

    /** Devuelve el id del álbum creado o null si hay errores. */
    fun registrar(): Int? {
        val s = uiState
        val nombre = s.nombre.trim()

        val errorNombre = when {
            nombre.isBlank() -> "El nombre es obligatorio"
            nombre.length !in 3..30 -> "Usa entre 3 y 30 caracteres"
            albumes.any { it.nombre.equals(nombre, ignoreCase = true) } ->
                "Ya existe un álbum con ese nombre"
            else -> null
        }
        val errorTipo = if (s.tipo.isBlank()) "Selecciona un tipo de álbum" else null
        val errorDesc = if (s.descripcion.length > 100) "Máximo 100 caracteres" else null

        if (errorNombre != null || errorTipo != null || errorDesc != null) {
            // Se conserva lo que el usuario escribió
            uiState = s.copy(
                errorNombre = errorNombre,
                errorTipo = errorTipo,
                errorDescripcion = errorDesc
            )
            return null
        }

        val album = Album(siguienteId++, nombre, s.descripcion.trim(), s.tipo, s.esPrivado)
        albumes.add(album)
        uiState = AlbumUiState()
        return album.id
    }

    fun buscarAlbum(id: Int): Album? = albumes.firstOrNull { it.id == id }
}