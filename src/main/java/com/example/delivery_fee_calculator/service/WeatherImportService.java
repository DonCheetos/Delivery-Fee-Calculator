package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.entity.Weather;
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

/**
 * Service that imports weather information from the weather portal, every certain given time
 */
@Service
public class WeatherImportService {

    private final WeatherService weatherService;

    /**
     * Constructs a WeatherServiceImpl with the given WeatherService.
     * <p>
     *     Spring Boot will automatically inject the WeatherService bean via constructor injection.
     * </p>
     *
     * @param weatherService the WeatherRepository bean injected by Spring
     */
    public WeatherImportService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Scheduled method that triggers the weather data import based on cron configuration.
     *
     * <p>
     *     The cron expression is defined in application.properties. Key in use: {@code weather.import.cron}
     * </p>
     *
     */
    //
    @Scheduled(cron = "${weather.import.cron}")
    public void scheduledTrigger() {
        importWeatherData();
    }

    /**
     * Reads data from weather portal of the Estonian Environment Agency and saves it into database
     *
     * <p>
     *     Currently saves only "Tallinn-Harku", "Tartu-Tõravere", "Pärnu" observations
     * </p>
     */
    private void importWeatherData() {
        System.out.println("ImportWeatherData triggered at " + java.time.LocalDateTime.now());
        try {
            System.out.println("Fetching XML data...");

            // Since URL is deprecated, Will use URI instead
            URL url = URI.create("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php").toURL();

            try (InputStream in = url.openStream()) {
                // Extracts XML root from the URL given
                Element root = parseXML(in);

                // Return early if root was null
                if (root == null) return;

                // List of station element nodes
                NodeList nodeList = root.getElementsByTagName("station");

                // Retrieve the "timestamp" attribute
                Long timestamp = Long.valueOf(root.getAttribute("timestamp"));

                // List of observations we care about
                final List<String> allowedList = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");


                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        String name = getElementText(element, "name");

                        if (allowedList.contains(name)) {
                            // Read observation data safely

                            String wmo = getElementText(element, "wmocode");
                            Double airtemperature = Double.valueOf(getElementText(element, "airtemperature"));
                            Double windspeed = Double.valueOf(getElementText(element, "windspeed"));
                            String phenomenon = getElementText(element, "phenomenon");

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
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Parses an XML document from the provided InputStream and returns its root element.
     *
     * <p>This method is Public to facilitate testing of XML parsing logic</p>
     *
     * @param inputStream InputStream that contains XML
     * @return XML root element
     */
    public Element parseXML(InputStream inputStream){
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML document directly from InputStream
            Document document = builder.parse(inputStream);

            // Normalize document (to avoid issues with different XML formatting)
            document.getDocumentElement().normalize();

            // Return the root element
            return document.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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