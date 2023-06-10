import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.math.*

class Perlin {

    private val permutation = intArrayOf(
        151, 160, 137,  91,  90,  15, 131,  13, 201,  95,  96,  53, 194, 233,   7, 225,
        140,  36, 103,  30,  69, 142,   8,  99,  37, 240,  21,  10,  23, 190,   6, 148,
        247, 120, 234,  75,    0,  26, 197,  62,  94, 252, 219, 203, 117,  35,  11,  32,
        57, 177,  33,  88, 237, 149,  56,  87, 174,  20, 125, 136, 171, 168,  68, 175,
        74, 165,  71, 134, 139,  48,  27, 166,  77, 146, 158, 231,  83, 111, 229, 122,
        60, 211, 133, 230, 220, 105,  92,  41,  55,  46, 245,  40, 244, 102, 143,  54,
        65,  25,  63, 161,   1, 216,  80,  73, 209,  76, 132, 187, 208,  89,  18, 169,
        200, 196, 135, 130, 116, 188, 159,  86, 164, 100, 109, 198, 173, 186,   3,  64,
        52, 217, 226, 250, 124, 123,   5, 202,  38, 147, 118, 126, 255,  82,  85, 212,
        207, 206,  59, 227,  47,  16,  58,  17, 182, 189,  28,  42, 223, 183, 170, 213,
        119, 248, 152,   2,  44, 154, 163,  70, 221, 153, 101, 155, 167,  43, 172,   9,
        129,  22,  39, 253,  19,  98, 108, 110,  79, 113, 224, 232, 178, 185, 112, 104,
        218, 246,  97, 228, 251,  34, 242, 193, 238, 210, 144,  12, 191, 179, 162, 241,
        81,  51, 145, 235, 249,  14, 239, 107,  49, 192, 214,  31, 181, 199, 106, 157,
        184,  84, 204, 176, 115, 121,  50,  45, 127,   4, 150, 254, 138, 236, 205,  93,
        222, 114,  67,  29,  24,  72, 243, 141, 128, 195,  78,  66, 215,  61, 156, 180
    )

/*
    private val permutation = intArrayOf(
        59, 62, 53, 35, 35, 5, 51, 5, 78, 37, 37, 20, 76, 91, 2, 88,
        54, 14, 40, 11, 27, 55, 3, 38, 14, 94, 8, 3, 9, 74, 2, 58,
        96, 47, 91, 29, 0, 10, 77, 24, 36, 98, 85, 79, 45, 13, 4, 12,
        22, 69, 12, 34, 92, 58, 21, 34, 68, 7, 49, 53, 67, 65, 26, 68,
        29, 64, 27, 52, 54, 18, 10, 65, 30, 57, 61, 90, 32, 43, 89, 47,
        23, 82, 52, 90, 86, 41, 36, 16, 21, 18, 96, 15, 95, 40, 56, 21,
        25, 9, 24, 63, 0, 84, 31, 28, 81, 29, 51, 73, 81, 34, 7, 66,
        78, 76, 52, 50, 45, 73, 62, 33, 64, 39, 42, 77, 67, 72, 1, 25,
        20, 85, 88, 98, 48, 48, 1, 79, 14, 57, 46, 49, 100, 32, 33, 83,
        81, 80, 23, 89, 18, 6, 22, 6, 71, 74, 10, 16, 87, 71, 66, 83,
        46, 97, 59, 0, 17, 60, 63, 27, 86, 60, 39, 60, 65, 16, 67, 3,
        50, 8, 15, 99, 7, 38, 42, 43, 30, 44, 87, 90, 69, 72, 43, 40,
        85, 96, 38, 89, 98, 13, 94, 75, 93, 82, 56, 4, 74, 70, 63, 94,
        31, 20, 56, 92, 97, 5, 93, 41, 19, 75, 83, 12, 70, 78, 41, 61,
        72, 32, 80, 69, 45, 47, 19, 17, 49, 1, 58, 99, 54, 92, 80, 36,
        87, 44, 26, 11, 9, 28, 95, 55, 50, 76, 30, 25, 84, 23, 61, 70
    )*/
/*

    private val permutation = intArrayOf(
        72,23,168,139,33,246,106,225,120,154,38,158,215,192,247,127,155,100,16,249,37,233,54,1,95,39,62,157,20,27,240,49,130,94,180,150,167,0,79,107,173,207,252,53,175,181,241,148,131,244,211,29,86,116,104,220,55,98,28,188,186,89,110,118,151,112,160,224,91,239,133,230,80,162,67,64,227,56,232,70,135,59,195,69,47,74,177,142,57,73,6,223,143,96,87,255,196,144,99,156,172,203,11,210,253,179,228,42,124,65,26,128,5,83,60,251,85,218,66,152,166,147,250,30,126,81,63,235,117,149,134,201,176,46,3,111,90,92,45,189,122,197,229,236,101,238,204,4,205,222,184,183,165,125,105,76,191,9,24,25,19,93,88,34,138,2,199,129,22,18,219,178,115,213,206,12,43,212,40,109,193,153,7,84,52,140,103,248,159,242,31,71,194,132,187,61,51,216,35,113,170,146,32,243,254,217,163,137,231,164,10,123,200,8,161,114,245,136,82,102,208,13,14,36,141,68,221,78,21,75,190,171,198,108,48,226,145,214,182,202,15,77,237,17,97,50,234,41,119,44,58,185,174,121,209,169
    )
*/

    private val p = IntArray(512) {
        if (it < 256) permutation[it] else permutation[it - 256]
    }

    fun noise(x: Double, y: Double, z: Double): Double {
        // Find unit cube that contains point
        val xi = floor(x).toInt() and 255
        val yi = floor(y).toInt() and 255
        val zi = floor(z).toInt() and 255

        // Find relative x, y, z of point in cube
        val xx = x - floor(x)
        val yy = y - floor(y)
        val zz = z - floor(z)

        // Compute fade curves for each of xx, yy, zz
        val u = fade(xx)
        val v = fade(yy)
        val w = fade(zz)

        // Hash co-ordinates of the 8 cube corners
        // and add blended results from 8 corners of cube

        val a  = p[xi] + yi
        val aa = p[a] + zi
        val ab = p[a + 1] + zi
        val b  = p[xi + 1] + yi
        val ba = p[b] + zi
        val bb = p[b + 1] + zi

        return lerp(w, lerp(v, lerp(u, grad(p[aa], xx, yy, zz),
            grad(p[ba], xx - 1, yy, zz)
        ),
            lerp(u, grad(p[ab], xx, yy - 1, zz),
                grad(p[bb], xx - 1, yy - 1, zz)
            )
        ),
            lerp(v, lerp(u, grad(p[aa + 1], xx, yy, zz - 1),
                grad(p[ba + 1], xx - 1, yy, zz - 1)
            ),
                lerp(u, grad(p[ab + 1], xx, yy - 1, zz - 1),
                    grad(p[bb + 1], xx - 1, yy - 1, zz - 1)
                )
            )
        )
    }

    private fun fade(t: Double) = t * t * t * (t * (t * 6 - 15) + 10)

    private fun lerp(t: Double, a: Double, b: Double) = a + t * (b - a)

    private fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        // Convert low 4 bits of hash code into 12 gradient directions
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return (if ((h and 1) == 0) u else -u) +
                (if ((h and 2) == 0) v else -v)
    }
}

fun main() {

    val f = File("C:\\Users\\Andrew\\eclipse-workspaces\\java\\Personal\\WaterPump\\permutations.txt")
    if(f.exists())
        Files.delete(File("C:\\Users\\Andrew\\eclipse-workspaces\\java\\Personal\\WaterPump\\permutations.txt").toPath())

    val perlin = Perlin()

    var highest = 0.0
    var lowest = 0.0

    val mapSize = 0..326
    val scale = mapSize.last/100.0

    for (i in mapSize) {
        for (j in mapSize) {
            val randomNoise = abs(perlin.noise(
                scale*j.toDouble(),
                scale*i.toDouble(),
                scale*126
            ))
            if(randomNoise > highest)
                highest = randomNoise
            if(randomNoise < lowest)
                lowest = randomNoise
        }
    }

    println("$lowest $highest")

    for (i in mapSize) {
        var line = "";
        for (j in mapSize) {
            val randomNoise = abs(perlin.noise(
                scale*j.toDouble(),
                scale*i.toDouble(),
                scale*126
            ))/(highest-lowest)

            line += "$randomNoise "
        }
        FileOutputStream(f, true).bufferedWriter().use { writer ->
            writer.appendLine(line)
        }
    }

}

