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
import ch.dss.gadgeothek.service.ItemSelectionListener;

public class GadgetRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public void setGadgets(List<Gadget> gadgets) {
        this.gadgets = gadgets;
    }

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

    private List<Gadget> gadgets;
    private ItemSelectionListener selectionListener;
    private Activity activity;


    public GadgetRecyclerAdapter(ItemSelectionListener selectionListener, Activity activity) {
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
        return new RecyclerItemViewHolder(view, itemName, itemManufacturer, itemState, itemMessage);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Gadget gadget = gadgets.get(position);
        GadgetApplication app = (GadgetApplication) activity.getApplication();
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        holder.setItemName(gadget.getName());
        holder.setItemManufacturer(gadget.getManufacturer());
        holder.setItemState(gadget.getCondition().toString());
        holder.setItemMessage("Neupreis: " + Double.toString(gadget.getPrice()));
        final int positionGadget = app.getAllGadgets().indexOf(gadget);
        holder.itemParent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onItemSelected(positionGadget);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gadgets == null ? 0 : gadgets.size();
    }
}



