package com.codingfactory.restaurant.interfaces;

import com.codingfactory.restaurant.controllers.FactoryController;

/**
 * Interface FactoryInterface used to force inject the FactoryController reference
 * to the children controller.
 */
public interface FactoryInterface {
    void setFactoryController(FactoryController controller);
}
