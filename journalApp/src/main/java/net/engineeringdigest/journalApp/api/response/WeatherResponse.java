package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.connector.Request;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class WeatherResponse {
   // private Request request;
   // private Location location;
    private Current current;
  @Getter
  @Setter
  public class Current{
       // @JsonProperty("observation_time")
       // ublic String observationTime;
        private int temperature;
@JsonProperty("weather_descriptions")
        public  List<String> weather_description;


        private int feelslike;

    }

   /* public class Request{
        public String type;
        private String query;
        private String language;
        private String unit;
    }
    public class Location {
        private String name;
        private String country;
        private String region;
        private String lat;
        private String lon;
        private String timezone_id;
        private String localtime;
        private int localtime_epoch;
        private String utc_offset;
    } */
}


