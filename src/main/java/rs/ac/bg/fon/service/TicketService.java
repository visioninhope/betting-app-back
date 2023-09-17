package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.Ticket.TicketDTO;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.utility.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TicketService {
    Ticket save(Ticket ticket);

    void updateAllTickets();

    List<Ticket> getUserTickets(String username);

    void processTickets();

    BigDecimal getWagerAmoutForUser(int userId);

    BigDecimal getTotalWinAmountForUser(int userId);

    ApiResponse<?> addNewTicketApiResponse(TicketDTO ticketDTO);
}
