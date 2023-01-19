package com.medicaldata.rest.business;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.CompileResponse;
import com.medicaldata.rest.business.exceptions.CompileTealProgramException;
import com.medicaldata.rest.business.service.SmartContractGenerator;
import com.medicaldata.rest.data.model.Survey;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SmartContractFactory {

    private final String CLEAR_STATE_PROGRAM_PATH = "/teal/clear.teal";

    private final String[] headers = {"X-API-Key"};
    private final String[] values = {""};/*Enter your purestake.io token*/

    private AlgodClient algodClient;
    private SmartContractGenerator smartContractGenerator;

    public SmartContractFactory(AlgodClient algodClient, SmartContractGenerator smartContractGenerator) {
        this.algodClient = algodClient;
        this.smartContractGenerator = smartContractGenerator;
    }

    public TEALProgram createApprovalProgramFrom(Survey survey) {

        return compileProgram(smartContractGenerator.generateTealSmartContract());
    }

    public TEALProgram createClearStateProgram() {

        String clearStateProgramAsString = readFile(CLEAR_STATE_PROGRAM_PATH);
        return compileProgram(clearStateProgramAsString);

    }

    private TEALProgram compileProgram(String tealProgramAsStream) {
        Response<CompileResponse> compileResponse;
        try {
            compileResponse = algodClient.TealCompile()
                    .source(tealProgramAsStream.getBytes(UTF_8)).execute(headers, values);
        } catch (Exception e) {
            throw new CompileTealProgramException(e);
        }

        return new TEALProgram(compileResponse.body().result);
    }

    private String readFile(String path) {
        try {
            InputStream resourceAsStream = this.getClass().getResourceAsStream(path);
            return IOUtils.toString(resourceAsStream, UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
