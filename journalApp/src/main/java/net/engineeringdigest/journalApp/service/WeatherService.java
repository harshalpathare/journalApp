package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.constants.Placeholders;
import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

  @Value("${weather.api.key}")
  private  String API_KEY ;
   //private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

   @Autowired
   private RestTemplate restTemplate; // ✅ injected

    @Autowired
private AppCache appCache;

    @Autowired
    private RedisService redisService;
   public WeatherResponse getWeather(String city) throws JsonProcessingException {

      WeatherResponse weatherResponse =  redisService.get("weather_of"+city,WeatherResponse.class);

      if(weatherResponse != null){
          return weatherResponse;
      }
      else{
          String finalAPI = appCache.appCache.get(AppCache.key.WEATHER_API.toString())
                  .replace(Placeholders.CITY, city)
                  .replace(Placeholders.API_KEY, API_KEY);
          HttpHeaders httpHeaders = new HttpHeaders();
          httpHeaders.set("key","value");
          ResponseEntity<WeatherResponse> response =
                  restTemplate.exchange(
                          finalAPI,
                          HttpMethod.GET,
                          null,
                          WeatherResponse.class
                  );
WeatherResponse body = response.getBody();

if(body != null){
    redisService.set("weather_of_"+city, body,300l);
}


          return response.getBody();
      }



      //User user  = User.builder().userName("666").password("666").build();
      //HttpEntity<User> httpEntity= new HttpEntity<>(user,httpHeaders);

   /*   ResponseEntity<WeatherResponse> response =
              restTemplate.exchange(
                      finalAPI,
                      HttpMethod.POST,
                      null,
                      WeatherResponse.class
              );*/


   }
}
