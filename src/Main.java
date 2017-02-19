import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by User on 19.02.2017.
 */
public class Main {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String TABLE_FILE_NAME = "table.txt";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Введите название входного файла: ");
        String fileIn = reader.readLine();

        long symbolsNumber  = Files.readAllLines(new File(fileIn).toPath())
                .stream()
                .map(String::length)
                .reduce(0, (x, y) -> x + y)
                .longValue();

        System.out.println("Количество символов: " + symbolsNumber + "\n");

        System.out.print("Введите название выходного файла: ");
        String fileOut = reader.readLine();

        System.out.print("Введите -> для шифрования или <- для расшифрования: ");
        String option = reader.readLine();

        File tableFile = new File(TABLE_FILE_NAME);

        if (!tableFile.exists()) {
            System.out.println("Нет файла с таблицей!");

        } else {
            Map<String, String> table = null;
            Stream<String[]> stream = Files.readAllLines(tableFile.toPath())
                    .stream()
                    .map(line -> line.split(" "));

            switch (option) {
                case "->": {
                    table = stream.collect(Collectors.toMap(line -> line[0], line -> line[1]));
                    break;
                }
                case "<-": {
                    table = stream.collect(Collectors.toMap(line -> line[1], line -> line[0]));
                    break;
                }
            }

            long start = System.currentTimeMillis();

            process(fileIn, fileOut, table);

            long finished = System.currentTimeMillis();

            System.out.println("Завершено за " + (finished - start) + " мс.");
        }
    }

    private static void process(String fileIn, String fileOut, Map<String, String> table) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileOut));

        BufferedReader reader = new BufferedReader(new FileReader(fileIn));
        String line;

        while ((line = reader.readLine()) != null) {
            String processedLine = processLine(line, table);
            writer.write(processedLine + "\n");
        }

        reader.close();
        writer.close();
    }

    private static String processLine(String originalLine, Map<String, String> table) {
        StringBuilder result = new StringBuilder();

        int firstLetterIdx = 0, secondLetterIdx;

        while (result.length() < originalLine.length()) {

            firstLetterIdx = findNexLetterIdx(result, originalLine, firstLetterIdx, true);
            if (firstLetterIdx == -1) {
                break;
            }

            secondLetterIdx = firstLetterIdx + 1;

            secondLetterIdx = findNexLetterIdx(result, originalLine, secondLetterIdx, false);
            if (secondLetterIdx == -1) {
                result.append(originalLine.substring(firstLetterIdx));
                break;
            }


            String firstLetter = symbol(originalLine, firstLetterIdx);
            String secondLetter = symbol(originalLine, secondLetterIdx);

            String tablePair = table.get(firstLetter.charAt(0) + secondLetter);

            result.append(symbol(tablePair, 0))
                    .append(firstLetterIdx + 1 == secondLetterIdx ? "" : originalLine.substring(firstLetterIdx + 1, secondLetterIdx))
                    .append(symbol(tablePair, 1));

            firstLetterIdx = secondLetterIdx + 1;
        }
        return result.toString();
    }

    private static int findNexLetterIdx(StringBuilder result, String originalLine, int idx, boolean append) {
        while (idx < originalLine.length() && !ALPHABET.contains(symbol(originalLine, idx))) {

            if (append) {
                result.append(symbol(originalLine, idx));
            }

            idx++;
        }

        return idx == originalLine.length()
                ? -1
                : idx;
    }

    private static String symbol(String line, int idx) {
        return String.valueOf(line.charAt(idx)).toLowerCase();
    }
}