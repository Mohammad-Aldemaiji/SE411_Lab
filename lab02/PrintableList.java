import java.util.Arrays;
import java.util.List;

public class PrintableList<T> {
    private List<T> items;

    public PrintableList(T[] itemsArray) {
        this.items = Arrays.asList(itemsArray);
    }

    public void printAll() {
        for (T item : items) {
            System.out.println(item);
        }
    }
}
