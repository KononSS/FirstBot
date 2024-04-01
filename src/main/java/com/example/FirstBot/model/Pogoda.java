package com.example.FirstBot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Data
public class Pogoda {
    double lon;
    double lat;
    List<Weather> weather;
    String base;
    Main main;
    int visibility;
    Wind wind;
    Clouds clouds;
    long dt;
    Sys sys;
    int timezone;
    int id;
    String name;
    int cod;

}
@Data
class Weather {
    int id;
    String main;
    String description;
    String icon;

}
class Main {
    @Setter
    double temp;
    @Setter
    double feelsLike;
    @Setter
    double tempMin;
    @Setter
    double tempMax;
    @Setter
    int pressure;
    @Setter
    int humidity;
    @Setter
    double seaLevel;
    @Setter
    double grndLevel;

    @Override
    public String toString() {
        return
                "температура " + temp +" градусов,\n"+
                "давление " + pressure + ",\n"+
                "влажность " + humidity +",\n";
    }
}

class Wind {
    @Setter
    double speed;
    @Setter
    int deg;
    @Setter
    double gust;

    @Override
    public String toString() {
        return
                "скорость ветра " + speed +",\n"+
                "угол ветра " + deg +",\n"+
                "порывы ветра " + gust;
    }
}

@Data
class Clouds {
    int all;
}
@Data
class Sys {
    int type;
    long id;
    String country;
    long sunrise;
    long sunset;
}

