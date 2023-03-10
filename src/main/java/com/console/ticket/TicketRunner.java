package com.console.ticket;

import com.console.ticket.entity.Company;
import com.console.ticket.service.ConsoleInputService;
import com.console.ticket.entity.Currency;
import lombok.NoArgsConstructor;

/**
 * @Author Evgeny Leshok
 */
@NoArgsConstructor
public class TicketRunner {
    Company company;
    ConsoleInputService consoleInputService;

    public static void main(String[] args) {
        TicketRunner ticketRunner = new TicketRunner();
        ticketRunner.initialize();
        ticketRunner.setConsoleInputService();
        ticketRunner.run();
    }

    public void setCompany() {
        company = new Company("Evroopt", "Minsk, Kalvariyskaja 17, 1", Currency.USA.getCurrency());
    }

    public void setConsoleInputService() {
        consoleInputService = ConsoleInputService.getInstance();
    }

    public void initialize() {
        setCompany();
    }

    public void run() {
        consoleInputService.readConsole(company);
    }
}
