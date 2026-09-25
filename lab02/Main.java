import java.util.List;

public class Main {
    public static void main(String[] args) {

        // ---------- Exercise 1: PrintableList ----------
        System.out.println("=== Exercise 1: PrintableList ===");
        String[] names = {"Alice", "Bob", "Charlie"};
        PrintableList<String> stringList = new PrintableList<>(names);
        stringList.printAll();

        // ---------- Exercise 2: NumberBox ----------
        System.out.println("\n=== Exercise 2: NumberBox ===");
        NumberBox<Integer> intBox = new NumberBox<>();
        intBox.setItem(5);
        System.out.println("Integer item: " + intBox.getItem());
        System.out.println("Sum of [1,2,3]: " + intBox.sum(List.of(1, 2, 3)));

        NumberBox<Double> doubleBox = new NumberBox<>();
        doubleBox.setItem(3.14);
        System.out.println("Double item: " + doubleBox.getItem());
        System.out.println("Sum of [1.5, 2.5]: " + doubleBox.sum(List.of(1.5, 2.5)));

        // ---------- Exercise 3: PipeLine ----------
        System.out.println("\n=== Exercise 3: PipeLine ===");
        PipeLine<Integer, Integer> pipeline = PipeLine.of(5);

        PipeLine<Integer, String> toStringPipeline =
                pipeline.add(x -> "Value: " + x);

        PipeLine<Integer, Integer> lengthPipeline =
                toStringPipeline.add(String::length);

        System.out.println("Final result: " + lengthPipeline.execute());

        // ---------- Exercise 4: Wildcards ----------
        System.out.println("\n=== Exercise 4: Wildcards ===");
        printList(List.of("x", "y", "z"));
        printList(List.of(1, 2, 3));
        System.out.println("Sum: " + sumNumbers(List.of(1, 2, 3, 4)));
    }

    // Accepts a list of any type
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    // Accepts a list of Number or any subclass of Number
    public static double sumNumbers(List<? extends Number> numbers) {
        double total = 0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }
}
