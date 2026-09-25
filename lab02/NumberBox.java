import java.util.List;

public class NumberBox<T extends Number> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public double sum(List<T> numbers) {
        double total = 0;
        for (T n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }
}
