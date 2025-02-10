import org.removingalldoubt.values.sharable.sharableSample
import org.removingalldoubt.values.dataclass.dataClassSample
import org.removingalldoubt.values.with.withSample
import kotlin.math.min

fun main() {
    repeat(100) {
        val size = it + 1
        val power = min(32 - (size - 1).countLeadingZeroBits(),  5)
        println("$size -> $power, effective size ${1 shl power}")
    }

    println("## Data classes ##")
    dataClassSample()
    println()

    println("## Sharable<T> classes ##")
    sharableSample()
    println()

    println("## With-style value classes ##")
    withSample()
    println()
}

