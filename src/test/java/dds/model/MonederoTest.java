package dds.model;

import dds.exceptions.MaximaCantidadDepositosException;
import dds.exceptions.MaximoExtraccionDiarioException;
import dds.exceptions.MontoNegativoException;
import dds.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(3856, cuenta.getSaldo());
  }

  @Test
  void MasDeTresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    cuenta.setSaldo(90);
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.sacar(1001);
    });
  }

  @Test
  void ExtraerMasDe1000() {
    cuenta.setSaldo(5000);
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.sacar(1001);
    });
  }

  @Test
  void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  void montoExtraidoHoy() {
    cuenta.poner(1500);
    cuenta.sacar(100);
    assertEquals(100,cuenta.getMontoExtraidoA(LocalDate.now()));
  }
}