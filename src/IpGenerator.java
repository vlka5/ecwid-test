import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Util class to generate file with ip addresses
 */
public class IpGenerator {

    public static void generateIpAndWriteToFile(String pathToFile, long count) {
        Random r = new Random();
        try (
                var fw = new FileWriter(pathToFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fw)
        ) {
            for (int i = 0; i < count; i++) {
                bufferedWriter.append(String.valueOf(r.nextInt(256))).append(".").append(String.valueOf(r.nextInt(256))).append(".").append(String.valueOf(r.nextInt(256))).append(".").append(String.valueOf(r.nextInt(256))).append('\n');
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
