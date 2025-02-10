import org.removingalldoubt.values.sharable.sharableSample
import org.removingalldoubt.values.dataclass.dataClassSample
import org.removingalldoubt.values.with.withSample

fun main() {
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

