package dds.model;

import java.time.LocalDate;

public class Extraccion extends Movimiento {
  public Extraccion(LocalDate fecha, double monto) {
    super(fecha, monto);
  }

  double calcularValor(double saldo) {
    return saldo + getMonto();
  }

  boolean isDeposito() {
    return false;
  }

  boolean isExtraccion(){
    return true;
  }

  public boolean fueExtraido(LocalDate fecha) {
    return this.esDeLaFecha(fecha);
  }
}
