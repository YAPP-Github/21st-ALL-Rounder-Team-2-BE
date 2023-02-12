package com.yapp.artie.global.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;

public class DateUtils {

  public static LocalDateTime getFirstDayOf(int year, int month) {
    return YearMonth.of(year, monthOf(month)).atDay(1).atStartOfDay();
  }

  public static LocalDateTime getLastDayOf(int year, int month) {
    return YearMonth.of(year, monthOf(month)).atEndOfMonth().atTime(LocalTime.MAX);
  }

  public static LocalDateTime getStartTimeOf(int year, int month, int day) {
    return YearMonth.of(year, monthOf(month)).atDay(day).atStartOfDay();
  }

  public static LocalDateTime getEndTimeOf(int year, int month, int day) {
    return YearMonth.of(year, monthOf(month)).atDay(day).atTime(LocalTime.MAX);
  }

  private static Month monthOf(int month) {
    return Month.of(month);
  }
}
