package com.dietdecoder.dietdecoder.database;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.util.UUID;

public class Converters {

  // UUIDConverters
  @TypeConverter
  public static String fromUUID(UUID uuid) {
    return uuid.toString();
  }
  @TypeConverter
  public static UUID uuidFromString(String string) {
    return UUID.fromString(string);
  }

  // InstantConverters
  @TypeConverter
  public static Instant toInstant(Long millisecondsSinceEpoch) {
    return millisecondsSinceEpoch == null ? null : Instant.ofEpochMilli(millisecondsSinceEpoch);
  }
  @TypeConverter
  public static Long toTimestamp(Instant instant) {
    return instant == null ? null : instant.toEpochMilli();
  }
}
