package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class NasaApi {

    public static String getNasaData() {
        String apiKey = "G7RLVSb79IM9CEAVbz0ObIb5VBdUYZY7kUBmgANN";
        String urlString = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;
        
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder responseContent = new StringBuilder();

        try {
            URL url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if (status != 200) {
                throw new RuntimeException("Error: " + status + " - No se pudo obtener información de NASA.");
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }

            JSONObject nasaJson = new JSONObject(responseContent.toString());
            String title = nasaJson.getString("title");
            String explanation = nasaJson.getString("explanation");
            String imageUrl = nasaJson.getString("url");
            String date = nasaJson.getString("date");
            String copyright = nasaJson.getString("copyright");

            return "\n"+"Autor: "+ copyright +"\nDate: "+ date +"\n\nTítulo: " + title + "\n\nExplicación: " + explanation + "\n\nURL de la imagen: " + imageUrl+"\n";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener los datos de NASA.";
        } finally {
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("---------------------NASA API--------------------");
        System.out.println("-------------------------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.print("Desea ver la foto del dia de hoy?\nSeleccione: si/no: ");
        String resul = sc.nextLine().toLowerCase();
        if (resul.equals("si")){
            String nasaInfo = getNasaData();
            System.out.println(nasaInfo);
        }
        else if (resul.equals("no")) {
            System.out.println("Saliendo...");
        } else {
            System.out.println("Respuesta no válida. Saliendo...");
        }
        
        sc.close();    
    }
}
