package com.example.multiwind.rxtest;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    public boolean isWorking = true;
    private Button firstShowBtn;
    private Button secondShowBtn;
    private Button loadText;
    private Button complete;
    private Button error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goToBluetoothActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
            }
        });

        initViews();
        initObservers();

        final FirstFragment firstFragment = new FirstFragment();
        final SecondFragment secondFragment = new SecondFragment();
        initListeners(firstFragment, secondFragment);
    }


    private void initListeners(final FirstFragment firstFragment, final SecondFragment secondFragment) {
        firstShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, firstFragment);
                fragmentTransaction.commit();
            }
        });


        secondShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, secondFragment);
                fragmentTransaction.commit();
            }
        });

        loadText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String s = String.valueOf(textToBeLoaded.getText());
//                int i = 0;
//                while (isWorking) {
//                    subject.onNext(s + i++);
//                }
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//         subject.onCompleted();
                                            isWorking = false;
                                        }
                                    }
        );


        error.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
//          subject.onError(new Throwable());
                                         isWorking = false;
                                     }
                                 }
        );
    }

    private void initViews() {
        firstShowBtn = (Button) findViewById(R.id.showFirstFrag);
        secondShowBtn = (Button) findViewById(R.id.showSecondFrag);
        final EditText textToBeLoaded = (EditText) findViewById(R.id.testToBeLoaded);
        loadText = (Button) findViewById(R.id.loadText);
        complete = (Button) findViewById(R.id.completeLoading);
        error = (Button) findViewById(R.id.throwError);
    }

    @NonNull
    private Observable<String> initObservers() {
        final Observable<String> just = Observable.just("one", "two", "three");
        final Observable<String> empty = Observable.empty();
        Observable<Long> values = Observable.interval(1000, TimeUnit.MILLISECONDS);
        empty.subscribe(new OnNextAction<String>("empty"), new OnErrorAction("empty"), new OnCompleate("empty"));
        just.subscribe(new OnNextAction<String>("just"), new OnErrorAction("just"), new OnCompleate("just"));
        values.subscribe(new OnNextAction<Long>("values"), new OnErrorAction("values"), new OnCompleate("values"));
        return just;
    }

    private void makeDelay(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class OnNextAction<T> implements rx.functions.Action1<T> {

        private String tagAddition;

        public OnNextAction(String string) {
            this.tagAddition = string;
        }

        @Override
        public void call(T i) {
            Log.d("TAG:" + tagAddition, String.valueOf(i));
        }
    }


    private class OnErrorAction implements rx.functions.Action1<Throwable> {
        private String tagAddition;

        public OnErrorAction(String string) {
            this.tagAddition = string;
        }

        @Override
        public void call(Throwable throwable) {
            Log.d("TAG:" + tagAddition, "Ooops something thrown");
            throwable.printStackTrace();
        }
    }

    private class OnCompleate implements rx.functions.Action0 {

        private String tagAddition;

        public OnCompleate(String string) {
            this.tagAddition = string;
        }

        @Override
        public void call() {
            Log.d("TAG:" + tagAddition, "Task is complete");
        }
    }
}
