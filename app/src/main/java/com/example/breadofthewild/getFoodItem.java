package com.example.breadofthewild;

import java.util.List;

public class getFoodItem implements Runnable {
    private AppDatabase db;
    private RunnableListener runnableListener;

    public getFoodItem(AppDatabase db, RunnableListener runnableListener) {
        this.db = db;
        this.runnableListener = runnableListener;
    }

    @Override
    public void run() {
        runnableListener.onResult(db.foodDAO().getAllFood());
    }
}

interface RunnableListener {
    void onResult(List<Food> food);
}
