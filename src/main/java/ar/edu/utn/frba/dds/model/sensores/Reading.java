package ar.edu.utn.frba.dds.model.sensores;

public class Reading {
  private double value; //valor
  private String unit; //unidades en que se mide

  public Reading(double value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  public String getUnit() {
    return unit;
  }

  public double getValue() {
    return value;
  }
}