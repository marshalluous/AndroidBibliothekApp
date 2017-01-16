package ch.dss.gadgeothek;


import java.util.ArrayList;
import java.util.List;

import ch.dss.gadgeothek.domain.Gadget;
import ch.dss.gadgeothek.domain.Loan;
import ch.dss.gadgeothek.domain.Reservation;

/**
 * Created by Marcel on 10/31/2016.
 */

public class GadgetApplication extends android.app.Application {
    private List<Gadget> gadgetList = new ArrayList<>();
    private List<Loan> loanList = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private List<Gadget> allGadgets = new ArrayList<>();


    public List<Gadget> getGadgets() {
        return gadgetList;
    }

    public void setGadgets(List<Gadget> gadgets) {
        gadgetList = gadgets;
    }

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public List<Reservation> getReservationList() {
        return reservations;
    }

    public void setReservationList(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Gadget> getAllGadgets() {
        return allGadgets;
    }

    public void setAllGadgets(List<Gadget> allGadgets) {
        this.allGadgets = allGadgets;
    }
}
