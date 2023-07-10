package com.kenzie.appserver.repositories.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeConverter  implements DynamoDBTypeConverter<String, ZonedDateTime> {
    @Override
    public String convert(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE); // serializing [someObject to string] the date into a string
    }

//    @Override
//    public ZonedDateTime unconvert(String dateTimeRepresentation) {
//        return ZonedDateTime.parse(dateTimeRepresentation); // deserializing [string > someObject] the date into a string
//    }

    @Override
    public ZonedDateTime unconvert(String dateTimeRepresentation) {
        LocalDate localDate = LocalDate.parse(dateTimeRepresentation, DateTimeFormatter.ISO_LOCAL_DATE);
        return localDate.atStartOfDay(ZoneOffset.UTC);
    }
}
