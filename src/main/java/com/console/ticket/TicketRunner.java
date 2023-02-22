package com.console.ticket;

import com.console.ticket.data.DataBase;
import com.console.ticket.entity.Company;
import com.console.ticket.service.ConsoleInputService;
import com.console.ticket.util.Currencies;

/**
 * @Author Evgeny Leshok
 */
public class TicketRunner {
    Company company;
    DataBase dataBase;

    public TicketRunner() {

    }

    public static void main(String[] args) {
        TicketRunner ticketRunner = new TicketRunner();
        ticketRunner.initialize();
    }

    public void setCompany() {
        company = new Company("Evroopt", "Minsk, Kalvariyskaja 17, 1",
                10, Currencies.USA.getCurrency());
    }

    public void setDataBase() {
        dataBase = DataBase.getInstance();
        dataBase.createTables();
    }

    public void initialize() {
        setCompany();
        setDataBase();
        ConsoleInputService.readConsole(company, dataBase);
    }
}
