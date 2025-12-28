package software.ulpgc.moneycalculator.architecture.control;

import software.ulpgc.moneycalculator.architecture.exceptions.RequestAmmountException;

public interface Command {
    void execute() throws RequestAmmountException;
}
