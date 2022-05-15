package dds.model;

import dds.exceptions.MaximaCantidadDepositosException;
import dds.exceptions.MaximoExtraccionDiarioAlcanzadoException;
import dds.exceptions.MontoNegativoOCeroException;
import dds.exceptions.SaldoInsuficiente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private double limiteExtraccion;
  private List<Movimiento> movimientos = new ArrayList<>();

  private int depositosMaximos = 3;

  Cuenta(double limiteExtraccion){
    this.limiteExtraccion = limiteExtraccion;
  }

  public void depositarDinero(double montoADepositar) {
  validarDeposito(montoADepositar);
  agregarMovimiento(new Movimiento(LocalDate.now(), montoADepositar, true));
  }

  public void validarDeposito(double montoADepositar){
    if (montoADepositar <= 0) {
      throw new MontoNegativoOCeroException(montoADepositar + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= this.depositosMaximos ) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + this.depositosMaximos + " depositos diarios");
    }
  }

  public void extraerDinero(double montoAExtraer) {
    validarExtraccion(montoAExtraer);
    agregarMovimiento(new Movimiento(LocalDate.now(), montoAExtraer, false));
  }

  public void validarExtraccion(double montoAExtraer){
    if (montoAExtraer <= 0) {
      throw new MontoNegativoOCeroException(montoAExtraer + ": el monto a extraer debe ser un valor positivo");
    }
    if (getSaldo() - montoAExtraer < 0) {
      throw new SaldoInsuficiente("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = this.limiteExtraccion - montoExtraidoHoy;
    if (montoAExtraer > limite) {
      throw new MaximoExtraccionDiarioAlcanzadoException("No puede extraer mas de $ " + this.limiteExtraccion
          + " diarios, límite: " + limite);
    }
  }

  public void agregarMovimiento(Movimiento movimiento) {
    setSaldo(simularMovimiento(movimiento));
    movimientos.add(movimiento);
  }
  public double simularMovimiento(Movimiento movimiento) {
    if (movimiento.isDeposito()) {
      return saldo + movimiento.getMonto();
    } else {
      return saldo - movimiento.getMonto();
    }
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
