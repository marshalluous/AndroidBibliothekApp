package ch.dss.gadgeothek;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.dss.gadgeothek.domain.Loan;
import ch.dss.gadgeothek.service.Callback;
import ch.dss.gadgeothek.service.ItemSelectionListener;
import ch.dss.gadgeothek.service.LibraryService;

public class LoanListFragment extends Fragment {

    private ItemSelectionListener itemSelectionCallback = null;
    private LoanRecyclerAdapter adapter;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = this.getActivity();
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new LoanRecyclerAdapter(itemSelectionCallback);
        setupRecyclerView(recyclerView);

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                setupRecyclerView(recyclerView);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getLoansFromDatabase();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        if (!(activity instanceof ItemSelectionListener)) {
            throw new IllegalStateException(activity.getClass() + " must implement ItemSelectionListener interface");
        }
        itemSelectionCallback = (ItemSelectionListener) activity;
    }

    private void getLoansFromDatabase() {
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                List<Loan> loanList = new ArrayList<>(input);
                GadgetApplication app = (GadgetApplication) getActivity().getApplication();
                app.setLoanList(loanList);
                adapter.setLoanList(loanList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(activity, "Login Fehlgeschlagen!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
