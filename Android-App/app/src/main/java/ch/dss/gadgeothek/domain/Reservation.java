package ch.dss.gadgeothek.domain;

import java.util.Date;

public class Reservation {

    private String id;
    private Gadget gadget;
    private Date reservationDate;
    private boolean finished;
    private int watingPosition;
    private boolean isReady;

    public String getReservationId() {
        return id;
    }

    public void setReservationId(String id) {
        this.id = id;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setData(Reservation reservation) {
        this.finished = reservation.finished;
    }

    public Gadget getGadget() {
        return gadget;
    }

    public void setGadget(Gadget gadget) {
        this.gadget = gadget;
    }

    public int getWatingPosition() {
        return watingPosition;
    }

    public void setWatingPosition(int watingPosition) {
        this.watingPosition = watingPosition;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Reservation other = (Reservation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
