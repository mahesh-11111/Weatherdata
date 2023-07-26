
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WeatherApp {
    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("Choose an option:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()

            switch (option) {
                case 1:
                    getWeatherDetails(scanner);
                    break;
                case 2:
                    getWindSpeed(scanner);
                    break;
                case 3:
                    getPressure(scanner);
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

    private static void getWeatherDetails(Scanner scanner) {
        System.out.print("Enter the date (yyyy-MM-dd HH:mm:ss): ");
        String inputDate = scanner.nextLine();
        String apiUrl = API_BASE_URL + "?q=London,uk&appid=" + API_KEY;

        try {
            JSONArray forecastData = makeAPIRequest(apiUrl);
            for (int i = 0; i < forecastData.length(); i++) {
                JSONObject data = forecastData.getJSONObject(i);
                String dateTime = data.getString("dt_txt");
                if (dateTime.equals(inputDate)) {
                    JSONObject main = data.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    System.out.println("Temperature at " + inputDate + " : " + temperature + " °C");
                    return;
                }
            }
            System.out.println("Data not found for the specified date.");
        } catch (IOException e) {
            System.out.println("An error occurred while fetching data from the API.");
        }
    }

    private static void getWindSpeed(Scanner scanner) {
        // Similar implementation as getWeatherDetails
    }

    private static void getPressure(Scanner scanner) {
        // Similar implementation as getWeatherDetails
    }

    private static JSONArray makeAPIRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject();
            return ((Object) jsonObject).getJSONArray("list");
        } else {
            throw new IOException("Failed to fetch data from the API. Response code: " + responseCode);
        }
    }
}

