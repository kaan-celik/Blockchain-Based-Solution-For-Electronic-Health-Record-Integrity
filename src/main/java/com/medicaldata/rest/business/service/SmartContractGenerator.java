package com.medicaldata.rest.business.service;

import static java.util.stream.Collectors.joining;

public class SmartContractGenerator {

    private final String smartContract = "#pragma version 2\n" +
            "\n" +
            "\n" +
            "// Check is creation or else\n" +
            "int 0\n" +
            "txn ApplicationID\n" +
            "==\n" +
            "bz not_creation\n" +
            "//Then created data put the sender ID to the global state\n" +
            "byte \"Creator\"\n" +
            "txn Sender\n" +
            "app_global_put\n" +
            "\n" +
            "int 1\n" +
            "return\n" +
            "\n" +
            "//Finalize creation-------------------------------------------\n" +
            "\n" +
            "not_creation:\n" +
            "\n" +
            "//Has Token ?\n" +
            "int 0\n" +
            "int 105379867\n" +
            "asset_holding_get AssetBalance\n" +
            "pop\n" +
            "// does sender have at least 1 vote token\n" +
            "int 1\n"+
            ">=\n"+
            "bz failed\n" +
            "//Opt In for local storage -------------------------------------------\n" +
            "txn OnCompletion\n" +
            "int OptIn\n" +
            "==\n" +
            "bnz register\n" +
            "\n" +
            "int 0\n" +
            "txn ApplicationID\n" +
            "app_opted_in\n" +
            "bz failed //If ıts true failed\n" +
            "\n" +
            "// Check any data inserted before\n" +
            "int 0 // sender\n" +
            "txn ApplicationID\n" +
            "byte \"Register\"\n" +
            "app_local_get_ex\n" +
            "bz failed //If ıts true failed\n" +
            "\n" +
            "int 0 // sender\n" +
            "txn ApplicationID\n" +
            "byte \"Completed\"\n" +
            "app_local_get_ex\n" +
            "bnz failed //If ıts true failed\n" +
            "\n" +
            "int 0 // sender\n" +
            "byte \"Completed\"\n" +
            "txna ApplicationArgs 0\n" +
            "app_local_put\n" +
            "byte \"KeyValue\"\n" +
            "txna ApplicationArgs 1\n" +
            "app_local_put\n" +
            "//Put data to the local storage of current sender\n" +
            "int 1\n" +
            "return\n" +
            "\n" +
            "register:\n" +
            "// Check opted in before\n" +
            "int 0 // sender\n" +
            "txn ApplicationID\n" +
            "byte \"Register\"\n" +
            "app_local_get_ex\n" +
            "bnz failed //If ıts true failed\n" +
            "\n" +
            "int 0 // sender\n" +
            "byte \"Register\"\n" +
            "txna ApplicationArgs 0\n" +
            "app_local_put\n" +
            "//Finalize program successfully\n" +
            "int 1\n" +
            "return\n" +
            "\n" +
            "failed:\n" +
            "int 0\n" +
            "return\n" +
            "\n" +
            "//Finalize program failed";


    public String generateTealSmartContract() {
        return smartContract;
    }
}
