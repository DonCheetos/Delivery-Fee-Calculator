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
import java.net.URL;
import java.util.List;

@Service
public class WeatherImportService {

    @Autowired
    private WeatherService weatherService;

    //@Scheduled(cron = "0 15 * * * ?")
    @Scheduled(fixedDelay = 5000)
    // Reads data from weather portal of the Estonian Environment Agency and saves it into DB
    public void importWeatherData() {

        System.out.println("importWeatherData triggered at " + java.time.LocalDateTime.now());
        try {
            // Create a DocumentBuilder
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // Parse the XML content from a page
            Document document = builder.parse(new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php").openStream());

            // Normalize in other words clean up the XML document structure, for less error-prone solution
            document.getDocumentElement().normalize();

            // Get the root element (the <observations> element)
            Element root = document.getDocumentElement();

            // Retrieve the "timestamp" attribute as a String
            String timestampStr = root.getAttribute("timestamp");

            // List of observations we care about
            final List<String> allowedList = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

            NodeList nodeList = root.getElementsByTagName("observation");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (allowedList.contains(element.getAttribute("name"))) {

                        // Read in observation data
                        String name = element.getAttribute("name");
                        String wmo = element.getAttribute("wmocode");
                        Double airtemperature = Double.parseDouble(element.getAttribute("airtemperature"));
                        Double windspeed = Double.parseDouble(element.getAttribute("windspeed"));
                        String phenomenon  = element.getAttribute("phenomenon");

                        // New object weather
                        Weather weather = new Weather();
                        weather.setTimestamp(timestampStr);
                        weather.setName(name);
                        weather.setWmo(wmo);
                        weather.setTemp(airtemperature);
                        weather.setWind(windspeed);
                        weather.setPhenomenon(phenomenon);

                        // Save into DB
                        weatherService.saveWeather(weather);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace(); // or use a proper logging framework
        }

    }
}