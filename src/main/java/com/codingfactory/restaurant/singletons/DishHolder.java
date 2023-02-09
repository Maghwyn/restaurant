package com.codingfactory.restaurant.singletons;

import com.codingfactory.restaurant.models.Dish;

public final class DishHolder {
    private Dish dish;


    private final static DishHolder INSTANCE = new DishHolder();

    private DishHolder() {}

    public static DishHolder getInstance() {
        return INSTANCE;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

}
