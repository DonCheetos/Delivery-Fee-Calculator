package com.example.delivery_fee_calculator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing weather data for storage in the database
 *
 * <p>
 *     Using lombok for reduction of boilerplate code
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(nullable = false)
    private String name;

    // Assuming WMO code for the station can not be Null
    @Column(nullable = false)
    private String wmo;

    @Column(nullable = false)
    private Double temp;

    @Column(nullable = false)
    private Double wind;

    @Column(nullable = false)
    private String phenomenon;

    @Column(nullable = false)
    private Long timestamp;
}
