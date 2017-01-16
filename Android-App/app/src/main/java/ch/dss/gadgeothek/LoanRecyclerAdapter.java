package ch.dss.gadgeothek;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ch.dss.gadgeothek.domain.Gadget;
import ch.dss.gadgeothek.domain.Loan;
import ch.dss.gadgeothek.service.ItemSelectionListener;

public class LoanRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemName;
        private final TextView itemManufacturer;
        private final TextView itemState;
        private final TextView itemMessage;
        private View itemParent;

        public RecyclerItemViewHolder(final View parent, TextView itemName, TextView itemManufacturer, TextView itemState, TextView itemMessage) {
            super(parent);

            this.itemParent = parent;
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

    private List<Loan> loans;
    private ItemSelectionListener selectionListener;

    public LoanRecyclerAdapter(ItemSelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void setLoanList(List<Loan> loanList) {
        loans = loanList;
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
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        Gadget loan = loans.get(position).getGadget();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateToReturn = dateFormat.format(loans.get(position).overDueDate());
        holder.setItemName(loan.getName());
        holder.setItemManufacturer(loan.getManufacturer());
        holder.setItemState("Ausgeliehen"); //Sind sowieso immer ausgeliehen.
        holder.setItemMessage("Zurückgeben am: " + dateToReturn);
        holder.itemParent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loans == null ? 0 : loans.size();
    }
}



