package com.codingfactory.restaurant.singletons;

import com.codingfactory.restaurant.models.Dish;

public final class DishHolder {
    private Dish dish;


    /**
     * Instance of our dishHolder Instance
     */
    private final static DishHolder INSTANCE = new DishHolder();

    private DishHolder() {}

    /**
     * get the instance of DishHolder
     * @return
     */
    public static DishHolder getInstance() {
        return INSTANCE;
    }

    /**
     * Getter of dish
     * @return
     */
    public Dish getDish() {
        return dish;
    }

    /**
     * Setter of dish
     * @param dish
     */
    public void setDish(Dish dish) {
        this.dish = dish;
    }

}
