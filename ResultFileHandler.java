package FinalProject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultFileHandler {

    public static void saveResult(String name, int score, int total) {

        try {
            File file = new File("quizresults.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
                String time = sdf.format(new Date());
                
                bw.write("Name: " + name);
                bw.newLine();
                bw.write("Score: " + score + "/" + total);
                bw.newLine();
                bw.write("Date: " + time);
                bw.newLine();
                bw.write("--------------------------");
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("File error");
        }
    }
}
