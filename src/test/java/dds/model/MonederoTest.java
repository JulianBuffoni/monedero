package dds.model;

import dds.exceptions.MaximaCantidadDepositosException;
import dds.exceptions.MaximoExtraccionDiarioAlcanzadoException;
import dds.exceptions.MontoNegativoOCeroException;
import dds.exceptions.SaldoInsuficiente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta(1000);
  }

  @Test
  void depositoExitoso() {
    cuenta.depositarDinero(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void depositoFallidoPorPonerMontoNegativo() {
    assertThrows(MontoNegativoOCeroException.class, () -> cuenta.depositarDinero(-1500));
  }

  @Test
  void cantidadMaximaDeDepositos() {
    cuenta.depositarDinero(1500);
    cuenta.depositarDinero(456);
    cuenta.depositarDinero(1900);
    assertEquals(3856, cuenta.getSaldo());
  }

  @Test
  void cantidadMaximaDeDepositosSuperada() {
    cuenta.depositarDinero(1500);
    cuenta.depositarDinero(456);
    cuenta.depositarDinero(1900);
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.depositarDinero(245);
    });
  }

  @Test
  void extraccionConSaldoInsuficiente() {
    cuenta.setSaldo(90);
    assertThrows(SaldoInsuficiente.class, () -> {
      cuenta.extraerDinero(1001);
    });
  }

  @Test
  void montoDeExtraccionMaximoSuperado() {
    cuenta.setSaldo(5000);
    assertThrows(MaximoExtraccionDiarioAlcanzadoException.class, () -> {
      cuenta.extraerDinero(1001);
    });
  }

  @Test
  void errorDeExtraccionDeMontoNegativo() {
    assertThrows(MontoNegativoOCeroException.class, () -> cuenta.extraerDinero(-500));
  }

  @Test
  void montoExtraidoHoy() {
    cuenta.depositarDinero(1500);
    cuenta.extraerDinero(100);
    assertEquals(100,cuenta.getMontoExtraidoA(LocalDate.now()));
  }
}