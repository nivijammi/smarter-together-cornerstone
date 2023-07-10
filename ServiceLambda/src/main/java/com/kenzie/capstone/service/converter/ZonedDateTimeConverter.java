package com.kenzie.capstone.service.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;


public class ZonedDateTimeConverter implements DynamoDBTypeConverter<String, ZonedDateTime> {
    @Override
    public String convert(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

//    @Override
//    public ZonedDateTime unconvert(String dateTimeRepresentation) {
//        return ZonedDateTime.parse(dateTimeRepresentation);
//    }

    @Override
    public ZonedDateTime unconvert(String dateTimeRepresentation) {
        LocalDate localDate = LocalDate.parse(dateTimeRepresentation, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate.atStartOfDay(ZoneOffset.UTC);
    }
}
