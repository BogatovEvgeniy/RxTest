package com.example.multiwind.rxtest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.Observer;

public class FirstFragment extends Fragment implements Observer<Integer> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        return view;
    }

    @Override
    public void onCompleted() {
        View view = getView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.loadedText);
            textView.setText("Compleated");
        }
    }

    @Override
    public void onError(Throwable e) {
        View view = getView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.loadedText);
            textView.setText("Oops error heppend");
        }
    }

    @Override
    public void onNext(Integer i) {
        View view = getView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.loadedText);
            textView.setText(i);
        }
    }
}
