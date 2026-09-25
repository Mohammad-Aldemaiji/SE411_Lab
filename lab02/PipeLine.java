import java.util.function.Function;

/**
 * A reusable pipeline that applies a sequence of transformations to a value.
 * T = the original input type.
 * R = the current output type (changes each time a transformer is added).
 */
public class PipeLine<T, R> {
    private final T initialValue;
    private final Function<T, R> function;

    private PipeLine(T initialValue, Function<T, R> function) {
        this.initialValue = initialValue;
        this.function = function;
    }

    // Start a new pipeline with an initial value. Output type == input type initially.
    public static <T> PipeLine<T, T> of(T initialValue) {
        return new PipeLine<>(initialValue, Function.identity());
    }

    // Add a transformer that turns the current output type R into a new type NEW_R.
    // Returns a NEW pipeline (immutable) whose output type is now NEW_R.
    public <NEW_R> PipeLine<T, NEW_R> add(Transformer<R, NEW_R> transformer) {
        Function<T, NEW_R> combined = input -> transformer.transform(function.apply(input));
        return new PipeLine<>(initialValue, combined);
    }

    // Run all the transformations in order and return the final result.
    public R execute() {
        return function.apply(initialValue);
    }
}
