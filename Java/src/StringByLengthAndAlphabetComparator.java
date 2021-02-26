import java.util.Comparator;

public class StringByLengthAndAlphabetComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;

        if (o1.length() > o2.length()) {
            return 1;
        } else if (o1.length() < o2.length()) {
            return -1;
        }
        return o1.compareTo(o2);
    }
}
