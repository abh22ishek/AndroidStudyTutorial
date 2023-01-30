package com.example.androidstudytutorial.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.androidstudytutorial.MainActivity;
import com.example.androidstudytutorial.listners.CallFragment;
import com.example.androidstudytutorial.model.Images;
import com.example.androidstudytutorial.recyclerview.ListRecyclerView;
import com.mine.mywallpaper.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private CallFragment callFragment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callFragment = (CallFragment) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.main_fragment, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        populateRecyclerViewStaggered();
    }




    private void populateRecyclerViewStaggered()
    {
        boolean isTab = getResources().getBoolean(R.bool.is_tablet);
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        if(isTab){
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        }else{
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

         final ListRecyclerView listRecyclerView = new ListRecyclerView(getActivity(), displayList(getActivity()), callFragment);
        recyclerView.setAdapter(listRecyclerView);

    }

    private List<Images> displayList(Context context){

        List<Images> imagesList =new ArrayList<>();
        TypedArray images =  context.getResources().obtainTypedArray(R.array.wallpaper_array);
        // int [] images ={R.drawable.wall_paper1,R.drawable.wall_paper2,R.drawable.wall_paper3,R.drawable.wall_paper4, R.drawable.wall_3,R.drawable.wall_paper5};

        for(int k=0;k<images.length();k++){
            Images i = new Images(1,images.getResourceId(k,0),"wallpaper"+"_"+k);
            imagesList.add(i);
        }

        images.recycle();

        return imagesList;
    }
}
