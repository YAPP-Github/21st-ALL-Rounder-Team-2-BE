package com.yapp.artie.global.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

public class DateUtils {

  public static LocalDate getFirstDayOf(int year, int month) {
    return YearMonth.of(year, monthOf(month)).atDay(1);
  }

  public static LocalDate getLastDayOf(int year, int month) {
    return YearMonth.of(year, monthOf(month)).atEndOfMonth();
  }

  private static Month monthOf(int month) {
    return Month.of(month);
  }
}
