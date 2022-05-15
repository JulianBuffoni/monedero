package dds.model;

import dds.exceptions.MaximaCantidadDepositosException;
import dds.exceptions.MaximoExtraccionDiarioException;
import dds.exceptions.MontoNegativoOCeroException;
import dds.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  } //no tiene sentido settar movimientos de esta manera. Deberían ir agregándose cada vez que se realicen

  public void depositarDinero(double montoADepositar) {
    if (montoADepositar <= 0) {
      throw new MontoNegativoOCeroException(montoADepositar + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3 ) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios"); //podría ponerse un atributo que determine la cantidad máxima, para poder cambiarse en caso de ser necesario
    }

    new Movimiento(LocalDate.now(), montoADepositar, true).agregateA(this);
  }

  public void extraerDinero(double montoAExtraer) { //Long method
    if (montoAExtraer <= 0) {
      throw new MontoNegativoOCeroException(montoAExtraer + ": el monto a ingresar debe ser un valor positivo"); //0 no es un valor negativo, el nombre y msj de la excepción no son correctos
    }
    if (getSaldo() - montoAExtraer < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy; //podría ponerse un atributo en vez de 1000 que determine la cantidad máxima, para poder cambiarse en caso de ser necesario
    if (montoAExtraer > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000 //podría ponerse un atributo que determine la cantidad máxima, para poder cambiarse en caso de ser necesario
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
