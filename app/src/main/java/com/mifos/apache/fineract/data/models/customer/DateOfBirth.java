package com.mifos.apache.fineract.data.models.customer;

public final class DateOfBirth {

  private Integer year;
  private Integer month;
  private Integer day;

  public DateOfBirth() {
    super();
  }

  public Integer getYear() {
    return this.year;
  }

  public void setYear(final Integer year) {
    this.year = year;
  }

  public Integer getMonth() {
    return this.month;
  }

  public void setMonth(final Integer month) {
    this.month = month;
  }

  public Integer getDay() {
    return this.day;
  }

  public void setDay(final Integer day) {
    this.day = day;
  }
}