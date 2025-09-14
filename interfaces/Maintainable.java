package interfaces;

import exceptions.*;

public interface Maintainable {
    void scheduleMaintenance();
    boolean needsMaintenance();
    void performMaintenance();
}