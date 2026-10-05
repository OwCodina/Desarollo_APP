package picflow.model

data class Album(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val esPrivado: Boolean
)