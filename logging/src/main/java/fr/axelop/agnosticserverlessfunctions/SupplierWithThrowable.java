package fr.axelop.agnosticserverlessfunctions;

@FunctionalInterface
public interface SupplierWithThrowable<RETURNED, THROWN extends Throwable> {

    RETURNED get() throws THROWN;
    
}
