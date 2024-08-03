import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
// import java.time.temporal.ChronoField;
// import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static List<String> randomMenu = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'randomize' to generate and display the random menu, or 'extract' to generate the menu CSV file:");

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine().trim().toLowerCase();

            switch (userInput) {
                case "randomize":
                    String mainDishesPath = "C:\\Users\\gunaw\\OneDrive\\Documents\\GitHub\\Menu_Randomizer\\main_menus.txt";
                    String extraDishesPath = "C:\\Users\\gunaw\\OneDrive\\Documents\\GitHub\\Menu_Randomizer\\extra_menus.txt";

                    List<String> mainDishes = readDishesFromFile(mainDishesPath);
                    List<String> extraDishes = readDishesFromFile(extraDishesPath);

                    randomMenu = prepareRandomMenu(mainDishes, extraDishes, 8, 2024);
                    displayRandomMenu(randomMenu); // Display the randomized menu
                    System.out.println("Menu has been randomized. Type 'extract' to save it to a CSV file.");
                    break;
                case "extract":
                    if (!randomMenu.isEmpty()) {
                        String csvOutputPath = "C:\\Users\\gunaw\\OneDrive\\Documents\\CATERING\\august_2024_menu.csv";
                        outputMenuToCSV(randomMenu, csvOutputPath);
                        System.out.println("Menu has been extracted to CSV.");
                    } else {
                        System.out.println("Please randomize the menu first by typing 'randomize'.");
                    }
                    break;
                default:
                    System.out.println("Unknown command. Please type 'randomize' or 'extract'.");
                    break;
            }

            System.out.println("Type 'randomize' to regenerate the random menu or 'extract' to save it to a CSV file:");
        }

        scanner.close();
    }

    static List<String> readDishesFromFile(String filePath) {
        List<String> dishes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dishes.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file:");
            e.printStackTrace();
        }
        return dishes;
    }

    static List<String> prepareRandomMenu(List<String> mainDishes, List<String> extraDishes, int month, int year) {
        List<String> menu = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate date = yearMonth.atDay(1);
        // int daysInMonth = yearMonth.lengthOfMonth();

        Collections.shuffle(mainDishes);
        Collections.shuffle(extraDishes);

        int mainIndex = 0, extraIndex = 0;
        while (date.getMonthValue() == month) {
            if (date.getDayOfWeek().getValue() < 6) {  // Monday to Friday
                String menuForDay = date.getDayOfMonth() + "," + mainDishes.get(mainIndex) + " + " + extraDishes.get(extraIndex);
                menu.add(menuForDay);
                mainIndex = (mainIndex + 1) % mainDishes.size();
                extraIndex = (extraIndex + 1) % extraDishes.size();
            }
            date = date.plusDays(1);
        }
        return menu;
    }

    static void outputMenuToCSV(List<String> menu, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("Monday,Tuesday,Wednesday,Thursday,Friday\n"); // Header
            int dayCount = 0;
            for (String line : menu) {
                writer.write(line.split(",")[1]);  // Write the menu only
                if (++dayCount % 5 == 0) {
                    writer.newLine();  // New line every 5 days (week)
                } else {
                    writer.write(",");  // Comma separates days of the week
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file:");
            e.printStackTrace();
        }
    }

    // Method to display the randomized menu on the console
    static void displayRandomMenu(List<String> menu) {
        System.out.println("Randomized Menu for August 2024:");
        for (String line : menu) {
            if (!line.isEmpty()) {
                System.out.println(line.replace(",", ": "));
            } else {
                System.out.println("----- New Week -----"); // Visual separator for weeks
            }
        }
    }
}
