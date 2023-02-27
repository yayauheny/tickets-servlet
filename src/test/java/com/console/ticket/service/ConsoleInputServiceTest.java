package com.console.ticket.service;

import com.console.ticket.constants.Constants;
import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import com.console.ticket.util.CompanyTestBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;

//@Disabled("complete in the near future")
@Tag("shouldFinish")
@ExtendWith(
        MockitoExtension.class
)
class ConsoleInputServiceTest {
    @Mock
    private static Company company;
    private static String inputLine;
//    private static Path currentTicketPath;
    private static String[] receiptRaws;

    @BeforeAll
    static void initialize() {
        company = CompanyTestBuilder.aCompany()
                .withName("Evroopt")
                .withAddress("Minsk, Kalvariyskaja 17, 1")
                .withCurrency(Currency.USA.getCurrency())
                .build();
        inputLine = "1-2 3-4 card-1111\nexit\n";
//        currentTicketPath = Constants.DEFAULT_RECEIPT_PATH;
    }

    @DisplayName("check read console correctly")
    @Test
    void checkReadConsoleIsCorrect() {
        InputStream input = new ByteArrayInputStream(inputLine.getBytes(StandardCharsets.UTF_8));
        System.setIn(input);

        ConsoleInputService.readConsole(company);

        //assert that all company fields using in receipt building
        verify(company).getName();
        verify(company).getAddress();
        verify(company).getCurrency();
    }

    @AfterAll
    static void clear() {
        FileService.clearReceiptFolder(Constants.DEFAULT_RECEIPT_PATH);
    }
}