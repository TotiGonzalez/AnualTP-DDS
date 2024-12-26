package ar.edu.utn.frba.dds.model.sensores;

public interface Wsensor {
  Reading getWeight(String serialNumber);
}