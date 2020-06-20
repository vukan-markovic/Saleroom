package vukan.com.apursp.ui.pocetna;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vukan.com.apursp.R;
import vukan.com.apursp.adapters.ProductRecyclerViewAdapter;

public class PocetnaFragment extends Fragment implements ProductRecyclerViewAdapter.ListItemClickListener {
    private ProductRecyclerViewAdapter adapter;
    SearchView search;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pocetna, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PocetnaViewModel pocetnaViewModel = new ViewModelProvider(this).get(PocetnaViewModel.class);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductRecyclerViewAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        search = view.findViewById(R.id.searchView);

        pocetnaViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);
            recyclerView.setAdapter(adapter);
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(String productID) {
        search.setQuery("", false);
        search.clearFocus();
        PocetnaFragmentDirections.PocetnaToProizvodFragmentAction action = PocetnaFragmentDirections.pocetnaToProizvodFragmentAction();
        action.setProductId(productID);
        Navigation.findNavController(requireView()).navigate(action);
    }
}
