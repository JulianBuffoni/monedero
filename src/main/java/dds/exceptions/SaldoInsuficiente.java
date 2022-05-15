package dds.exceptions;

public class SaldoInsuficiente extends RuntimeException {
  public SaldoInsuficiente(String message) {
    super(message);
  }
}