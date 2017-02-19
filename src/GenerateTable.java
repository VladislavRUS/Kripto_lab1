import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 19.02.2017.
 */
public class GenerateTable {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws IOException {

        File file = new File("table.txt");

        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < ALPHABET.length(); i++) {
            for (int j = 0; j < ALPHABET.length(); j++) {
                String pair = String.valueOf(ALPHABET.charAt(i)) + String.valueOf(ALPHABET.charAt(j));
                stringList.add(pair);
            }
        }

        List<String> shuffled = new ArrayList<>(stringList);
        Collections.shuffle(shuffled);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));

        for (int i = 0; i < stringList.size(); i++) {
            String first = stringList.get(i);
            String second = shuffled.get(i);

            outputStreamWriter.write(first + " " + second + "\n");
        }

        System.out.println("Таблица сгенерирована!");
        outputStreamWriter.close();
    }
}
