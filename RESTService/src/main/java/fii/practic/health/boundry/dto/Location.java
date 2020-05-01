package fii.practic.health.boundry.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    private Integer id;
    private String country;

    @JsonProperty(value = "country_code")
    private String countryCode;

    @JsonProperty(value = "country_population")
    private Integer countryPopulation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getCountryPopulation() {
        return countryPopulation;
    }

    public void setCountryPopulation(Integer countryPopulation) {
        this.countryPopulation = countryPopulation;
    }
}
