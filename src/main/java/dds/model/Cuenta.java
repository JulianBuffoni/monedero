package dds.model;

import dds.exceptions.MaximaCantidadDepositosException;
import dds.exceptions.MaximoExtraccionDiarioException;
import dds.exceptions.MontoNegativoOCeroException;
import dds.exceptions.SaldoInsuficiente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private double limiteExtraccion = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  private int depositosMaximos = 3;

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  } //no tiene sentido settar movimientos de esta manera. Deberían ir agregándose cada vez que se realicen

  public void depositarDinero(double montoADepositar) {
    if (montoADepositar <= 0) {
      throw new MontoNegativoOCeroException(montoADepositar + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= this.depositosMaximos ) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + this.depositosMaximos + " depositos diarios");
    }

    new Movimiento(LocalDate.now(), montoADepositar, true).agregateA(this);
  }

  public void extraerDinero(double montoAExtraer) { //Long method
    if (montoAExtraer <= 0) {
      throw new MontoNegativoOCeroException(montoAExtraer + ": el monto a ingresar debe ser un valor positivo"); //0 no es un valor negativo, el nombre y msj de la excepción no son correctos
    }
    if (getSaldo() - montoAExtraer < 0) {
      throw new SaldoInsuficiente("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = this.limiteExtraccion - montoExtraidoHoy;
    if (montoAExtraer > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + this.limiteExtraccion
          + " diarios, límite: " + limite);
    }
    new Movimiento(LocalDate.now(), montoAExtraer, false).agregateA(this);
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
