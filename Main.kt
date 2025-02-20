import java.io.File
import java.util.*

data class Tramo(val vehiculo: String, val origen: String, val destino: String, val coste: Double) : Comparable<Tramo> {
    override fun compareTo(other: Tramo): Int = coste.compareTo(other.coste)
}

class RedTransporte {
    private val tramos = mutableListOf<Tramo>()

    fun agregarTramo(vehiculo: String, origen: String, destino: String, coste: Double) {
        tramos.add(Tramo(vehiculo, origen, destino, coste))
    }

    fun kruskal(): MutableList<Tramo> {
        val arbolDeExpansionMinima = mutableListOf<Tramo>()
        val conjuntos = mutableMapOf<String, String>()

        // Inicializar cada nodo como su propio conjunto
        val nodos = tramos.flatMap { listOf(it.origen, it.destino) }.toSet()
        nodos.forEach { conjuntos[it] = it }

        // Función para encontrar el conjunto al que pertenece un nodo
        fun encontrar(nodo: String): String {
            if (conjuntos[nodo] == nodo) return nodo
            return encontrar(conjuntos[nodo]!!)
        }

        // Función para unir dos conjuntos
        fun unir(nodo1: String, nodo2: String) {
            val raiz1 = encontrar(nodo1)
            val raiz2 = encontrar(nodo2)
            if (raiz1 != raiz2) {
                conjuntos[raiz2] = raiz1
            }
        }

        // Ordenar los tramos por costo
        val tramosOrdenados = tramos.sorted()

        // Aplicar el algoritmo de Kruskal
        for (tramo in tramosOrdenados) {
            val origen = tramo.origen
            val destino = tramo.destino

            if (encontrar(origen) != encontrar(destino)) {
                arbolDeExpansionMinima.add(tramo)
                unir(origen, destino)
            }
        }

        return arbolDeExpansionMinima
    }
}

fun main() {
    val nombreArchivo = "input.txt"
    val escaner = Scanner(File(nombreArchivo))

    // Leer las líneas de cantidad de autobuses y rutas (no se utilizan directamente)
    escaner.nextLine()
    escaner.nextLine()

    val red = RedTransporte()

    // Leer los tramos desde el archivo
    while (escaner.hasNextLine()) {
        val linea = escaner.nextLine().split(" ")
        val vehiculo = linea[0]
        val origen = linea[1]
        val destino = linea[2]
        val coste = linea[3].toDouble()

        red.agregarTramo(vehiculo, origen, destino, coste)
    }

    escaner.close()

    // Aplicar Kruskal
    val arbolDeExpansionMinima = red.kruskal()

    // Imprimir la ruta óptima
    println("Ruta óptima (Árbol de Expansión Mínima):")
    arbolDeExpansionMinima.forEach {
        println("${it.vehiculo}: ${it.origen} -> ${it.destino} (${it.coste})")
    }
}
