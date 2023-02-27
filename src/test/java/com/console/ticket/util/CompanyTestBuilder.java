package com.console.ticket.util;

import com.console.ticket.entity.Company;
import com.console.ticket.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCompany")
@With
public class CompanyTestBuilder implements TestBuilder<Company> {
    private String name = "";
    private String address = "";
    private String currency = Currency.USA.getCurrency();

    @Override
    public Company build() {
        final var company = Company.builder()
                .name(name)
                .address(address)
                .currency(currency)
                .build();
        return company;
    }
}
