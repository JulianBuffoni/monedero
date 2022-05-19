package dds.model;

import java.time.LocalDate;

public class Deposito extends Movimiento {
  public Deposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }

  double calcularValor(double saldo) {
    return saldo + getMonto();
  }

  boolean isDeposito() {
    return true;
  }

  boolean isExtraccion(){
    return false;
  }

  public boolean fueExtraido(LocalDate fecha) {
    return false;
  }
}
