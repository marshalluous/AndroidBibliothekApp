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

import java.util.List;

import ch.dss.gadgeothek.domain.Reservation;
import ch.dss.gadgeothek.service.Callback;
import ch.dss.gadgeothek.service.ItemSelectionListener;
import ch.dss.gadgeothek.service.LibraryService;

public class ReservationListFragment extends Fragment {

    private ItemSelectionListener itemSelectionCallback = null;
    private ReservationRecyclerAdapter adapter;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);
        activity = getActivity();
        adapter = new ReservationRecyclerAdapter(itemSelectionCallback, activity);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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
        getReservationsFromDatabase();
        recyclerView.setAdapter(adapter);
    }

    private void getReservationsFromDatabase() {
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                List<Reservation> reservationList = input;
                GadgetApplication app = (GadgetApplication) getActivity().getApplication();
                app.setReservationList(reservationList);
                adapter.setReservationList(reservationList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(activity, "Login Fehlgeschlagen!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void onAttach(Context activity){
        super.onAttach(activity);

        if (!(activity instanceof ItemSelectionListener)) {
            throw new IllegalStateException(activity.getClass() + " must implement ItemSelectionListener interface");
        }
        itemSelectionCallback = (ItemSelectionListener) activity;
    }
}
