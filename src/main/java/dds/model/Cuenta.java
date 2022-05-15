package dds.model;

import dds.exceptions.MaximaCantidadDepositosException;
import dds.exceptions.MaximoExtraccionDiarioException;
import dds.exceptions.MontoNegativoException;
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

  public void poner /*mal nombre, poner que? en donde?*/ (double cuanto /*mal nombre, cuanto que?*/) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo"); //0 no es un valor negativo, el nombre y msj de la excepción no son correctos
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3 ) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios"); //podría ponerse un atributo que determine la cantidad máxima, para poder cambiarse en caso de ser necesario
    }

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar/*mal nombre, sacar que? de donde?*/ (double cuanto /*mal nombre, cuanto que?*/) { //Long method
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo"); //0 no es un valor negativo, el nombre y msj de la excepción no son correctos
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy; //podría ponerse un atributo en vez de 1000 que determine la cantidad máxima, para poder cambiarse en caso de ser necesario
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000 //podría ponerse un atributo que determine la cantidad máxima, para poder cambiarse en caso de ser necesario
          + " diarios, límite: " + limite);
    }
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) { //podría recibirse como parámetro al movimiento, en vez de sus atributos
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha)) //debería hacer uso de fue Extraido
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
