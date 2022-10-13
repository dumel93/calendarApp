package com.calendar.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Locale;
import java.util.TimeZone;


@SpringBootApplication
public class CalendarApp extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(CalendarApp.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        return application.sources(CalendarApp.class);
    }
}