package ch.dss.gadgeothek;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.dss.gadgeothek.domain.Gadget;
import ch.dss.gadgeothek.domain.Reservation;
import ch.dss.gadgeothek.service.ItemSelectionListener;

public class ReservationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemName;
        private final TextView itemManufacturer;
        private final TextView itemState;
        private final TextView itemMessage;
        private View itemParent;

        public RecyclerItemViewHolder(final View parent, TextView itemName, TextView itemManufacturer, TextView itemState, TextView itemMessage) {
            super(parent);

            itemParent = parent;
            this.itemName = itemName;
            this.itemManufacturer = itemManufacturer;
            this.itemState = itemState;
            this.itemMessage = itemMessage;
        }

        public void setItemName(CharSequence text) {
            itemName.setText(text);
        }

        public void setItemManufacturer(CharSequence text){
            itemManufacturer.setText(text);
        }

        public void setItemState(CharSequence text){
            itemState.setText(text);
        }

        public void setItemMessage(CharSequence text){
            itemMessage.setText(text);
        }
    }

    private List<Reservation> reservations;
    private ItemSelectionListener selectionListener;
    private Activity activity;

    public ReservationRecyclerAdapter(ItemSelectionListener selectionListener, Activity activity) {
        this.activity = activity;
        this.selectionListener = selectionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_gadget, parent, false);
        TextView itemName = (TextView) view.findViewById(R.id.itemName);
        TextView itemManufacturer = (TextView) view.findViewById(R.id.itemManufacturer);
        TextView itemState = (TextView) view.findViewById(R.id.itemState);
        TextView itemMessage = (TextView) view.findViewById(R.id.itemMessage);
        return new ReservationRecyclerAdapter.RecyclerItemViewHolder(view, itemName, itemManufacturer, itemState, itemMessage);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ReservationRecyclerAdapter.RecyclerItemViewHolder holder = (ReservationRecyclerAdapter.RecyclerItemViewHolder) viewHolder;
        final GadgetApplication app = (GadgetApplication) activity.getApplication();
        final Gadget reservedGadget = reservations.get(position).getGadget();
        String itemState;
        String itemMessage;
        if(reservations.get(position).isReady()){
            itemState = "Abholbereit";
        } else {
            itemState = "Reserviert";
        }
        if(reservations.get(position).getWatingPosition() != 0){
            itemMessage = "Warteposition: " + Integer.toString(reservations.get(position).getWatingPosition());
        } else {
            itemMessage = "Sie dürfen Ihr Gerät abholen";
        }

        holder.setItemName(reservedGadget.getName());
        holder.setItemManufacturer(reservedGadget.getManufacturer());
        holder.setItemState(itemState);
        holder.setItemMessage(itemMessage);
        final int positionGadget = app.getAllGadgets().indexOf(reservedGadget);
        holder.itemParent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onItemSelected(positionGadget);
            }
        });
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservations = reservationList;
    }

    @Override
    public int getItemCount() {
        return reservations == null ? 0 : reservations.size();
    }
}
