package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Service
public class WeatherImportService {

    @Autowired
    private WeatherService weatherService;

    // Every hour at minute 15
    @Scheduled(cron = "0 15 * * * ?")
    //@Scheduled(fixedDelay = 10000)
    // Reads data from weather portal of the Estonian Environment Agency and saves it into DB
    private void importWeatherData() {
        System.out.println("ImportWeatherData triggered at " + java.time.LocalDateTime.now());
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            System.out.println("Fetching XML data...");

            // Since URL is deprecated, Will use URI instead
            URL url = URI.create("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php").toURL();


            // Try-with-resources ensures InputStream is closed
            try (InputStream stream = url.openStream()) {
                // Parse the XML document directly from InputStream
                Document document = builder.parse(stream);

                // Normalize document (to avoid issues with different XML formatting)
                document.getDocumentElement().normalize();

                // Get the root element
                Element root = document.getDocumentElement();

                // Retrieve the "timestamp" attribute
                Long timestamp = Long.valueOf(root.getAttribute("timestamp"));

                // List of observations we care about
                final List<String> allowedList = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

                // Get all <station> elements from root element "observations"
                NodeList nodeList = root.getElementsByTagName("station");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        String name = getElementText(element, "name");

                        if (allowedList.contains(name)) {
                            // Read observation data safely

                            String wmo = getElementText(element, "wmocode");
                            Double airtemperature = Double.valueOf(getElementText(element,"airtemperature"));
                            Double windspeed = Double.valueOf(getElementText(element,"windspeed"));
                            String phenomenon = getElementText(element,"phenomenon");

                            // Create a Weather object
                            Weather weather = new Weather();
                            weather.setTimestamp(timestamp);
                            weather.setName(name);
                            weather.setWmo(wmo);
                            weather.setTemp(airtemperature);
                            weather.setWind(windspeed);
                            weather.setPhenomenon(phenomenon);

                            // Save information into DB
                            weatherService.saveWeather(weather);
                        }
                    }
                }
                System.out.println("Saved weather information, timestamp: " + timestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility method to get text content of an element
    private static String getElementText(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return "";
    }
}