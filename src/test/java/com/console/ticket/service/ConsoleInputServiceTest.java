package com.console.ticket.service;

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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@Tag("shouldFinish")
@ExtendWith(
        MockitoExtension.class
)
class ConsoleInputServiceTest {
    @Mock
    private static Company company;
    private static String inputLine;
    private static ConsoleInputService consoleInputService;
    @BeforeAll
    static void initialize() {
        company = CompanyTestBuilder.aCompany()
                .withName("Evroopt")
                .withAddress("Minsk, Kalvariyskaja 17, 1")
                .withCurrency(Currency.USA.getCurrency())
                .build();
        inputLine = "1-2 3-4 card-1111\nexit\n";
        consoleInputService = ConsoleInputService.getInstance();
    }

    @DisplayName("check read console correctly")
    @Test
    void checkReadConsoleIsCorrect() {
        InputStream input = new ByteArrayInputStream(inputLine.getBytes(StandardCharsets.UTF_8));
        System.setIn(input);

        consoleInputService.readConsole(company);

        verify(company).getName();
        verify(company).getAddress();
        verify(company).getCurrency();
    }
}
