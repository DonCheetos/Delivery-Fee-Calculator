package com.example.delivery_fee_calculator.service;

import com.example.delivery_fee_calculator.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for validating weather importation functionality,
 * including XML parsing and full data import integration.
 */
@SpringBootTest
@ActiveProfiles("test") // Ensures the tests run against the test DB profile
public class WeatherImportServiceTest {
    @Autowired
    WeatherService weatherService;

    @Autowired
    WeatherRepository weatherRepository;

    WeatherImportService weatherImportService;

    @BeforeEach
    public void init() {
        // Clean the database and initialize the import service before each test
        weatherRepository.deleteAll();
        weatherImportService = new WeatherImportService(weatherService);
    }

    /**
     * Unit test for validating XML parsing from weather API response.
     * Ensures the weather XML is parsed correctly and data fields are extracted.
     */
    @Test
    public void testXMLParse() {
        String testXML ="<observations timestamp=\"1742135859\">"+
                        "<station>\n" +
                        "<name>Tallinn-Harku</name>\n" +
                        "<wmocode>26038</wmocode>\n" +
                        "<longitude>24.602891666624284</longitude>\n" +
                        "<latitude>59.398122222355134</latitude>\n" +
                        "<phenomenon>Clear</phenomenon>\n" +
                        "<visibility>35.0</visibility>\n" +
                        "<precipitations>0</precipitations>\n" +
                        "<airpressure>1009.3</airpressure>\n" +
                        "<relativehumidity>47</relativehumidity>\n" +
                        "<airtemperature>1.1</airtemperature>\n" +
                        "<winddirection>313</winddirection>\n" +
                        "<windspeed>2</windspeed>\n" +
                        "<windspeedmax>4.8</windspeedmax>\n" +
                        "<waterlevel/>\n" +
                        "<waterlevel_eh2000/>\n" +
                        "<watertemperature/>\n" +
                        "<uvindex>0.8</uvindex>\n" +
                        "<sunshineduration>459</sunshineduration>\n" +
                        "<globalradiation>338</globalradiation>\n" +
                        "</station>"+
                        "</observations>";



        // Create Root element from the String XML
        Element root = weatherImportService.parseXML(new ByteArrayInputStream(testXML.getBytes(StandardCharsets.UTF_8)));

        // Extract information
        Long timestamp = Long.valueOf(root.getAttribute("timestamp"));
        Element station = (Element) root.getElementsByTagName("station").item(0);

        String stationName = station.getElementsByTagName("name").item(0).getTextContent().trim();
        String wmo = station.getElementsByTagName("wmocode").item(0).getTextContent().trim();
        Double airtemperature = Double.valueOf(station.getElementsByTagName("airtemperature").item(0).getTextContent().trim());
        Double windspeed = Double.valueOf(station.getElementsByTagName("windspeed").item(0).getTextContent().trim());
        String phenomenon = station.getElementsByTagName("phenomenon").item(0).getTextContent().trim();


        // Asses if the parsed information from XML is correct
        assertEquals("Tallinn-Harku", stationName);
        assertEquals("26038", wmo);
        assertEquals(1.1, airtemperature);
        assertEquals(2.0, windspeed);
        assertEquals("Clear", phenomenon);
        assertEquals(1742135859L,timestamp);
    }

    /**
     * Integration test for full weather data import via scheduled trigger.
     * Validates that data is fetched, parsed, and persisted correctly for all required stations.
     */
    @Test
    public void testWeatherImport() {
        // Manually trigger importation of data
        weatherImportService.scheduledTrigger();

        // For current implementation should return only 3, for Tallinn-Harku, Pärnu and Tartu-Tõravere
        assertEquals(3,weatherRepository.count());

        // Make sure all observations are present and only once
        assertEquals(1, weatherRepository.findByNameOrderByTimestampDesc("Tallinn-Harku").size());
        assertEquals(1, weatherRepository.findByNameOrderByTimestampDesc("Pärnu").size());
        assertEquals(1, weatherRepository.findByNameOrderByTimestampDesc("Tartu-Tõravere").size());
    }
}
